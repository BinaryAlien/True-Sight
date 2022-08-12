package truesight.ui.settings

import truesight.Extension
import truesight.Settings
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.*

class SettingsPane : JPanel() {
    private val directoryField = JTextField()
    private val directoryBrowse = JButton()
    private val directoryTemporary = JButton()

    private val indentField = JSpinner(
        SpinnerNumberModel(Settings.indent.get(), INDENT_MINIMUM, INDENT_MAXIMUM, 1)
    )

    init {
        initFields()
        initPane()
    }

    private fun initFields() {
        /* Directory field */
        directoryField.isEditable = false
        directoryField.text = getDirectoryText(Settings.projectDir.get())
        Settings.projectDir.bind { directoryField.text = getDirectoryText(it) }

        /* Directory browse */
        directoryBrowse.action = object : AbstractAction("Browse") {
            override fun actionPerformed(event: ActionEvent) {
                val chooser = JFileChooser(Settings.projectDir.get()?.parentFile)
                chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                val option = chooser.showOpenDialog(Extension.owningWindow)
                if (option == JFileChooser.APPROVE_OPTION)
                    Settings.projectDir.set(chooser.selectedFile)
            }
        }

        /* Directory temporary */
        directoryTemporary.action = object : AbstractAction("Temporary") {
            override fun actionPerformed(event: ActionEvent) {
                Settings.projectDir.set(null)
            }
        }

        /* Indent field */
        indentField.addChangeListener { Settings.indent.set(indentField.value as Int) }
    }

    private fun initPane() {
        border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        layout = createLayout()
    }

    private fun createLayout(): GroupLayout {
        val layout = GroupLayout(this)
        layout.autoCreateGaps = true

        val directoryLabel = JLabel("Project")
        val indentLabel = JLabel("Files indentation")

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                    .addComponent(directoryLabel)
                    .addComponent(indentLabel))
                .addGroup(layout.createParallelGroup()
                    .addComponent(directoryField)
                    .addComponent(indentField))
                .addGroup(layout.createParallelGroup()
                    .addComponent(directoryBrowse))
                .addGroup(layout.createParallelGroup()
                    .addComponent(directoryTemporary)))

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(directoryLabel)
                    .addComponent(directoryField)
                    .addComponent(directoryBrowse)
                    .addComponent(directoryTemporary))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                    .addComponent(indentLabel)
                    .addComponent(indentField)))

        return layout
    }

    private fun getDirectoryText(dir: File?) = dir?.canonicalPath ?: "<temporary project>"

    companion object {
        const val INDENT_MINIMUM = 0
        const val INDENT_MAXIMUM = 8
    }
}
