package com.pgu.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgu.shared.UserXmpp;

@SuppressWarnings("serial")
public class ProbeXMPPServlet extends HttpServlet {

    private final DAO dao = new DAO();
    private final XmppHelper helper = new XmppHelper();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final UserXmpp user = helper.retrieveUserFromPresence(req);
        setProbe(user);
    }

    private void setProbe(final UserXmpp user) {
        user.setPresenceStatus("probe");
        dao.ofy().put(user);
    }

}
