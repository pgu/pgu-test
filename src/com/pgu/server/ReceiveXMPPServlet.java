package com.pgu.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

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

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

    private final XmppHelper helper = new XmppHelper();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        // TODO PGU Feb 6, 2013
        // TODO PGU Feb 6, 2013
        // TODO PGU Feb 6, 2013
        // TODO PGU Feb 6, 2013
        // TODO PGU Feb 6, 2013 faire une entity
        // TODO PGU Feb 6, 2013
        // TODO PGU Feb 6, 2013
        final XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        final Message message = xmpp.parseMessage(req);

        final JID fromJid = message.getFromJid();
        final String body = message.getBody();

        final String fullJid = fromJid.getId();
        final String bareJid = helper.toBareJid(fullJid);

        log.info("receive xmpp message from: " + fullJid);

        // save messages
        final Entity entity = new Entity("chat_message", "example");

        entity.setProperty("full_jid", fullJid);
        entity.setProperty("bare_jid", bareJid);

        entity.setProperty("body", new Text(body));
        entity.setProperty("length", body.length());

        entity.setProperty("time", new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(new Date()));

        datastoreService.put(entity);
    }



}
