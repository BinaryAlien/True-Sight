package truesight.modules

import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import truesight.*
import truesight.ui.modules.workflow.editor.WorkflowEditor
import java.io.*

class Workflows : Module {
    val count: Int
        get() = loadedWorkflows.size

    private val loadedWorkflows = mutableSetOf<Workflow>()
    private var currentSaveDir: File? = null

    /** Determines to which workflow a document belongs. */
    fun which(document: Document) = loadedWorkflows.find { it.documents.contains(document) }

    fun reassign(document: Document, workflowName: String?) {
        if (document.workflow == workflowName)
            return
        unassign(document)
        if (workflowName != null && workflowName.isNotBlank()) {
            val workflow = getOrCreateWorkflowByName(workflowName)
            workflow.documents.add(document)
            saveWorkflow(workflow)
        }
    }

    fun unassign(document: Document) {
        val workflow = which(document)
        if (workflow != null) {
            workflow.documents.remove(document)
            saveWorkflowOrDeleteIfEmpty(workflow)
        }
    }

    fun exists(workflowName: String) = loadedWorkflows.any { it.name.equals(workflowName, ignoreCase = true) }

    fun handleUpdate(updatedWorkflow: Workflow) {
        saveWorkflowOrDeleteIfEmpty(updatedWorkflow)
    }

    override fun loadFromDisk(projectDir: File) {
        currentSaveDir = File(projectDir, "workflows")
        currentSaveDir!!.mkdir()
        reloadWorkflows()
    }

    override fun loadTemporary() {
        unloadWorkflows()
        currentSaveDir = null
    }

    private fun getOrCreateWorkflowByName(name: String): Workflow {
        var workflow = loadedWorkflows.find { it.name.equals(name, ignoreCase = true) }
        if (workflow == null) {
            workflow = Workflow(name)
            loadedWorkflows.add(workflow)
        }
        return workflow
    }

    private fun saveWorkflowOrDeleteIfEmpty(workflow: Workflow) {
        if (workflow.documents.isNotEmpty())
            saveWorkflow(workflow)
        else
            deleteWorkflow(workflow)
    }

    private fun saveWorkflow(workflow: Workflow) {
        val saveFile = getSaveFile(workflow) ?: return
        try {
            FileWriter(saveFile).use { workflow.toJson().write(it, Settings.indent.get(), 0) }
        } catch (ex: IOException) {
            // Can occur when the parent directory ceases to exist.
            ex.printStackTrace()
            Extension.logger.severe("Could not save workflow '${workflow.name}' ${workflow.id}: ${ex.message}")
        }
    }

    private fun reloadWorkflows() {
        unloadWorkflows()
        loadWorkflows()
    }

    private fun unloadWorkflows() {
        WorkflowEditor.closeAll()
        loadedWorkflows.clear()
    }

    private fun loadWorkflows() {
        currentSaveDir!!.listFiles(fileFilter)!!.forEach { loadWorkflow(it) }
        removeEmptyWorkflows()
    }

    private fun loadWorkflow(source: File) {
        try {
            FileReader(source).use {
                val json = JSONObject(JSONTokener(it))
                loadedWorkflows.add(Workflow(json))
            }
        } catch (ex: JSONException) {
            ex.printStackTrace()
            if (source.delete())
                Extension.logger.info("Invalid workflow file '${source.name}' was automatically deleted.")
            else
                Extension.logger.warning("Invalid workflow file '${source.name}' could not be deleted.")
        }
    }

    private fun deleteWorkflow(workflow: Workflow): Boolean {
        WorkflowEditor.close(workflow)
        val saveFile = getSaveFile(workflow) ?: return true
        val success = saveFile.delete()
        if (success)
            loadedWorkflows.remove(workflow)
        else
            Extension.logger.warning("Could not delete workflow '${workflow.name}' ${workflow.id}.")
        return success
    }

    private fun removeEmptyWorkflows() {
        val emptyWorkflows = loadedWorkflows.filter { it.documents.isEmpty() }
        emptyWorkflows.forEach { deleteWorkflow(it) }
    }

    private fun getSaveFile(workflow: Workflow) =
        if (currentSaveDir == null)
            null
        else
            File(currentSaveDir, "${workflow.id}.json")

    companion object {
        val fileFilter = FileFilter { it.extension == "json" }
    }
}
