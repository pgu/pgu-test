package com.pgu.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class SendXMPPServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        log.info("send xmpp message ");

        final JID jid = new JID("guilcher.pascal@gmail.com");
        final String text = "hello world";

        final Message message = new MessageBuilder() //
        .withRecipientJids(jid) //
        .withBody(text) //
        .build();

        final XMPPService xmppService = XMPPServiceFactory.getXMPPService();

        if (xmppService.getPresence(jid).isAvailable()) {
            final SendResponse sendResponse = xmppService.sendMessage(message);

            if (sendResponse.getStatusMap().get(jid) == SendResponse.Status.SUCCESS) {
                resp.getWriter().write("OK");
            } else {
                resp.getWriter().write("NOT OK");
            }

        } else {
            resp.getWriter().write("Unavailable");
        }

    }













}
