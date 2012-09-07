package com.pgu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
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

    private Index profileIdx;

    private Index getProfileIdx() {
        if (profileIdx == null) {
            profileIdx = SearchServiceFactory.getSearchService().getIndex(
                    IndexSpec.newBuilder().setName("profile_index"));
        }
        return profileIdx;
    }

    @Override
    public void importProfileJson() {

        final String jsonProfileA = profileTest("a_profile.json");
        final String jsonProfileB = profileTest("b_profile.json");

        final Document.Builder docBuilderA = Document.newBuilder();
        docBuilderA.addField(Field.newBuilder().setName("profile").setText(jsonProfileA));
        getProfileIdx().add(docBuilderA.build());

        final Document.Builder docBuilderB = Document.newBuilder();
        docBuilderB.addField(Field.newBuilder().setName("profile").setText(jsonProfileB));
        getProfileIdx().addAsync(docBuilderB.build());

    }

    @Override
    public String searchProfile(final String text) {
        final String _query = "~\"" + text + "\"";
        final com.google.appengine.api.search.Query mainQuery = com.google.appengine.api.search.Query.newBuilder() //
                .build(_query);

        final Results<ScoredDocument> docs = getProfileIdx().search(mainQuery);

        final StringBuilder sb = new StringBuilder();
        for (final ScoredDocument doc : docs) {
            sb.append(doc.getOnlyField("profile"));
            sb.append("<br/>");
            sb.append("<br/>");
            sb.append("<br/>");
        }

        return sb.toString();
    }

    private String profileTest(final String filename) {

        final InputStream is = getServletContext().getResourceAsStream("/WEB-INF/pgu/" + filename);
        final BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        final StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

}
