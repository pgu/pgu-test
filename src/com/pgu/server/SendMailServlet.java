package com.pgu.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SendMailServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SendMailServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        LOGGER.info("do get...");
        doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        try {
            sendMail();
        } catch (final MessagingException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void sendMail() throws UnsupportedEncodingException, MessagingException {
        LOGGER.info("send mail...");

        final Properties props = new Properties();
        final Session session = Session.getDefaultInstance(props, null);

        final Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("guilcher.pascal.dev@gmail.com", "The boss"));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("guilcher.pascal@gmail.com", "The customer"));
        msg.setSubject("email subject");
        msg.setText("email body test\nsee this: http://pgu-calendar.appspot.com/\n");

        Transport.send(msg);

    }

}
