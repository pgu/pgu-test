package com.pgu.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchService;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchServiceFactory;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.pgu.shared.XmppMessage;

@SuppressWarnings("serial")
public class ReceiveXMPPServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    //    private final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

    private final DAO dao = new DAO();
    private final XmppHelper helper = new XmppHelper();

    private final ChannelService channelService = ChannelServiceFactory.getChannelService();
    private final ProspectiveSearchService prospectiveSearchService = ProspectiveSearchServiceFactory.getProspectiveSearchService();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        final Message chatMessage = xmpp.parseMessage(req);

        final JID fromJid = chatMessage.getFromJid();
        final String body = chatMessage.getBody();

        final String fullJid = fromJid.getId();

        log.info("receive xmpp message from: " + fullJid);

        // save message
        final XmppMessage msg =  new XmppMessage();
        msg.setFullJid(fullJid);
        msg.setBody(body);
        msg.setTime(new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss").format(new Date()));
        dao.ofy().put(msg);
        //        datastoreService.put(entity);

        // prospective search
        final Entity tmpMsg = new Entity("xmpp_message");
        tmpMsg.setProperty("body", body);

        for (final String topic : prospectiveSearchService.listTopics("", 1000)) {
            prospectiveSearchService.match(tmpMsg, topic);
        }

        // channel
        final String channelMessage = //
                "{\"type\":\"chat\",\"body\":\"" + //
                body + " [ from " + fullJid + "]" + //
                "\"}";

        final String channelClientId = "1";
        channelService.sendMessage(new ChannelMessage(channelClientId, channelMessage));

    }



}
