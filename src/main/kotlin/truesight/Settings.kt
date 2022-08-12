package truesight

import java.io.File

object Settings {
    val projectDir = Setting(
        name = "projectDir",
        defaultValue = null,
        serialize = { if (it != null) it.canonicalPath else "" },
        deserialize = { if (it.isNotBlank()) File(it) else null }
    )

    val indent = Setting(
        name = "indent",
        defaultValue = 0,
        serialize = Int::toString,
        deserialize = String::toInt
    )
}

class Setting<T>(
    private val name: String,
    defaultValue: T,
    private val serialize: Serializer<T>,
    deserialize: Deserializer<T>
) {
    private var value: T

    private val observers = mutableSetOf<Observer<T>>()

    init {
        val valueSaved = Extension.callbacks.loadExtensionSetting(name)
        if (valueSaved == null) {
            value = defaultValue
            save()
        } else {
            value = deserialize(valueSaved)
        }
    }

    fun get(): T = value

    fun set(newValue: T) {
        value = newValue
        save()
        notify(newValue)
    }

    fun bind(fn: Observer<T>) = observers.add(fn)

    private fun save() = Extension.callbacks.saveExtensionSetting(name, serialize(value))

    private fun notify(newValue: T) = observers.forEach { it(newValue) }
}

typealias Serializer<T> = (value: T) -> String
typealias Deserializer<T> = (value: String) -> T
typealias Observer<T> = (value: T) -> Unit
