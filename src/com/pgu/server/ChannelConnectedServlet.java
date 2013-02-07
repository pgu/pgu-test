package com.pgu.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class ChannelConnectedServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    ChannelService channelService = ChannelServiceFactory.getChannelService();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        log.info("channel connected");

        final ChannelPresence presence = channelService.parsePresence(req);

        log.info("client id: " + presence.clientId());
        log.info("is connected? " + presence.isConnected());
    }

}
