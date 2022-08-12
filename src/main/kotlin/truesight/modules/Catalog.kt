package truesight.modules

import burp.IHttpRequestResponse
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import truesight.*
import truesight.Helper.pluralize
import truesight.ui.document.editor.DocumentEditor
import truesight.ui.document.table.DocumentTableModel
import java.io.*
import java.util.*

class Catalog : DocumentTableModel(), Module {
    val documentCount: Int
        get() = loadedDocuments.size

    private val loadedDocuments = mutableListOf<Document>()
    private var currentSaveDir: File? = null

    override fun getDocumentAt(rowIndex: Int) = loadedDocuments[rowIndex]
    fun getDocumentById(id: UUID) = loadedDocuments.find { it.id == id }

    fun append(messages: Array<IHttpRequestResponse>) = append(messages.map { Document(it) })

    @Synchronized
    fun append(documentsToAppend: List<Document>) {
        val newDocuments = documentsToAppend.filter { !hasDuplicate(it) }
        if (newDocuments.isNotEmpty()) {
            val firstRow = loadedDocuments.size
            loadedDocuments.addAll(newDocuments)
            newDocuments.forEach { saveDocument(it) }
            fireTableRowsInserted(firstRow, loadedDocuments.size - 1)
        }
        val duplicateCount = documentsToAppend.size - newDocuments.size
        if (duplicateCount > 0)
            Extension.logger.info("$duplicateCount " + pluralize("message", duplicateCount) +
                " rejected by the catalog due to identical contents.")
    }

    override fun deleteDocument(document: Document) = deleteDocumentAt(loadedDocuments.indexOf(document))

    @Synchronized
    override fun deleteDocumentAt(rowIndex: Int): Boolean {
        val document = getDocumentAt(rowIndex)
        DocumentEditor.close(document)
        val saveFile = getSaveFile(document)
        val success = saveFile == null || saveFile.delete()
        if (success) {
            Project.workflows.unassign(document)
            loadedDocuments.removeAt(rowIndex)
            fireTableRowsDeleted(rowIndex, rowIndex)
        } else {
            Extension.logger.warning("Could not delete document '$document' ${document.id}.")
        }
        return success
    }

    @Synchronized
    fun handleUpdate(updatedDocument: Document) {
        val documentIndex = loadedDocuments.indexOf(updatedDocument)
        saveDocument(updatedDocument)
        fireTableRowsUpdated(documentIndex, documentIndex)
    }

    override fun loadFromDisk(projectDir: File) {
        currentSaveDir = File(projectDir, "catalog")
        currentSaveDir!!.mkdir()
        reloadDocuments()
    }

    override fun loadTemporary() {
        unloadDocuments()
        currentSaveDir = null
    }

    private fun hasDuplicate(document: Document) = loadedDocuments.any { it.contentEquals(document) }

    private fun reloadDocuments() {
        unloadDocuments()
        loadDocuments()
    }

    @Synchronized
    private fun unloadDocuments() {
        if (loadedDocuments.isNotEmpty()) {
            DocumentEditor.closeAll()
            val lastDocumentIndex = loadedDocuments.size - 1
            loadedDocuments.clear()
            fireTableRowsDeleted(0, lastDocumentIndex)
        }
    }

    @Synchronized
    private fun loadDocuments() {
        currentSaveDir!!.listFiles(fileFilter)!!.forEach { loadDocument(it) }
        if (loadedDocuments.isNotEmpty())
            fireTableRowsInserted(0, loadedDocuments.size - 1)
    }

    private fun loadDocument(source: File) {
        try {
            FileReader(source).use {
                val json = JSONObject(JSONTokener(it))
                loadedDocuments.add(Document(json))
            }
        } catch (ex: JSONException) {
            ex.printStackTrace()
            if (source.delete())
                Extension.logger.info("Invalid document file '${source.name}' was automatically deleted.")
            else
                Extension.logger.warning("Invalid document file '${source.name}' could not be deleted.")
        }
    }

    private fun saveDocument(document: Document) {
        val saveFile = getSaveFile(document) ?: return
        try {
            FileWriter(saveFile).use { document.toJson().write(it, Settings.indent.get(), 0) }
        } catch (ex: IOException) {
            // Can occur when the parent directory ceases to exist.
            ex.printStackTrace()
            Extension.logger.severe("Could not save document ${document.id}: ${ex.message}")
        }
    }

    private fun getSaveFile(document: Document) =
        if (currentSaveDir == null)
            null
        else
            File(currentSaveDir, "${document.id}.json")

    override fun getRowCount() = loadedDocuments.size
    override fun getColumnCount() = columnNames.size
    override fun getColumnName(columnIndex: Int) = columnNames[columnIndex]
    override fun getColumnClass(columnIndex: Int) =
        when (columnIndex) {
            Column.STATUS.index -> Integer::class.java
            else -> String::class.java
        }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        val document = getDocumentAt(rowIndex)
        return when (columnIndex) {
            Column.HOST.index -> document.request.url.host
            Column.METHOD.index -> document.request.method.name
            Column.PATH.index -> document.request.url.path + document.request.queryString
            Column.STATUS.index -> document.response.statusCode
            Column.TITLE.index -> document.title
            Column.WORKFLOW.index -> document.workflow
            else -> null
        }
    }

    override fun setValueAt(value: Any, rowIndex: Int, columnIndex: Int) {
        if (value !is String)
            return
        val document = getDocumentAt(rowIndex)
        when (columnIndex) {
            Column.TITLE.index -> document.title = value.trim().ifBlank { null }
            Column.WORKFLOW.index -> document.workflow = value.trim().ifBlank { null }
            else -> return
        }
        saveDocument(document)
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int) =
        columnIndex == Column.TITLE.index || columnIndex == Column.WORKFLOW.index

    companion object {
        val columnNames = Column.values().sortedBy { it.index }.map { it.header }
        val fileFilter = FileFilter { it.extension == "json" }
    }

    enum class Column(val header: String, val index: Int) {
        HOST("Host", 0),
        METHOD("Method", 1),
        PATH("URL", 2),
        STATUS("Status", 3),
        TITLE("Title", 4),
        WORKFLOW("Workflow", 5)
    }
}
