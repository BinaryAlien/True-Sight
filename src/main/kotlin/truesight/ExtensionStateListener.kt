package truesight

import burp.IExtensionStateListener
import truesight.ui.document.editor.DocumentEditor
import truesight.ui.modules.workflow.editor.WorkflowEditor

class ExtensionStateListener : IExtensionStateListener {
    override fun extensionUnloaded() {
        DocumentEditor.closeAll()
        WorkflowEditor.closeAll()
    }
}
