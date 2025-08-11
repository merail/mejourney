package merail.life.core.extensions

import android.content.Context
import android.content.Intent

fun Context.rerunApp() = packageManager.getLaunchIntentForPackage(packageName)?.component?.let {
    val mainIntent = Intent.makeRestartActivityTask(it)
    mainIntent.setPackage(packageName)
    startActivity(mainIntent)
    Runtime.getRuntime().exit(0)
}