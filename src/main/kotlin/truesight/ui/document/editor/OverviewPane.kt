package truesight.ui.document.editor

import truesight.Document
import java.awt.event.ActionEvent
import javax.swing.*

class OverviewPane(private val document: Document) : JPanel() {
    private val titleField = JTextField(FIELD_COLUMNS)
    private val workflowField = JTextField(FIELD_COLUMNS)
    private val hostField = JTextField(FIELD_COLUMNS)
    private val pathField = JTextField(FIELD_COLUMNS)
    private val methodField = JTextField(FIELD_COLUMNS)
    private val statusField = JTextField(FIELD_COLUMNS)
    private val protocolField = JTextField(FIELD_COLUMNS)
    private val notesField = JTextArea(NOTES_ROWS, FIELD_COLUMNS)
    private val highlightButton = JButton()

    init {
        initPane()
        initFields()
    }

    fun saveChanges() {
        document.title = titleField.text.trim().ifBlank { null }
        document.notes = notesField.text.ifBlank { null }
        document.workflow = workflowField.text.trim().ifBlank { null }
    }

    private fun initPane() {
        border = BorderFactory.createTitledBorder("Overview")
        layout = createLayout()
    }

    private fun initFields() {
        /* Title */
        titleField.isEditable = true
        titleField.text = document.title

        /* Workflow */
        workflowField.isEditable = true
        workflowField.text = document.workflow

        /* Host */
        hostField.isEditable = false
        hostField.text = document.request.url.host

        /* Method */
        methodField.isEditable = false
        methodField.text = document.request.method.name
        methodField.toolTipText = document.request.method.description

        /* Path */
        pathField.isEditable = false
        pathField.text = document.request.url.path
        pathField.toolTipText = document.request.url.path + document.request.queryString

        /* Status */
        statusField.isEditable = false
        statusField.text = document.response.statusText

        /* Protocol */
        protocolField.isEditable = false
        protocolField.text = document.response.protocolVersion

        /* Notes */
        notesField.isEditable = true
        notesField.lineWrap = true
        notesField.text = document.notes

        /* Highlight */
        highlightButton.action = object : AbstractAction() {
            override fun actionPerformed(event: ActionEvent) {
                val newHighlight = JColorChooser.showDialog(topLevelAncestor, "Choose a highlight", document.highlight)
                if (newHighlight != null)
                    document.highlight = newHighlight
            }
        }
        highlightButton.text = "Choose"
    }

    private fun createLayout(): GroupLayout {
        val layout = GroupLayout(this)
        layout.autoCreateGaps = true

        val notesScrollPane = JScrollPane(notesField)

        val titleLabel = JLabel("Title")
        val workflowLabel = JLabel("Workflow")
        val hostLabel = JLabel("Host")
        val methodLabel = JLabel("Method")
        val pathLabel = JLabel("URL")
        val statusLabel = JLabel("Status")
        val protocolLabel = JLabel("Protocol")
        val notesLabel = JLabel("Notes")
        val highlightLabel = JLabel("Highlight")

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                    .addComponent(titleLabel)
                    .addComponent(workflowLabel)
                    .addComponent(hostLabel)
                    .addComponent(methodLabel)
                    .addComponent(pathLabel)
                    .addComponent(statusLabel)
                    .addComponent(protocolLabel)
                    .addComponent(notesLabel)
                    .addComponent(highlightLabel))
                .addGroup(layout.createParallelGroup()
                    .addComponent(titleField)
                    .addComponent(workflowField)
                    .addComponent(hostField)
                    .addComponent(methodField)
                    .addComponent(pathField)
                    .addComponent(statusField)
                    .addComponent(protocolField)
                    .addComponent(notesScrollPane)
                    .addComponent(highlightButton)))

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(titleLabel)
                    .addComponent(titleField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(workflowLabel)
                    .addComponent(workflowField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(hostLabel)
                    .addComponent(hostField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(methodLabel)
                    .addComponent(methodField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(pathLabel)
                    .addComponent(pathField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(statusLabel)
                    .addComponent(statusField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(protocolLabel)
                    .addComponent(protocolField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(notesLabel)
                    .addComponent(notesScrollPane))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(highlightLabel)
                    .addComponent(highlightButton)))

        return layout
    }

    companion object {
        private const val FIELD_COLUMNS = 80
        private const val NOTES_ROWS = 6
    }
}
