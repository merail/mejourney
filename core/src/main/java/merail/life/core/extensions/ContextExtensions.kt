package merail.life.core.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent

val Context.activity: Activity?
    get() {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

fun Context.rerunApp() = packageManager.getLaunchIntentForPackage(packageName)?.component?.let {
    val mainIntent = Intent.makeRestartActivityTask(it)
    mainIntent.setPackage(packageName)
    startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}