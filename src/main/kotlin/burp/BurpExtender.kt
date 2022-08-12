package burp

import truesight.*
import truesight.logging.DualStreamHandler
import truesight.ui.ctxmenu.ContextMenu
import truesight.ui.tabs.CatalogTab
import truesight.ui.tabs.ExtensionTab
import truesight.ui.tabs.SettingsTab
import java.util.logging.Level
import java.util.logging.Logger

class BurpExtender : IBurpExtender {
    /* Entry point */
    override fun registerExtenderCallbacks(callbacks: IBurpExtenderCallbacks) {
        initCallbacks(callbacks)
        initLogger()
        initProject()
        initExtension()
    }

    private fun initCallbacks(callbacks: IBurpExtenderCallbacks) {
        Extension.callbacks = callbacks
        Extension.helpers = callbacks.helpers
    }

    private fun initLogger() {
        Extension.logger = Logger.getAnonymousLogger()
        Extension.logger.addHandler(DualStreamHandler(
            Extension.callbacks.stdout,
            Extension.callbacks.stderr,
            Extension.loggerFormatter
        ))
        Extension.logger.level = Level.INFO
    }

    private fun initProject() {
        Project.load(Settings.projectDir.get())
        Settings.projectDir.bind { Project.load(it) }
    }

    private fun initExtension() {
        Extension.callbacks.setExtensionName(Extension.NAME)
        Extension.callbacks.registerContextMenuFactory(ContextMenu())
        Extension.callbacks.registerExtensionStateListener(ExtensionStateListener())
        Extension.callbacks.addSuiteTab(ExtensionTab(
            CatalogTab(), SettingsTab()
        ))
    }
}
