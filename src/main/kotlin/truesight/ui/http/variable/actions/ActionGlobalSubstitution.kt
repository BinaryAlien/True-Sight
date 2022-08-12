package truesight.ui.http.variable.actions

import truesight.Extension
import truesight.ui.http.variable.VariableTable
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JOptionPane

class ActionGlobalSubstitution(private val table: VariableTable) : AbstractAction("Apply global substitution") {
    override fun actionPerformed(event: ActionEvent) {
        var substitute = JOptionPane.showInputDialog(
            Extension.owningWindow, "Enter global substitute to set", "Global substitution", JOptionPane.PLAIN_MESSAGE)
        if (substitute != null) {
            substitute = substitute.trim().ifBlank { null }
            table.selectedVariables.forEach { it.setGlobalSubstitute(substitute) }
        }
    }

    companion object {
        const val KEY = "globalsub"
    }
}
