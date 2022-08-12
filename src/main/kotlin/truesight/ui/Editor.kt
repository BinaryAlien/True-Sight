package truesight.ui

import truesight.Extension
import java.awt.event.WindowEvent
import javax.swing.JDialog

open class Editor(title: String) : JDialog(Extension.owningWindow, title) {
    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        super.setLocationRelativeTo(parent)
    }

    open fun close() {
        dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
    }
}
