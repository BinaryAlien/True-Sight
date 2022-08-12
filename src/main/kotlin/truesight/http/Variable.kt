package truesight.http

import burp.ICookie
import org.json.JSONObject
import truesight.Extension
import truesight.Project

data class Variable(
    val name: String,
    val value: String,
    val type: Type,
    var notes: String? = null,
    private var localSubstitute: String? = null
) {
    val substitute: String?
        get() = localSubstitute ?: Project.substitutions.get(name, type)

    constructor(cookie: ICookie) : this(cookie.name, cookie.value, Type.COOKIE)

    constructor(json: JSONObject, type: Type) : this(
        name = json.getString(Keys.NAME),
        value = json.getString(Keys.VALUE),
        type = type,
        notes = json.optString(Keys.NOTES, null),
        localSubstitute = json.optString(Keys.LOCAL_SUBSTITUTE, null)
    )

    fun setLocalSubstitute(substitute: String?) {
        localSubstitute =
            if (substitute == null || substitute.isBlank() || substitute == name)
                null
            else
                substitute
    }

    fun setGlobalSubstitute(substitute: String?) = Project.substitutions.set(name, type, substitute)

    override fun equals(other: Any?) = other is Variable && this.name == other.name && this.value == other.value
    override fun hashCode(): Int {
        var hash = 7
        hash = 31 * hash + name.hashCode()
        hash = 31 * hash + value.hashCode()
        return hash
    }

    override fun toString() =
        when (type) {
            Type.HEADER -> "$name: $value"
            Type.QUERY_PARAMETER, Type.FORM_DATA -> name.urlEncode() + "=" + value.urlEncode()
            else -> "$name=$value"
        }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put(Keys.NAME, name)
        json.put(Keys.VALUE, value)
        json.putOpt(Keys.NOTES, notes)
        json.putOpt(Keys.LOCAL_SUBSTITUTE, localSubstitute)
        return json
    }

    enum class Type {
        HEADER, QUERY_PARAMETER, COOKIE, FORM_DATA
    }

    private object Keys {
        const val NAME = "name"
        const val VALUE = "value"
        const val NOTES = "notes"
        const val LOCAL_SUBSTITUTE = "substitute"
    }
}

private fun String.urlEncode(): String = Extension.helpers.urlEncode(this)
