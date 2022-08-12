package truesight.http.response

import burp.IResponseInfo
import org.json.JSONArray
import org.json.JSONObject
import truesight.Extension
import truesight.http.Variable
import truesight.http.body.Body
import truesight.http.body.Generic

data class Response(
    val protocolVersion: String,
    val statusCode: Int,
    val headers: List<Variable>,
    val cookies: List<Variable>,
    val body: Body
) {
    val statusText = Status.getText(statusCode)

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put(Keys.PROTOCOL_VERSION, protocolVersion)
        json.put(Keys.STATUS_CODE, statusCode)
        json.put(Keys.HEADERS, headers.map { it.toJson() })
        json.put(Keys.COOKIES, cookies.map { it.toJson() })
        json.put(Keys.BODY, body.toJson())
        return json
    }

    companion object {
        fun from(bytes: ByteArray): Response {
            val info = Extension.helpers.analyzeResponse(bytes)
            return Response(
                protocolVersion = info.getProtocolVersion(),
                statusCode = info.statusCode.toInt(),
                headers = getHeaders(info),
                cookies = getCookies(info),
                body = Generic(bytes.copyOfRange(info.bodyOffset, bytes.size))
            )
        }

        fun from(json: JSONObject) = Response(
            protocolVersion = json.getString(Keys.PROTOCOL_VERSION),
            statusCode = json.getInt(Keys.STATUS_CODE),
            headers = getHeaders(json.getJSONArray(Keys.HEADERS)),
            cookies = getCookies(json.getJSONArray(Keys.COOKIES)),
            body = Generic(json.getString(Keys.BODY))
        )

        private fun getHeaders(info: IResponseInfo) = info.headers.asSequence()
            .drop(1) // The first item is always the status-line: discard it.
            .map { it.split(": ", limit = 2) }
            .filter { it[0].lowercase() != "cookie" } // Discard the 'Cookie' header: it is stored in 'cookies'.
            .map { Variable(name = it[0], value = it[1], Variable.Type.HEADER) }
            .toList()

        private fun getHeaders(json: JSONArray) = json.asSequence()
            .map { Variable(it as JSONObject, Variable.Type.HEADER) }
            .toList()

        private fun getCookies(info: IResponseInfo) = info.cookies.asSequence()
            .map { Variable(it) }
            .toList()

        private fun getCookies(json: JSONArray) = json.asSequence()
            .map { Variable(it as JSONObject, Variable.Type.COOKIE ) }
            .toList()
    }

    private object Keys {
        const val PROTOCOL_VERSION = "protocolVersion"
        const val STATUS_CODE = "statusCode"
        const val HEADERS = "headers"
        const val COOKIES = "cookies"
        const val BODY = "body"
    }
}

private fun IResponseInfo.getStatusLine() = headers[0]
private fun IResponseInfo.getProtocolVersion() = getStatusLine().split(" ", limit = 2)[0]
