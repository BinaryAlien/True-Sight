package truesight.ui.modules.catalog

import truesight.Helper
import truesight.Project
import truesight.modules.Catalog
import truesight.ui.document.table.DocumentTable
import truesight.ui.modules.catalog.actions.ActionWorkflow
import javax.swing.ListSelectionModel

class CatalogTable : DocumentTable(Project.catalog) {
    init {
        initActions()
        initTable()
    }

    override fun getTooltipText(rowIndex: Int, columnIndex: Int): String? {
        val document = model.getDocumentAt(rowIndex)
        return when (columnIndex) {
            Catalog.Column.HOST.index -> document.request.url.host
            Catalog.Column.METHOD.index -> document.request.method.description
            Catalog.Column.PATH.index -> document.request.url.path + document.request.queryString
            Catalog.Column.STATUS.index -> document.response.statusText
            Catalog.Column.TITLE.index -> document.title
            Catalog.Column.WORKFLOW.index -> document.workflow
            else -> null
        }
    }

    private fun initActions() {
        actionMap.put(ActionWorkflow.KEY, ActionWorkflow(this))
    }

    private fun initTable() {
        autoCreateRowSorter = true
        componentPopupMenu = Helper.createPopupMenuFrom(actionMap)
        selectionModel.selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        setColumnWidth(Catalog.Column.METHOD.index, 70)
        setColumnWidth(Catalog.Column.STATUS.index, 60)
    }
}
