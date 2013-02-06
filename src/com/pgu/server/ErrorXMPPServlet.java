package com.pgu.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.io.ByteStreams;



@SuppressWarnings("serial")
public class ErrorXMPPServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ErrorXMPPServlet.class.getName());

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        // Parse the POST data, which is sent as a MIME stream containing the stanza.
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ServletInputStream inputStream = req.getInputStream();
        ByteStreams.copy(inputStream, baos);

        // Log the error
        log.warning("Error stanza received: " + baos.toString());

    }






}
