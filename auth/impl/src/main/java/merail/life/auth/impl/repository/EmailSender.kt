package merail.life.auth.impl.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import merail.life.auth.impl.R
import javax.mail.Message
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

class EmailSender(
    private val context: Context,
) {
    private val session by EmailSessionCreator()

    private var code: Int = 0

    suspend fun sendOneTimePassword(stringReceiverEmail: String) = withContext(Dispatchers.IO) {
        val mimeMessage = MimeMessage(session)
        mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(stringReceiverEmail))
        mimeMessage.subject = context.getString(R.string.email_title)
        code = generateCode()
        mimeMessage.setText(context.getString(R.string.email_body))
        Transport.send(mimeMessage)
    }

    private fun generateCode() = Random.nextInt(0, 10000)
}