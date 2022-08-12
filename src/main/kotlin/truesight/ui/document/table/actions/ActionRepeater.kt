package truesight.ui.document.table.actions

import truesight.ui.document.table.DocumentTable
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ActionRepeater(private val table: DocumentTable) : AbstractAction("Send to Repeater") {
    override fun actionPerformed(event: ActionEvent) {
        table.selectedDocuments.forEach { it.sendToRepeater() }
    }

    companion object {
        const val KEY = "repeater"
    }
}
