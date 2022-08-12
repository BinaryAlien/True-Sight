package truesight.ui.modules.workflow.editor

import truesight.Workflow
import truesight.ui.modules.workflow.table.WorkflowPane
import javax.swing.JSplitPane

class WorkflowEditorPane(workflow: Workflow) : JSplitPane(VERTICAL_SPLIT) {
    private val overviewPane = OverviewPane(workflow)

    init {
        topComponent = overviewPane
        bottomComponent = WorkflowPane(workflow)
    }

    fun saveChanges() {
        overviewPane.saveChanges()
    }
}
