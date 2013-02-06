package com.pgu.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.Subscription;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.pgu.shared.UserXmpp;

public class XmppHelper {

    private final DAO dao = new DAO();

    public UserXmpp retrieveUserFromSubscription(final HttpServletRequest req) {

        final Subscription subscription = parseSubscription(req);

        final JID jid = subscription.getFromJid();
        final String fullJid = jid.getId();
        final String bareJid = toBareJid(fullJid);

        return findUser(fullJid, bareJid);
    }

    private String toBareJid(final String fullJid) {
        // Split the bare XMPP address (e.g., user@gmail.com)
        // from the resource (e.g., gmail.CD6EBC4A)
        return fullJid.split("/")[0];
    }

    private UserXmpp findUser(final String fullJid, final String bareJid) {

        final UserXmpp user = dao.ofy().find(UserXmpp.class, fullJid);
        if (user == null) {

            final UserXmpp new_user = new UserXmpp();
            new_user.setFullJid(fullJid);
            new_user.setBareJid(bareJid);
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

    public UserXmpp retrieveUserFromPresence(final HttpServletRequest req) {

        final Presence presence = parsePresence(req);

        final JID jid = presence.getFromJid();
        final String fullJid = jid.getId();
        final String bareJid = toBareJid(fullJid);

        return findUser(fullJid, bareJid);
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
