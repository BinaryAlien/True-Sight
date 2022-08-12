package truesight.ui.document.table

import truesight.Document
import javax.swing.table.AbstractTableModel

abstract class DocumentTableModel : AbstractTableModel() {
    fun getDocumentsAt(rowIndexes: List<Int>) = rowIndexes.map { getDocumentAt(it) }
    abstract fun getDocumentAt(rowIndex: Int): Document

    fun deleteDocuments(documents: List<Document>) = documents.forEach { deleteDocument(it) }
    abstract fun deleteDocument(document: Document): Boolean
    abstract fun deleteDocumentAt(rowIndex: Int): Boolean
}
