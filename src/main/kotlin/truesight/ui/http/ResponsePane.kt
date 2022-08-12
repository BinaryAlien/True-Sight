package truesight.ui.http

import truesight.http.response.Response
import truesight.ui.http.variable.VariablePane
import javax.swing.JTabbedPane

class ResponsePane(response: Response) : JTabbedPane() {
    init {
        if (response.headers.isNotEmpty())
            add("Headers", VariablePane(response.headers))
        if (response.cookies.isNotEmpty())
            add("Cookies", VariablePane(response.cookies))
        if (response.body.isNotEmpty())
            add("Body", BodyPane(response.body, isRequest = false))
    }
}
