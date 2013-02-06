package com.pgu.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchServiceFactory;

@SuppressWarnings("serial")
public class MatchResponseServlet extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        final int resultsOffset = Integer.parseInt(req.getParameter("results_offset"));
        final int resultsCount = Integer.parseInt(req.getParameter("results_count"));

        final String [] reqSubIDs = req.getParameterValues("id");

        // Optional inclusion of matched entity if requested in original match(...) request:
        Entity matchedEntity = null;
        if (req.getParameter("document") != null) {
            matchedEntity =
                    ProspectiveSearchServiceFactory.getProspectiveSearchService().getDocument(req);
        }

        // Do something based on match...
    }

}
