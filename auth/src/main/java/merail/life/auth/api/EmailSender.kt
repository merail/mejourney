package merail.life.auth.api

import merail.life.auth.BuildConfig
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSender {
    fun sendEmail(email: String) {
        try {
            val stringSenderEmail = BuildConfig.HOST_EMAIL
            val stringPasswordSenderEmail = BuildConfig.HOST_PASSWORD
            val stringReceiverEmail = email

            val stringHost = "smtp.gmail.com"

            val properties: Properties = System.getProperties()

            properties.setProperty("mail.transport.protocol", "smtp")
            properties.setProperty("mail.host", stringHost)
            properties["mail.smtp.host"] = stringHost
            properties["mail.smtp.port"] = "465"
            properties["mail.smtp.socketFactory.fallback"] = "false"
            properties.setProperty("mail.smtp.quitwait", "false")
            properties["mail.smtp.socketFactory.port"] = "465"
            properties["mail.smtp.starttls.enable"] = "true"
            properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            properties["mail.smtp.ssl.enable"] = "true"
            properties["mail.smtp.auth"] = "true"
            properties["mail.smtp.ssl.protocols"] = "TLSv1.2"

            val session: Session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                    return javax.mail.PasswordAuthentication(
                        stringSenderEmail,
                        stringPasswordSenderEmail
                    )
                }
            })

            val mimeMessage = MimeMessage(session)
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(stringReceiverEmail))

            mimeMessage.subject = "Subject: Android App email"
            mimeMessage.setText("Hello Programmer, \n\nProgrammer World has sent you this 2nd email. \n\n Cheers!\nProgrammer World")

            val thread = Thread {
                try {
                    Transport.send(mimeMessage)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }
            thread.start()
        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}