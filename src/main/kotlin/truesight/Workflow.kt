package truesight

import org.json.JSONObject
import java.util.*

data class Workflow(
    val id: UUID,
    var name: String,
    val documents: MutableList<Document>,
    var description: String? = null
) {
    constructor(name: String) : this(
        id = UUID.randomUUID(),
        name = name,
        documents = mutableListOf()
    )

    constructor(json: JSONObject) : this(
        id = UUID.fromString(json.getString(Keys.ID)),
        name = json.getString(Keys.NAME),
        documents = json.getJSONArray(Keys.DOCUMENTS).asSequence()
            .map { UUID.fromString(it as String) }
            .mapNotNull { Project.catalog.getDocumentById(it) }
            .toMutableList(),
        description = json.optString(Keys.DESCRIPTION, null)
    )

    override fun equals(other: Any?) = other is Workflow && this.id == other.id
    override fun hashCode() = id.hashCode()

    override fun toString() = name
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put(Keys.ID, id)
        json.put(Keys.NAME, name)
        json.put(Keys.DOCUMENTS, documents.map { it.id.toString() })
        json.putOpt(Keys.DESCRIPTION, description)
        return json
    }

    private object Keys {
        const val ID = "id"
        const val NAME = "name"
        const val DOCUMENTS = "documents"
        const val DESCRIPTION = "description"
    }
}
