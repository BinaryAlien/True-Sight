package truesight.ui.tabs

import burp.ITab
import truesight.Extension
import javax.swing.JTabbedPane

class ExtensionTab(vararg tabs: ITab) : ITab {
    private val tab = JTabbedPane()

    init {
        tabs.forEach { tab.add(it.tabCaption, it.uiComponent) }
    }

    override fun getTabCaption() = Extension.NAME
    override fun getUiComponent() = tab
}
