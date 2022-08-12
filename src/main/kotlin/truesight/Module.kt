package truesight

import java.io.File

interface Module {
    fun loadFromDisk(projectDir: File)
    fun loadTemporary()
}

fun Module.load(projectDir: File?) {
    if (projectDir != null)
        loadFromDisk(projectDir)
    else
        loadTemporary()
}
