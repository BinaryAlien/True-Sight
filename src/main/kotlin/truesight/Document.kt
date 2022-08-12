package truesight

import burp.IHttpRequestResponse
import org.json.JSONObject
import truesight.http.request.Request
import truesight.http.response.Response
import java.awt.Color
import java.util.*

data class Document(
    val id: UUID,
    var title: String? = null,
    var notes: String? = null,
    var highlight: Color = Highlights.none,
    val request: Request,
    val response: Response
) {
    var workflow: String?
        get() = Project.workflows.which(this)?.name
        set(value) = Project.workflows.reassign(this, value)

    constructor(message: IHttpRequestResponse) : this(
        id = UUID.randomUUID(),
        title = message.comment?.trim()?.ifBlank { null },
        highlight = Highlights.get(message.highlight),
        request = Request.from(message.httpService, message.request),
        response = Response.from(message.response)
    )

    constructor(json: JSONObject) : this(
        id = UUID.fromString(json.getString(Keys.ID)),
        title = json.optString(Keys.TITLE, null),
        notes = json.optString(Keys.NOTES, null),
        highlight = Color.decode(json.getString(Keys.HIGHLIGHT)),
        request = Request.from(json.getJSONObject(Keys.REQUEST)),
        response = Response.from(json.getJSONObject(Keys.RESPONSE))
    )

    fun sendToRepeater() = Extension.callbacks.sendToRepeater(
        request.httpService.host,
        request.httpService.port,
        request.httpService.protocol == "https",
        request.toBytes(),
        title
    )

    fun contentEquals(other: Document) = this.request == other.request && this.response == other.response
    override fun equals(other: Any?) = other is Document && this.id == other.id
    override fun hashCode() = id.hashCode()

    override fun toString() = title ?: request.url.toString()
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put(Keys.ID, id)
        json.putOpt(Keys.TITLE, title)
        json.putOpt(Keys.NOTES, notes)
        json.put(Keys.HIGHLIGHT, highlight.toHexString())
        json.put(Keys.REQUEST, request.toJson())
        json.put(Keys.RESPONSE, response.toJson())
        return json
    }

    private object Keys {
        const val ID = "id"
        const val TITLE = "title"
        const val NOTES = "notes"
        const val HIGHLIGHT = "highlight"
        const val REQUEST = "request"
        const val RESPONSE = "response"
    }
}

private fun Color.toHexString() = "#" +
    red.toString(16).padStart(2, '0') +
    green.toString(16).padStart(2, '0') +
    blue.toString(16).padStart(2, '0')
