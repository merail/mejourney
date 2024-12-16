package merail.life.auth.impl.mail

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import merail.life.auth.impl.R
import javax.mail.Message
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

internal class EmailSender(
    private val context: Context,
) {
    companion object {
        private const val LOWER_CODE_LIMIT = 1000

        private const val UPPER_CODE_LIMIT = 10000

        private const val MESSAGE_MIME_TYPE = "text/html; charset=utf-8"
    }

    private val session by EmailSessionCreator()

    private var code = 0

    suspend fun sendOtp(
        email: String,
    ) = withContext(Dispatchers.IO) {
        val mimeMessage = MimeMessage(session)
        mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(email))
        mimeMessage.subject = context.getString(R.string.email_title)
        code = generateCode()
        mimeMessage.setContent(context.getString(R.string.email_body, code), MESSAGE_MIME_TYPE)
        Transport.send(mimeMessage)
    }

    fun getCurrentOtp() = code

    private fun generateCode() = Random.nextInt(LOWER_CODE_LIMIT, UPPER_CODE_LIMIT)
}