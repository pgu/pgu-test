package com.pgu.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.Subscription;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.pgu.shared.XmppUser;

public class XmppHelper {

    private final DAO dao = new DAO();

    public XmppUser retrieveUserFromSubscription(final HttpServletRequest req) {

        final Subscription subscription = parseSubscription(req);

        final JID jid = subscription.getFromJid();
        final String fullJid = jid.getId();

        return findUser(fullJid);
    }

    public String toBareJid(final String fullJid) {
        // Split the bare XMPP address (e.g., user@gmail.com)
        // from the resource (e.g., gmail.CD6EBC4A)
        return fullJid.split("/")[0];
    }

    private XmppUser findUser(final String fullJid) {

        final XmppUser user = dao.ofy().find(XmppUser.class, fullJid);
        if (user == null) {

            final XmppUser new_user = new XmppUser();
            new_user.setFullJid(fullJid);
            dao.ofy().put(new_user);
            return new_user;

        } else {
            return user;
        }
    }

    private Subscription parseSubscription(final HttpServletRequest req) {
        final XMPPService xmppService = XMPPServiceFactory.getXMPPService();

        try {
            return xmppService.parseSubscription(req);

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public XmppUser retrieveUserFromPresence(final HttpServletRequest req) {

        final Presence presence = parsePresence(req);

        final JID jid = presence.getFromJid();
        final String fullJid = jid.getId();

        return findUser(fullJid);
    }

    private Presence parsePresence(final HttpServletRequest req) {
        final XMPPService xmppService = XMPPServiceFactory.getXMPPService();

        try {
            return xmppService.parsePresence(req);

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
