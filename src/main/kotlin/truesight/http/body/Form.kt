package truesight.http.body

import org.json.JSONArray
import org.json.JSONObject
import truesight.http.Variable

/** `x-www-form-urlencoded` request body. */
class Form(val fields: List<Variable>) : Body() {
    constructor(json: JSONArray) : this(json.asSequence()
        .map { Variable(it as JSONObject, Variable.Type.FORM_DATA) }
        .toMutableList()
    )

    override fun isEmpty() = fields.isEmpty()
    override fun isNotEmpty() = !isEmpty()

    override fun equals(other: Any?) = other is Form && this.fields == other.fields
    override fun hashCode() = fields.hashCode()

    override fun toBytes() = fields.joinToString("&").toByteArray()
    override fun toJson() = JSONArray(fields.map { it.toJson() })
}
