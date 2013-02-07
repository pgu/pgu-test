package com.pgu.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgu.shared.XmppUser;

@SuppressWarnings("serial")
public class SubscribeXMPPServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final DAO dao = new DAO();
    private final XmppHelper helper = new XmppHelper();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final XmppUser user = helper.retrieveUserFromSubscription(req);
        subscribe(user);
    }

    private void subscribe(final XmppUser user) {
        log.info("subscribe: " + user.getFullJid());
        user.setSubscriptionStatus("subscribe");
        dao.ofy().put(user);
    }

}
