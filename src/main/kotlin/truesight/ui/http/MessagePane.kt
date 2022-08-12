package truesight.ui.http

import truesight.http.request.Request
import truesight.http.response.Response
import javax.swing.JTabbedPane

class MessagePane(request: Request, response: Response) : JTabbedPane() {
    init {
        add("Request", RequestPane(request))
        add("Response", ResponsePane(response))
    }
}
