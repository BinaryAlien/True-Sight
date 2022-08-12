package truesight.ui.modules.workflow.editor

import truesight.Project
import truesight.Workflow
import javax.swing.*

class OverviewPane(private val workflow: Workflow) : JPanel() {
    private val nameField = JTextField(FIELD_COLUMNS)
    private val descriptionField = JTextArea(DESCRIPTION_ROWS, FIELD_COLUMNS)

    init {
        initPane()
        initFields()
    }

    fun saveChanges() {
        val newWorkflowName = nameField.text.trim()
        if (newWorkflowName.isNotBlank() && !Project.workflows.exists(newWorkflowName))
            workflow.name = newWorkflowName
        workflow.description = descriptionField.text.ifBlank { null }
    }

    private fun initPane() {
        border = BorderFactory.createTitledBorder("Overview")
        layout = createLayout()
    }

    private fun initFields() {
        /* Title */
        nameField.isEditable = true
        nameField.text = workflow.name

        /* Workflow */
        descriptionField.isEditable = true
        descriptionField.text = workflow.description
    }

    private fun createLayout(): GroupLayout {
        val layout = GroupLayout(this)
        layout.autoCreateGaps = true

        val nameLabel = JLabel("Name")
        val descriptionLabel = JLabel("Description")

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                    .addComponent(nameLabel)
                    .addComponent(descriptionLabel))
                .addGroup(layout.createParallelGroup()
                    .addComponent(nameField)
                    .addComponent(descriptionField)))

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(nameLabel)
                    .addComponent(nameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(descriptionLabel)
                    .addComponent(descriptionField)))

        return layout
    }

    companion object {
        private const val FIELD_COLUMNS = 80
        private const val DESCRIPTION_ROWS = 6
    }
}
