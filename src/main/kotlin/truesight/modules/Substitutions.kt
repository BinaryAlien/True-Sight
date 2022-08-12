package truesight.modules

import org.json.JSONObject
import org.json.JSONTokener
import truesight.Extension
import truesight.Module
import truesight.Settings
import truesight.http.Variable
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class Substitutions : Module {
    val count: Int
        get() = headers.size + queryParameters.size + cookies.size + formData.size

    private val headers = mutableMapOf<String, String>()
    private val queryParameters = mutableMapOf<String, String>()
    private val cookies = mutableMapOf<String, String>()
    private val formData = mutableMapOf<String, String>()

    private var saveFile: File? = null

    fun get(name: String, type: Variable.Type): String? = getSubstitutions(type).get(name)

    fun set(name: String, type: Variable.Type, substitution: String?) {
        val substitutions = getSubstitutions(type)
        if (substitution == null || substitution.isBlank() || substitution == name)
            substitutions.remove(name)
        else
            substitutions[name] = substitution
        saveSubstitutions()
    }

    override fun loadFromDisk(projectDir: File) {
        saveFile = File(projectDir, "substitutions.json")
        if (!saveFile!!.exists())
            saveSubstitutions()
        reloadSubstitutions()
    }

    override fun loadTemporary() {
        unloadSubstitutions()
        saveFile = null
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put(Keys.HEADERS, headers)
        json.put(Keys.QUERY_PARAMETERS, queryParameters)
        json.put(Keys.COOKIES, cookies)
        json.put(Keys.FORM_DATA, formData)
        return json
    }

    private fun getSubstitutions(type: Variable.Type) =
        when (type) {
            Variable.Type.HEADER -> headers
            Variable.Type.QUERY_PARAMETER -> queryParameters
            Variable.Type.COOKIE -> cookies
            Variable.Type.FORM_DATA -> formData
        }

    private fun saveSubstitutions() {
        if (saveFile == null)
            return
        try {
            FileWriter(saveFile!!).use { toJson().write(it, Settings.indent.get(), 0) }
        } catch (ex: IOException) {
            // Can occur when the parent directory ceases to exist.
            ex.printStackTrace()
            Extension.logger.severe("Could not save substitutions: ${ex.message}")
        }
    }

    private fun reloadSubstitutions() {
        unloadSubstitutions()
        loadSubstitutions()
    }

    private fun unloadSubstitutions() {
        unloadSubstitutions(headers)
        unloadSubstitutions(queryParameters)
        unloadSubstitutions(cookies)
        unloadSubstitutions(formData)
    }

    private fun loadSubstitutions() {
        FileReader(saveFile!!).use {
            val json = JSONObject(JSONTokener(it))
            loadSubstitutions(headers, json.getJSONObject(Keys.HEADERS))
            loadSubstitutions(queryParameters, json.getJSONObject(Keys.QUERY_PARAMETERS))
            loadSubstitutions(cookies, json.getJSONObject(Keys.COOKIES))
            loadSubstitutions(formData, json.getJSONObject(Keys.FORM_DATA))
        }
    }

    private fun unloadSubstitutions(substitutions: MutableMap<String, String>) = substitutions.clear()

    private fun loadSubstitutions(dest: MutableMap<String, String>, src: JSONObject) {
        src.toMap().map { (name, substitution) -> Pair(name, substitution as String) }.toMap(dest)
    }

    private object Keys {
        const val HEADERS = "headers"
        const val QUERY_PARAMETERS = "queryParameters"
        const val COOKIES = "cookies"
        const val FORM_DATA = "formData"
    }
}
