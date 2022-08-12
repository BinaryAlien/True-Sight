package truesight.ui.document.table.actions

import truesight.ui.document.editor.DocumentEditor
import truesight.ui.document.table.DocumentTable
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ActionOpen(private val table: DocumentTable) : AbstractAction("Open") {
    override fun actionPerformed(event: ActionEvent) {
        table.selectedDocuments.forEach { DocumentEditor.open(it) }
    }

    companion object {
        const val KEY = "open"
    }
}
