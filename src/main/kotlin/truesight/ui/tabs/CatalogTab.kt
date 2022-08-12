package truesight.ui.tabs

import burp.ITab
import truesight.ui.modules.CatalogPane

class CatalogTab : ITab {
    private val container = CatalogPane()

    override fun getTabCaption() = CAPTION
    override fun getUiComponent() = container

    companion object {
        const val CAPTION = "Catalog"
    }
}
