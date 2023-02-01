package mx.com.gm.util;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailService {

	public static void enviarCorreo(String destinatario, String asunto, String cuerpo, String archivo1, String archivo2)
			throws MessagingException {

		// Configurar las propiedades de la sesión de correo electrónico
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.enable", "true");

		// Crear una sesión de correo electrónico
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication("enriquesuarez1991@gmail.com", "zetkcktsbhlnljom");
			}
		});

		// Crear el mensaje de correo electrónico
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("enriquesuarez1991@gmail.com"));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destinatario));
		//message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("jhoanna.romera1902@gmail.com"));
		message.setSubject(asunto);

		// Crear la parte del cuerpo del mensaje
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(cuerpo, "text/html");

		// Crear la parte con los archivos adjuntos
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		if (archivo1 != "") {

			// Adjuntar el primer archivo
			MimeBodyPart attachment1 = new MimeBodyPart();
			DataSource source1 = new FileDataSource(new File(archivo1));
			attachment1.setDataHandler(new DataHandler(source1));
			attachment1.setFileName(new File(archivo1).getName());
			multipart.addBodyPart(attachment1);
		}
		if (archivo2 != "") {
			// Adjuntar el segundo archivo
			MimeBodyPart attachment2 = new MimeBodyPart();
			DataSource source2 = new FileDataSource(new File(archivo2));
			attachment2.setDataHandler(new DataHandler(source2));
			attachment2.setFileName(new File(archivo2).getName());
			multipart.addBodyPart(attachment2);
		}

		// Agregar las partes del mensaje al mensaje final
		message.setContent(multipart);

		// Enviar el mensaje de correo electrónico
		Transport.send(message);
	}
}
