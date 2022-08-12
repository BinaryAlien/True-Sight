package truesight.ui.modules.workflow.table

import truesight.Helper
import truesight.modules.Catalog
import truesight.ui.document.table.DocumentTable
import truesight.ui.modules.workflow.table.actions.ActionMoveDown
import truesight.ui.modules.workflow.table.actions.ActionMoveUp
import truesight.ui.modules.workflow.table.actions.ActionRemove
import java.awt.event.KeyEvent
import javax.swing.KeyStroke
import javax.swing.ListSelectionModel

class WorkflowTable(override val model: WorkflowTableModel) : DocumentTable(model) {
    init {
        initActions()
        initKeyBindings()
        initTable()
    }

    override fun getTooltipText(rowIndex: Int, columnIndex: Int): String? {
        val document = model.getDocumentAt(rowIndex)
        return when (columnIndex) {
            WorkflowTableModel.Column.HOST.index -> document.request.url.host
            WorkflowTableModel.Column.METHOD.index -> document.request.method.description
            WorkflowTableModel.Column.PATH.index -> document.request.url.path + document.request.queryString
            WorkflowTableModel.Column.STATUS.index -> document.response.statusText
            WorkflowTableModel.Column.TITLE.index -> document.title
            else -> null
        }
    }

    private fun initActions() {
        actionMap.put(ActionMoveUp.KEY, ActionMoveUp(this))
        actionMap.put(ActionMoveDown.KEY, ActionMoveDown(this))
        actionMap.put(ActionRemove.KEY, ActionRemove(this))
    }

    private fun initKeyBindings() {
        val inputMap = getInputMap(WHEN_FOCUSED)
        /* Move up */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK), ActionMoveUp.KEY)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), ActionMoveUp.KEY)
        /* Move down */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK), ActionMoveDown.KEY)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), ActionMoveDown.KEY)
    }

    private fun initTable() {
        autoCreateRowSorter = false
        componentPopupMenu = Helper.createPopupMenuFrom(actionMap)
        selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
        initColumns()
    }

    private fun initColumns() {
        setColumnWidth(Catalog.Column.METHOD.index, 50)
        setColumnWidth(Catalog.Column.STATUS.index, 47)
    }
}
