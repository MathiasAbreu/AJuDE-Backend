package br.com.psoft.ajude.services;

import br.com.psoft.ajude.entities.Usuario;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import javax.servlet.http.HttpServlet;

public class EmailService extends HttpServlet {

    public static void service(Usuario usuario) {

        final String sendgridApiKey = "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";
        final String sendgridSender = "ajude@application.com.br";

        final String toEmail = usuario.getEmail();

        Email to = new Email(toEmail);
        Email from = new Email(sendgridSender);
        String subject = "Este é um email de boas vindas!";
        Content content = new Content("text/plain", "Seja Bem vindo ao AJuDE! Acesse esse link: https://ajude-frontend.herokuapp.com para usurfruir das funcionalidades do nosso aplicativo.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendgrid = new SendGrid(sendgridApiKey);

        Request request = new Request();

        try {

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendgrid.api(request);

        } catch (IOException e) {
            System.out.println("Não foi possível enviar o email para o usuário: " + usuario.getEmail() + " Erro: " + e.getMessage());
        }
    }
}
