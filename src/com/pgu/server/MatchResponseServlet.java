package com.pgu.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchServiceFactory;

@SuppressWarnings("serial")
public class MatchResponseServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(this.getClass().getName());
    ChannelService channelService = ChannelServiceFactory.getChannelService();

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        log.info("### matching response");

        final int resultsOffset = Integer.parseInt(req.getParameter("results_offset"));
        final int resultsCount = Integer.parseInt(req.getParameter("results_count"));

        final String [] reqSubIDs = req.getParameterValues("id");

        log.info("### offset: " + resultsOffset);
        log.info("### count: " + resultsCount);
        log.info("### subIds: " + Arrays.toString(reqSubIDs));

        // Optional inclusion of matched entity if requested in original match(...) request:
        Entity matchedEntity = null;
        if (req.getParameter("document") != null) {
            matchedEntity =
                    ProspectiveSearchServiceFactory.getProspectiveSearchService().getDocument(req);

            log.info("### matched entity");
            log.info(matchedEntity.toString());
            log.info("> kind > " + matchedEntity.getKey().getKind()); // Comment
            log.info("> k#id > " + matchedEntity.getKey().getId()); // id numerique

            for (final String reqSubID : reqSubIDs) {

                final String channelMessage = //
                        "{\"type\":\"search\",\"body\":\"" + //
                        "The entity of type '" + matchedEntity.getKey().getKind() + //
                        "' and with the id '" + matchedEntity.getKey().getId() + //
                        "' is positive for the subscription '" + reqSubID + //
                        "'\"}";

                final String channelClientId = "1";
                channelService.sendMessage(new ChannelMessage(channelClientId, channelMessage));
            }

        } else {
            log.info("No document");
        }
    }

}
