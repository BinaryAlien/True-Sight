package truesight.http.body

abstract class Body {
    abstract fun isEmpty(): Boolean
    abstract fun isNotEmpty(): Boolean

    abstract override fun equals(other: Any?): Boolean
    abstract override fun hashCode(): Int

    abstract fun toBytes(): ByteArray
    abstract fun toJson(): Any
}
