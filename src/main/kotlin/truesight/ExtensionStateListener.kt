package truesight

import burp.IExtensionStateListener
import truesight.ui.document.editor.DocumentEditor
import truesight.ui.modules.workflow.editor.WorkflowEditor

class ExtensionStateListener : IExtensionStateListener {
    override fun extensionUnloaded() {
        /*
         * When using the 'closeAll' method of editors, the saving of the
         * changes occurs as soon as the window closing event is caught.
         *
         * However, by the time the window closing event is caught, Burp Suite will have
         * unloaded the extension, making it unable for editors to save changes.
         *
         * This is the reason saving has to be done explicitly and using blocking calls in order
         * to prevent Burp Suite from unloading the extension before the saves are made.
         */

        forceSaveEditors()
        forceCloseEditors()
    }

    private fun forceSaveEditors() {
        DocumentEditor.forceSaveAll()
        WorkflowEditor.forceSaveAll()
    }

    private fun forceCloseEditors() {
        DocumentEditor.forceCloseAll()
        WorkflowEditor.forceCloseAll()
    }
}
