package truesight.http.request

import burp.IHttpService
import burp.IParameter
import burp.IRequestInfo
import org.json.JSONArray
import org.json.JSONObject
import truesight.Extension
import truesight.http.Variable
import truesight.http.body.Body
import truesight.http.body.Form
import truesight.http.body.Generic
import java.net.URI
import java.net.URL

data class Request(
    val method: Method,
    /** Original URL excluding the query parameters and the "ref" (also known as "fragment"). */
    val url: URL,
    val protocolVersion: String,
    /** Original headers excluding 'Cookie'. */
    val headers: List<Variable>,
    val queryParameters: List<Variable>,
    /** Cookies obtained from the original 'Cookie' header. */
    val cookies: List<Variable>,
    val body: Body
) {
    val httpService: IHttpService = Extension.helpers.buildHttpService(url.host, url.port, url.protocol)

    val queryString =
        if (queryParameters.isNotEmpty())
            "?" + queryParameters.joinToString("&")
        else
            ""

    val requestLine = "$method ${url.path}$queryString $protocolVersion"

    fun toBytes(): ByteArray = Extension.helpers.buildHttpMessage(buildHeaders(), body.toBytes())
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put(Keys.METHOD, method)
        json.put(Keys.URL, url)
        json.put(Keys.PROTOCOL_VERSION, protocolVersion)
        json.putOpt(Keys.HEADERS, headers.map { it.toJson() })
        json.put(Keys.QUERY_PARAMETERS, queryParameters.map { it.toJson() })
        json.put(Keys.COOKIES, cookies.map { it.toJson() })
        json.put(Keys.BODY, body.toJson())
        return json
    }

    private fun buildHeaders(): List<String> {
        val headers = (listOf(requestLine) + headers.map { it.toString() }).toMutableList()
        if (cookies.isNotEmpty())
            headers.add(cookies.joinToString("; "))
        return headers
    }

    companion object {
        fun from(service: IHttpService, bytes: ByteArray): Request {
            val info = Extension.helpers.analyzeRequest(service, bytes)
            val formData = getFormData(info)
            return Request(
                method = Method.from(info.method),
                url = info.url.trimQueryAndRef(),
                protocolVersion = info.getProtocolVersion(),
                headers = getHeaders(info),
                cookies = getCookies(info),
                queryParameters = getQueryParameters(info),
                body =
                    if (formData.isNotEmpty())
                        Form(formData)
                    else
                        Generic(bytes.copyOfRange(info.bodyOffset, bytes.size))
            )
        }

        fun from(json: JSONObject): Request {
            val body = json.get(Keys.BODY)
            return Request(
                method = Method.from(json.getString(Keys.METHOD)),
                url = URL(json.getString(Keys.URL)),
                protocolVersion = json.getString(Keys.PROTOCOL_VERSION),
                headers = getHeaders(json.getJSONArray(Keys.HEADERS)),
                queryParameters = getQueryParameters(json.getJSONArray(Keys.QUERY_PARAMETERS)),
                cookies = getCookies(json.getJSONArray(Keys.COOKIES)),
                body =
                    when (body) {
                        is JSONArray -> Form(body)
                        is String -> Generic(body)
                        else -> throw RuntimeException("Unknown body type")
                    }
            )
        }

        private fun getHeaders(info: IRequestInfo) = info.headers.asSequence()
            .drop(1) // The first item is always the request-line: discard it.
            .map { it.split(": ", limit = 2) }
            .filter { it[0].lowercase() != "cookie" } // Discard the 'Cookie' header: it is stored in 'cookies'.
            .map { Variable(name = it[0], value = it[1], Variable.Type.HEADER) }
            .toList()

        private fun getHeaders(json: JSONArray) = json.asSequence()
            .map { Variable(it as JSONObject, Variable.Type.HEADER) }
            .toList()

        private fun getQueryParameters(info: IRequestInfo) = info.parameters.asSequence()
            .filter { it.type == IParameter.PARAM_URL }
            .map { Variable(it.name, it.value, Variable.Type.QUERY_PARAMETER) }
            .toList()

        private fun getQueryParameters(json: JSONArray) = json.asSequence()
            .map { Variable(it as JSONObject, Variable.Type.QUERY_PARAMETER) }
            .toList()

        private fun getCookies(info: IRequestInfo) = info.parameters.asSequence()
            .filter { it.type == IParameter.PARAM_COOKIE }
            .map { Variable(it.name, it.value, Variable.Type.COOKIE) }
            .toList()

        private fun getCookies(json: JSONArray) = json.asSequence()
            .map { Variable(it as JSONObject, Variable.Type.COOKIE) }
            .toList()

        private fun getFormData(info: IRequestInfo) = info.parameters.asSequence()
            .filter { it.type == IParameter.PARAM_BODY }
            .map { Variable(it.name, it.value, Variable.Type.FORM_DATA) }
            .toList()
    }

    private object Keys {
        const val METHOD = "method"
        const val URL = "url"
        const val PROTOCOL_VERSION = "protocolVersion"
        const val HEADERS = "headers"
        const val QUERY_PARAMETERS = "queryParameters"
        const val COOKIES = "cookies"
        const val BODY = "body"
    }
}

private fun URL.trimQueryAndRef() = URI(protocol, authority, path, null, null).toURL()

private fun IRequestInfo.getRequestLine() = headers[0]
private fun IRequestInfo.getProtocolVersion() = getRequestLine().split(" ", limit = 3)[2]
