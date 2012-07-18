package com.pgu.server;

import java.util.ArrayList;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;
import com.pgu.client.GreetingService;
import com.pgu.shared.Book;
import com.pgu.shared.FieldVerifier;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private final DAO dao = new DAO();

    @Override
    public String greetServer(String input) throws IllegalArgumentException {
        // Verify that the input is valid.
        if (!FieldVerifier.isValidName(input)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException("Name must be at least 4 characters long");
        }

        final String serverInfo = getServletContext().getServerInfo();
        String userAgent = getThreadLocalRequest().getHeader("User-Agent");

        // Escape data from the client to avoid cross-site script vulnerabilities.
        input = escapeHtml(input);
        userAgent = escapeHtml(userAgent);

        return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
                + userAgent;
    }

    /**
     * Escape an html string. Escaping data received from the client helps to prevent cross-site script vulnerabilities.
     * 
     * @param html
     *            the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(final String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    @Override
    public void createBooks() {
        // 1000 done
        for (int i = 0; i < 10; i++) {
            final Book book = new Book();
            book.setTitle("t" + 1000 + i);
            dao.ofy().put(book);
        }
    }

    @Override
    public void fetchBooks() {
        final ArrayList<Long> bookIds = new ArrayList<Long>();

        final Query<Book> q = dao.ofy().query(Book.class);
        System.out.println("count: " + q.count());

        final QueryResultIterator<Book> bookItr = q.iterator();

        while (bookItr.hasNext()) {
            bookIds.add(bookItr.next().getId());
        }

        final Query<Book> mainQ = dao.ofy().query(Book.class).filter("id in", bookIds);
        System.out.println("count: " + mainQ.count());

        final QueryResultIterator<Book> _bookItr = mainQ.iterator();

        final StringBuilder sb = new StringBuilder();
        while (_bookItr.hasNext()) {
            sb.append(_bookItr.next().getId() + ",");
        }
        System.out.print(sb.toString());

    }

    @Override
    public void deleteBooks() {
        final QueryResultIterator<Book> bookItr = dao.ofy().query(Book.class).iterator();
        while (bookItr.hasNext()) {
            dao.ofy().delete(bookItr.next());
        }
    }
}
