package truesight.ui

import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.JTable
import javax.swing.table.TableModel

open class Table() : JTable() {
    val selectedRowModelIndex: Int
        get() =
            if (isValidRow(selectedRow))
                if (rowSorter != null)
                    rowSorter.convertRowIndexToModel(selectedRow)
                else
                    selectedRow
            else
                -1

    constructor(dm: TableModel) : this() { model = dm }

    override fun getToolTipText(event: MouseEvent): String? {
        val cell = cellAtPoint(event.point)
        return if (cell != null)
            getTooltipText(cell.first, cell.second)
        else
            null
    }

    fun cellAtPoint(point: Point): Pair<Int, Int>? {
        var rowIndex = rowAtPoint(point)
        var columnIndex = columnAtPoint(point)
        if (!isValidRow(rowIndex) && isValidColumn(columnIndex))
            return null
        rowIndex = convertRowIndexToModel(rowIndex)
        columnIndex = convertColumnIndexToModel(columnIndex)
        return Pair(rowIndex, columnIndex)
    }

    open fun getTooltipText(rowIndex: Int, columnIndex: Int): String? = null

    fun isValidRow(rowIndex: Int) = rowIndex in 0 until rowCount
    fun isValidColumn(columnIndex: Int) = columnIndex in 0 until columnCount

    fun setColumnWidth(columnIndex: Int, width: Int) {
        val column = columnModel.getColumn(columnIndex)
        column.minWidth = width
        column.maxWidth = width
        column.resizable = false
    }
}
