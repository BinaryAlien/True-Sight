package truesight.ui

import truesight.Extension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JDialog

open class Editor(title: String) : JDialog(Extension.owningWindow, title) {
    private var isClosing = false

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        super.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                saveChanges()
            }
        })
    }

    open fun saveChanges() {}

    fun centerLocationRelativeToParent() {
        if (parent == null) {
            setLocation(0, 0)
        } else {
            val x = parent.x + parent.width / 2 - width / 2
            val y = parent.y + parent.height / 2 - height / 2
            setLocation(x, y)
        }
    }

    fun close() {
        if (!isClosing) {
            isClosing = true
            dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
        }
    }
}
