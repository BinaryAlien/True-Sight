package truesight.ui.http

import truesight.http.body.Form
import truesight.http.body.Generic
import truesight.http.request.Request
import truesight.ui.http.variable.VariablePane
import javax.swing.JTabbedPane

class RequestPane(request: Request) : JTabbedPane() {
    init {
        if (request.headers.isNotEmpty())
            add("Headers", VariablePane(request.headers))
        if (request.queryParameters.isNotEmpty())
            add("Params", VariablePane(request.queryParameters))
        if (request.cookies.isNotEmpty())
            add("Cookies", VariablePane(request.cookies))
        if (request.body.isNotEmpty()) {
            val bodyPane =
                when (request.body) {
                    is Form -> VariablePane(request.body.fields)
                    is Generic -> BodyPane(request.body, isRequest = true)
                    else -> throw RuntimeException("Unknown body type")
                }
            add("Body", bodyPane)
        }
    }
}
