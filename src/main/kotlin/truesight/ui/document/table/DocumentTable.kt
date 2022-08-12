package truesight.ui.document.table

import truesight.Document
import truesight.ui.Table
import truesight.ui.document.editor.DocumentEditor
import truesight.ui.document.table.actions.ActionDelete
import truesight.ui.document.table.actions.ActionHighlight
import truesight.ui.document.table.actions.ActionOpen
import truesight.ui.document.table.actions.ActionRepeater
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.KeyStroke

open class DocumentTable(open val model: DocumentTableModel) : Table(model) {
    val selectedDocuments: List<Document>
        get() = model.getDocumentsAt(selectedRows.map { convertRowIndexToModel(it) })

    init {
        initActions()
        initKeyBindings()
        initMouseHandler()
        initTable()
    }

    fun select(rowIndex: Int) = setRowSelectionInterval(rowIndex, rowIndex)

    private fun initActions() {
        actionMap.put(ActionOpen.KEY, ActionOpen(this))
        actionMap.put(ActionDelete.KEY, ActionDelete(this))
        actionMap.put(ActionRepeater.KEY, ActionRepeater(this))
        actionMap.put(ActionHighlight.KEY, ActionHighlight(this))
    }

    private fun initKeyBindings() {
        val inputMap = getInputMap(WHEN_FOCUSED)
        /* Delete */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), ActionDelete.KEY)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), ActionDelete.KEY)
        /* Send to Repeater */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), ActionRepeater.KEY)
    }

    private fun initMouseHandler() {
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(event: MouseEvent) {
                if (!isDoubleLeftClick(event))
                    return
                val cell = cellAtPoint(event.point)
                if (cell != null && !model.isCellEditable(cell.first, cell.second)) {
                    val document = model.getDocumentAt(cell.first)
                    DocumentEditor.open(document)
                }
            }

            private fun isDoubleLeftClick(event: MouseEvent) = event.button == MouseEvent.BUTTON1 && event.clickCount == 2
        })
    }

    private fun initTable() {
        val renderer = DocumentTableCellRenderer()
        setDefaultRenderer(String::class.java, renderer)
        setDefaultRenderer(Integer::class.java, renderer)
    }
}
