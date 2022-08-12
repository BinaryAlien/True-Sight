package truesight.ui.http.variable

import truesight.http.Variable
import javax.swing.JScrollPane

class VariablePane(variables: List<Variable>, type: Variable.Type) : JScrollPane(VariableTable(variables, type))
