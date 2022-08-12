package truesight.ui.modules.workflow.table.actions

import truesight.ui.modules.workflow.table.WorkflowTable
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ActionMoveDown(private val table: WorkflowTable) : AbstractAction("Move down") {
    override fun actionPerformed(event: ActionEvent) {
        var documentIndex = table.selectedRow
        documentIndex = table.model.moveDocumentDown(documentIndex)
        table.select(documentIndex)
    }

    companion object {
        const val KEY = "movedown"
    }
}
