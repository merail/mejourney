package merail.life.core.log

import android.util.Log
import javax.inject.Inject

internal class MejourneyLogger @Inject constructor() : IMejourneyLogger {
    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun w(tag: String, msg: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.w(tag, msg, throwable)
        } else {
            Log.w(tag, msg)
        }
    }
}