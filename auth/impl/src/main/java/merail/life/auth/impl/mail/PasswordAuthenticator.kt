package merail.life.auth.impl.mail

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication

internal object PasswordAuthenticator: Authenticator() {

    var hostEmail: String? = null

    var hostPassword: String? = null

    override fun getPasswordAuthentication() = PasswordAuthentication(
        hostEmail,
        hostPassword,
    )
}