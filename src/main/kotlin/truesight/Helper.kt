package truesight

import javax.swing.ActionMap
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

object Helper {
    fun pluralize(text: String, count: Int) = text + (if (count != 1) "s" else "")

    fun createPopupMenuFrom(actionMap: ActionMap): JPopupMenu {
        val menu = JPopupMenu()
        for (key in actionMap.keys())
            menu.add(JMenuItem(actionMap[key]))
        return menu
    }
}
