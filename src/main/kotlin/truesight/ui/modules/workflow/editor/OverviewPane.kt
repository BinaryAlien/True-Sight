package truesight.ui.modules.workflow.editor

import truesight.Extension
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
        saveName()
        saveDescription()
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

    private fun saveName() {
        val newName = nameField.text.trim()
        if (newName.isBlank())
            return
        else if (newName.equals(workflow.name, ignoreCase = true) || !Project.workflows.exists(newName))
            workflow.name = newName
        else
            JOptionPane.showMessageDialog(Extension.owningWindow, "Cannot rename a workflow with an existing name.",
                "Name collision", JOptionPane.ERROR_MESSAGE)
    }

    private fun saveDescription() {
        workflow.description = descriptionField.text.ifBlank { null }
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
