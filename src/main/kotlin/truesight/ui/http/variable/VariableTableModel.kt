package truesight.ui.http.variable

import truesight.http.Variable
import javax.swing.table.AbstractTableModel

class VariableTableModel(private var variables: List<Variable>, private val type: Variable.Type)
    : AbstractTableModel() {
    fun getVariableAt(rowIndex: Int) = variables[rowIndex]
    fun getVariablesAt(rowIndexes: List<Int>) = rowIndexes.map { getVariableAt(it) }

    fun createClipboardCopy(variables: List<Variable>) = variables.joinToString(type.separator)

    override fun getRowCount() = variables.size
    override fun getColumnCount() = columnNames.size
    override fun getColumnName(columnIndex: Int) = columnNames[columnIndex]
    override fun getColumnClass(columnIndex: Int) = String::class.java

    override fun getValueAt(rowIndex: Int, columnIndex: Int): String? {
        val variable = getVariableAt(rowIndex)
        return when (columnIndex) {
            Column.NAME.index -> variable.substitute ?: variable.name
            Column.VALUE.index -> variable.value
            Column.NOTES.index -> variable.notes
            else -> null
        }
    }

    override fun setValueAt(value: Any, rowIndex: Int, columnIndex: Int) {
        if (value !is String)
            return
        val variable = getVariableAt(rowIndex)
        when (columnIndex) {
            Column.NAME.index -> variable.setLocalSubstitute(value.trim().ifBlank { null })
            Column.NOTES.index -> variable.notes = value.ifBlank { null }
        }
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int) = columnIndex != Column.VALUE.index

    companion object {
        val columnNames = Column.values().sortedBy { it.index }.map { it.header }
    }

    enum class Column(val header: String, val index: Int) {
        NAME("Name", 0),
        VALUE("Value", 1),
        NOTES("Notes", 2)
    }
}
