package truesight.ui.modules.workflow.table.actions

import truesight.ui.modules.workflow.table.WorkflowTable
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ActionRemove(private val table: WorkflowTable) : AbstractAction("Remove") {
    override fun actionPerformed(event: ActionEvent) {
        table.selectedDocuments.forEach { it.workflow = null }
    }

    companion object {
        const val KEY = "remove"
    }
}
