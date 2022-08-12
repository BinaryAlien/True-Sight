package truesight.ui

import truesight.Extension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JDialog

open class Editor(title: String) : JDialog(Extension.owningWindow, title) {
    private var isClosing = false

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        super.setLocationRelativeTo(parent)
        super.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                saveChanges()
            }
        })
    }

    open fun saveChanges() {}

    fun close() {
        if (!isClosing) {
            isClosing = true
            dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
        }
    }
}
