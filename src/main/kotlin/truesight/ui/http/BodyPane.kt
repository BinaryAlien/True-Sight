package truesight.ui.http

import truesight.Extension
import truesight.http.body.Body
import java.awt.BorderLayout
import javax.swing.JPanel

class BodyPane(body: Body, isRequest: Boolean) : JPanel() {
    init {
        layout = BorderLayout()
        val bodyEditor = Extension.callbacks.createMessageEditor(null, false)
        bodyEditor.setMessage(body.toBytes(), isRequest)
        add(bodyEditor.component, BorderLayout.CENTER)
    }
}
