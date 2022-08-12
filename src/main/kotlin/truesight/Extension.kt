package truesight

import burp.IBurpExtenderCallbacks
import burp.IExtensionHelpers
import java.awt.Window
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.LogRecord
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

object Extension {
    const val NAME = "True Sight"

    lateinit var callbacks: IBurpExtenderCallbacks
    lateinit var helpers: IExtensionHelpers

    lateinit var logger: Logger
    val loggerFormatter = object : SimpleFormatter() {
        private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS")
        override fun format(record: LogRecord): String {
            val time = dateFormat.format(Date.from(record.instant))
            return "$time: ${record.message}" + System.lineSeparator()
        }
    }

    // Hack to get the instance of the Burp Suite window.
    val owningWindow = Window.getWindows().find { it.name == "suiteFrame" }
}
