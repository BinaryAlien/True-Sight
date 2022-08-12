package truesight.ui.ctxmenu.actions

import burp.IHttpRequestResponse
import truesight.Project
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ActionSend(private val messages: Array<IHttpRequestResponse>) : AbstractAction("Send to Catalog") {
    override fun actionPerformed(event: ActionEvent) {
        Project.catalog.append(messages)
    }
}
