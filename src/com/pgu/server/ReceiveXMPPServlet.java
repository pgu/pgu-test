package com.pgu.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class ReceiveXMPPServlet extends HttpServlet {

    private final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        final Message message = xmpp.parseMessage(req);

        final JID fromJid = message.getFromJid();
        final String body = message.getBody();

        // save messages
        final Entity entity = new Entity("chat_message", "example");

        entity.setProperty("jid", fromJid);

        entity.setProperty("body", new Text(body));
        entity.setProperty("length", body.length());

        entity.setProperty("time", new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(new Date()));

        datastoreService.put(entity);
    }



}
