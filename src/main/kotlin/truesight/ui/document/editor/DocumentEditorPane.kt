package truesight.ui.document.editor

import truesight.Document
import truesight.ui.http.MessagePane
import javax.swing.JSplitPane

class DocumentEditorPane(document: Document) : JSplitPane(VERTICAL_SPLIT) {
    private val overviewPane = OverviewPane(document)

    init {
        topComponent = overviewPane
        bottomComponent =  MessagePane(document.request, document.response)
    }

    fun saveChanges() {
        overviewPane.saveChanges()
    }
}
