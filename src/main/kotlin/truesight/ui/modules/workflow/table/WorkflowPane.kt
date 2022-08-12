package truesight.ui.modules.workflow.table

import truesight.Workflow
import javax.swing.JScrollPane

class WorkflowPane(workflow: Workflow) : JScrollPane(WorkflowTable(WorkflowTableModel(workflow)))
