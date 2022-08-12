package truesight.ui.tabs

import burp.ITab
import truesight.ui.modules.catalog.CatalogTable
import javax.swing.JScrollPane

class CatalogTab : ITab {
    private val container = JScrollPane(CatalogTable())

    override fun getTabCaption() = CAPTION
    override fun getUiComponent() = container

    companion object {
        const val CAPTION = "Catalog"
    }
}
