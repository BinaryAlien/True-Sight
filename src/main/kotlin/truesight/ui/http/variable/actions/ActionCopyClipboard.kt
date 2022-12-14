package truesight.ui.http.variable.actions

import truesight.ui.http.variable.VariableTable
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ActionCopyClipboard(private val table: VariableTable) : AbstractAction("Copy to clipboard") {
    override fun actionPerformed(event: ActionEvent) {
        val contents = StringSelection(table.model.createClipboardCopy(table.selectedVariables))
        clipboard.setContents(contents, null)
    }

    companion object {
        const val KEY = "copy"

        private val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    }
}
