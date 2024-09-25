package merail.life.auth.impl.repository

import merail.life.auth.impl.BuildConfig
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import kotlin.reflect.KProperty

internal class EmailSessionCreator {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Session {
        val stringSenderEmail = BuildConfig.HOST_EMAIL
        val stringPasswordSenderEmail = BuildConfig.HOST_PASSWORD

        val stringHost = "smtp.gmail.com"

        val properties = System.getProperties()

        properties["mail.transport.protocol"] = "smtp"
        properties["mail.host"] = stringHost
        properties["mail.smtp.host"] = stringHost
        properties["mail.smtp.port"] = "465"
        properties["mail.smtp.socketFactory.fallback"] = "false"
        properties["mail.smtp.quitwait"] = "false"
        properties["mail.smtp.socketFactory.port"] = "465"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties["mail.smtp.ssl.enable"] = "true"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.ssl.protocols"] = "TLSv1.2"

        val session = Session.getInstance(
            properties,
            object : Authenticator() {
                override fun getPasswordAuthentication() = PasswordAuthentication(
                    stringSenderEmail,
                    stringPasswordSenderEmail,
                )
            },
        )

        return session
    }
}