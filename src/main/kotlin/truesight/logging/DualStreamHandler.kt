package truesight.logging

import java.io.OutputStream
import java.util.logging.Formatter
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.StreamHandler

class DualStreamHandler(out: OutputStream, err: OutputStream, fmt: Formatter) : StreamHandler(out, fmt) {
    private val err = StreamHandler(err, fmt)

    override fun publish(record: LogRecord) {
        if (record.level.intValue() <= Level.INFO.intValue()) {
            super.publish(record) // out
            super.flush()
        } else {
            err.publish(record)
            err.flush()
        }
    }
}
