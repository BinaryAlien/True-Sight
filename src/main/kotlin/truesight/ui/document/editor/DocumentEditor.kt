package truesight.ui.document.editor

import truesight.Document
import truesight.Project
import truesight.ui.Editor
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

class DocumentEditor private constructor(private val document: Document) : Editor(title = document.toString()) {
    private val editorPane = DocumentEditorPane(document)

    init {
        openEditors.add(this)
        contentPane = editorPane
        pack()
        centerLocationRelativeToParent()
        addWindowListener(object : WindowAdapter() {
            override fun windowClosed(event: WindowEvent) {
                openEditors.remove(this@DocumentEditor)
            }
        })
    }

    override fun saveChanges() {
        editorPane.saveChanges()
        Project.catalog.handleUpdate(document)
    }

    companion object {
        private val openEditors = mutableSetOf<DocumentEditor>()

        fun open(document: Document) {
            var editor = find(document)
            if (editor == null) {
                editor = DocumentEditor(document)
                editor.isVisible = true
            } else {
                editor.requestFocus()
            }
        }

        fun close(document: Document) {
            val editor = find(document)
            editor?.close()
        }

        fun closeAll() = openEditors.forEach { it.close() }

        fun forceSaveAll() = openEditors.forEach { it.saveChanges() }
        fun forceCloseAll() = openEditors.forEach { it.dispose() }

        private fun find(document: Document) = openEditors.find { it.document == document }
    }
}
