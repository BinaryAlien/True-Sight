package truesight.ui.modules.catalog.actions

import truesight.Document
import truesight.Extension
import truesight.Project
import truesight.ui.document.table.DocumentTable
import truesight.ui.modules.workflow.editor.WorkflowEditor
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JOptionPane

class ActionWorkflow(private val table: DocumentTable) : AbstractAction("Workflow") {
    override fun actionPerformed(event: ActionEvent) {
        val documents = table.selectedDocuments
        if (documents.size > 1)
            setWorkflow(documents)
        else if (documents.size == 1)
            openWorkflow(documents[0])
    }

    private fun openWorkflow(document: Document) {
        val workflow = Project.workflows.which(document) ?: return
        WorkflowEditor.open(workflow)
    }

    private fun setWorkflow(documents: List<Document>) {
        val workflow = JOptionPane.showInputDialog(
            Extension.owningWindow, "Enter workflow to set", "Workflow definition", JOptionPane.PLAIN_MESSAGE)
        if (workflow != null)
            documents.forEach { it.workflow = workflow }
    }

    companion object {
        const val KEY = "workflow"
    }
}
