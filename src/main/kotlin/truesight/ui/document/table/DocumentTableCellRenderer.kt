package truesight.ui.document.table

import truesight.Document
import truesight.Highlights
import java.awt.Color
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer

class DocumentTableCellRenderer : DefaultTableCellRenderer() {
    override fun getTableCellRendererComponent(table: JTable, value: Any?, isSelected: Boolean, hasFocus: Boolean, rowIndex: Int, columnIndex: Int): Component {
        val component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, columnIndex)
        val model = table.model as DocumentTableModel
        val documentIndex = table.convertRowIndexToModel(rowIndex)
        val document = model.getDocumentAt(documentIndex)
        component.background = getHighlight(document, isSelected)
        component.foreground = if (component.background.isBright()) Color.BLACK else Color.WHITE
        return component
    }

    companion object {
        private fun getHighlight(document: Document, selected: Boolean): Color =
            if (selected)
                getSelectionHighlight(document)
            else
                document.highlight

        private fun getSelectionHighlight(document: Document): Color =
            if (document.highlight == Highlights.none)
                Highlights.selection
            else
                document.highlight.darker()
    }
}

private fun Color.isBright() = grayscale() > 186.0
private fun Color.grayscale() = red * 0.299 + green * 0.587 + blue * 0.114
