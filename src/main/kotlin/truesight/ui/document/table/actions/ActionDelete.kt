package truesight.ui.document.table.actions

import truesight.ui.document.table.DocumentTable
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ActionDelete(private val table: DocumentTable) : AbstractAction("Delete") {
    override fun actionPerformed(event: ActionEvent) {
        table.model.deleteDocuments(table.selectedDocuments)
    }

    companion object {
        const val KEY = "delete"
    }
}
