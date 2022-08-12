package truesight.ui.tabs

import burp.ITab
import truesight.ui.settings.SettingsPane

class SettingsTab : ITab {
    private val container = SettingsPane()

    override fun getTabCaption() = CAPTION
    override fun getUiComponent() = container

    companion object {
        const val CAPTION = "Settings"
    }
}
