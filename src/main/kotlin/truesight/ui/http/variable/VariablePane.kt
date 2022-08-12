package truesight.ui.http.variable

import truesight.http.Variable
import javax.swing.JScrollPane

class VariablePane(variables: List<Variable>) : JScrollPane(VariableTable(variables))
