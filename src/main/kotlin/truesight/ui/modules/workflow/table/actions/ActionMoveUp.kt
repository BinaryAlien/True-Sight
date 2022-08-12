package truesight.ui.modules.workflow.table.actions

import truesight.ui.modules.workflow.table.WorkflowTable
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ActionMoveUp(private val table: WorkflowTable) : AbstractAction("Move up") {
    override fun actionPerformed(event: ActionEvent) {
        var documentIndex = table.selectedRow
        documentIndex = table.model.moveDocumentUp(documentIndex)
        table.select(documentIndex)
    }

    companion object {
        const val KEY = "moveup"
    }
}
