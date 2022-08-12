package truesight.ui.http.variable

import truesight.Helper
import truesight.http.Variable
import truesight.ui.Table
import truesight.ui.http.variable.actions.ActionCopyClipboard
import truesight.ui.http.variable.actions.ActionGlobalSubstitution
import javax.swing.ListSelectionModel

class VariableTable(variables: List<Variable>, type: Variable.Type) : Table() {
    val selectedVariables: List<Variable>
        get() = model.getVariablesAt(selectedRows.map { convertRowIndexToModel(it) })

    val model = VariableTableModel(variables, type)

    init {
        initActions()
        initTable()
    }

    override fun getTooltipText(rowIndex: Int, columnIndex: Int): String? {
        val variable = model.getVariableAt(rowIndex)
        return when (columnIndex) {
            VariableTableModel.Column.NAME.index -> getNameTooltipText(variable)
            VariableTableModel.Column.VALUE.index -> variable.value
            VariableTableModel.Column.NOTES.index -> variable.notes
            else -> null
        }
    }

    private fun initActions() {
        actionMap.put(ActionCopyClipboard.KEY, ActionCopyClipboard(this))
        actionMap.put(ActionGlobalSubstitution.KEY, ActionGlobalSubstitution(this))
    }

    private fun initTable() {
        autoCreateRowSorter = true
        componentPopupMenu = Helper.createPopupMenuFrom(actionMap)
        selectionModel.selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        setModel(model)
    }

    private fun getNameTooltipText(variable: Variable) =
        if (variable.substitute != null)
            variable.substitute + " (" + variable.name + ")"
        else
            variable.name
}
