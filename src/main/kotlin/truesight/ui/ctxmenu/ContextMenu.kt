package truesight.ui.ctxmenu

import burp.IContextMenuFactory
import burp.IContextMenuInvocation
import burp.IHttpRequestResponse
import truesight.ui.ctxmenu.actions.ActionSend
import javax.swing.JMenuItem

class ContextMenu : IContextMenuFactory {
    override fun createMenuItems(invocation: IContextMenuInvocation): List<JMenuItem> {
        val items = mutableListOf<JMenuItem>()
        if (targetedContexts.contains(invocation.invocationContext) && !containsNullResponse(invocation.selectedMessages))
            items.add(JMenuItem(ActionSend(invocation.selectedMessages)))
        return items
    }

    companion object {
        private val targetedContexts = arrayOf(
            IContextMenuInvocation.CONTEXT_PROXY_HISTORY,
            IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST,
            IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE,
            IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST,
            IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE
        )

        private fun containsNullResponse(messages: Array<IHttpRequestResponse>) = messages.any { it.response == null }
    }
}
