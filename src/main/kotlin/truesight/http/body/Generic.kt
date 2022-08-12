package truesight.http.body

import truesight.Extension

/** Generic HTTP message body. */
class Generic(private val bytes: ByteArray) : Body() {
    constructor(base64: String) : this(Extension.helpers.base64Decode(base64))

    override fun isEmpty() = bytes.isEmpty()
    override fun isNotEmpty() = !isEmpty()

    override fun equals(other: Any?) = other is Generic && this.bytes.contentEquals(bytes)
    override fun hashCode() = bytes.hashCode()

    override fun toBytes() = bytes
    override fun toJson() = bytes.toBase64()
}

private fun ByteArray.toBase64(): String = Extension.helpers.base64Encode(this)
