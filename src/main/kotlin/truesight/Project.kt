package truesight

import truesight.Helper.pluralize
import truesight.modules.Catalog
import truesight.modules.Substitutions
import truesight.modules.Workflows
import java.io.File

object Project : Module {
    val catalog = Catalog()
    val workflows = Workflows()
    val substitutions = Substitutions()

    private val modules = arrayOf(catalog, workflows, substitutions)

    override fun loadFromDisk(projectDir: File) {
        Extension.logger.info("Loading project @ ${projectDir.canonicalPath}")
        if (projectDir.exists()) {
            modules.forEach { it.load(projectDir) }
            Extension.logger.info("${catalog.documentCount} " + pluralize("document", catalog.documentCount) + " loaded.")
            Extension.logger.info("${workflows.count} " + pluralize("workflow", workflows.count) + " loaded.")
            Extension.logger.info("${substitutions.count} " + pluralize("global substitution", substitutions.count) + " loaded.")
        } else {
            Settings.projectDir.set(null)
            Extension.logger.warning("Directory ${projectDir.canonicalPath} does not exist: running on a temporary project.")
        }
    }

    override fun loadTemporary() {
        Extension.logger.info("Loading a temporary project.")
        modules.forEach { it.load(null) }
    }
}
