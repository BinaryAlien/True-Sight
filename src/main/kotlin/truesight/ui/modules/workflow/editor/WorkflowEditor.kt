package truesight.ui.modules.workflow.editor

import truesight.Project
import truesight.Workflow
import truesight.ui.Editor
import truesight.ui.document.editor.DocumentEditor
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

class WorkflowEditor private constructor(private val workflow: Workflow) : Editor(title = workflow.toString()) {
    private val editorPane = WorkflowEditorPane(workflow)

    init {
        openEditors.add(this)
        contentPane = editorPane
        addWindowListener(object : WindowAdapter() {
            override fun windowClosed(event: WindowEvent) {
                openEditors.remove(this@WorkflowEditor)
            }
        })
    }

    override fun saveChanges() {
        editorPane.saveChanges()
        Project.workflows.handleUpdate(workflow)
    }

    companion object {
        private val openEditors = mutableSetOf<WorkflowEditor>()

        fun open(workflow: Workflow) {
            var editor = find(workflow)
            if (editor == null) {
                editor = WorkflowEditor(workflow)
                editor.pack()
                editor.isVisible = true
            } else {
                editor.requestFocus()
            }
        }

        fun close(workflow: Workflow) {
            val editor = find(workflow)
            editor?.close()
        }

        fun closeAll() = openEditors.forEach { it.close() }

        fun forceSaveAll() = openEditors.forEach { it.saveChanges() }
        fun forceCloseAll() = openEditors.forEach { it.dispose() }

        private fun find(workflow: Workflow) = openEditors.find { it.workflow == workflow }
    }
}
