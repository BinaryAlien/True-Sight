package truesight.ui.document.table.actions

import truesight.Extension
import truesight.Highlights
import truesight.Project
import truesight.ui.document.table.DocumentTable
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JColorChooser

class ActionHighlight(private val table: DocumentTable) : AbstractAction("Highlight") {
    override fun actionPerformed(event: ActionEvent) {
        val documents = table.selectedDocuments
        if (documents.isEmpty())
            return
        val newHighlight = JColorChooser.showDialog(Extension.owningWindow, "Choose a highlight", Highlights.none)
        if (newHighlight != null) {
            documents.forEach {
                it.highlight = newHighlight
                Project.catalog.handleUpdate(it)
            }
        }
    }

    companion object {
        const val KEY = "highlight"
    }
}
