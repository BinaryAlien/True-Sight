package truesight.ui.modules.workflow.table

import truesight.Document
import truesight.Project
import truesight.Workflow
import truesight.ui.document.table.DocumentTableModel

class WorkflowTableModel(private val workflow: Workflow) : DocumentTableModel() {
    override fun getDocumentAt(rowIndex: Int) = workflow.documents[rowIndex]

    override fun deleteDocument(document: Document) = deleteDocumentAt(workflow.documents.indexOf(document))

    @Synchronized
    override fun deleteDocumentAt(rowIndex: Int): Boolean {
        val document = getDocumentAt(rowIndex)
        val success = Project.catalog.deleteDocument(document)
        if (success)
            fireTableRowsDeleted(rowIndex, rowIndex)
        return success
    }

    fun moveDocumentUp(documentIndex: Int): Int {
        val newDocumentIndex = if (documentIndex > 0) {
            swapConsecutiveDocuments(documentIndex - 1, documentIndex)
            documentIndex - 1
        } else {
            documentIndex
        }
        return newDocumentIndex
    }

    fun moveDocumentDown(documentIndex: Int): Int {
        val newDocumentIndex = if (documentIndex + 1 < workflow.documents.size) {
            swapConsecutiveDocuments(documentIndex, documentIndex + 1)
            documentIndex + 1
        } else {
            documentIndex
        }
        return newDocumentIndex
    }

    override fun getRowCount() = workflow.documents.size
    override fun getColumnCount() = columnNames.size
    override fun getColumnName(columnIndex: Int) = columnNames[columnIndex]
    override fun getColumnClass(columnIndex: Int) =
        when (columnIndex) {
            Column.STATUS.index -> Integer::class.java
            else -> String::class.java
        }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        val document = workflow.documents[rowIndex]
        return when (columnIndex) {
            Column.HOST.index -> document.request.url.host
            Column.METHOD.index -> document.request.method.name
            Column.PATH.index -> document.request.url.path + document.request.queryString
            Column.STATUS.index -> document.response.statusCode
            Column.TITLE.index -> document.title
            else -> null
        }
    }

    override fun setValueAt(value: Any, rowIndex: Int, columnIndex: Int) {
        if (value !is String)
            return
        val document = workflow.documents[rowIndex]
        when (columnIndex) {
            Column.TITLE.index -> document.title = value.trim().ifBlank { null }
        }
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int) = columnIndex == Column.TITLE.index

    @Synchronized
    private fun swapConsecutiveDocuments(first: Int, second: Int) {
        val copyOfFirst = workflow.documents[first]
        workflow.documents[first] = workflow.documents[second]
        workflow.documents[second] = copyOfFirst
        fireTableRowsUpdated(first, second)
    }

    companion object {
        val columnNames = Column.values().sortedBy { it.index }.map { it.header }
    }

    enum class Column(val header: String, val index: Int) {
        HOST("Host", 0),
        METHOD("Method", 1),
        PATH("URL", 2),
        STATUS("Status", 3),
        TITLE("Title", 4)
    }
}
