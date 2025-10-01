package merail.life.core.log

interface IMejourneyLogger {
    fun d(tag: String, msg: String)
    fun w(tag: String, msg: String, tr: Throwable? = null)
}