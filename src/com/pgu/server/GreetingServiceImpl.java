package com.pgu.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.prospectivesearch.FieldType;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchService;
import com.google.appengine.api.prospectivesearch.ProspectiveSearchServiceFactory;
import com.google.appengine.api.prospectivesearch.Subscription;
import com.google.appengine.api.prospectivesearch.Subscription.State;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Query;
import com.pgu.client.GreetingService;
import com.pgu.shared.Book;
import com.pgu.shared.Comment;
import com.pgu.shared.FieldVerifier;
import com.pgu.shared.MyLocation;
import com.pgu.shared.PguSubscription;
import com.pgu.shared.XmppUser;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    //    private final DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

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
        getProfileIdx().add(docBuilderB.build());

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

    private Index testIdx;

    private Index getTestIdx() {
        if (testIdx == null) {
            testIdx = SearchServiceFactory.getSearchService().getIndex(IndexSpec.newBuilder().setName("test_index"));
        }
        return testIdx;
    }

    @Override
    public String importGeopoints() {

        addGeoDocument("java, j2ee", MyLocation.Prague, MyLocation.Rostock);
        addGeoDocument("gwt, app engine", MyLocation.Brest);
        addGeoDocument("python, django", MyLocation.Nantes, MyLocation.Madrid);
        addGeoDocument("ruby, rails", MyLocation.Paris);

        return "OK";
    }

    private static final String field_keywords = "keywords";
    private static final String field_location = "location";

    private void addGeoDocument(final String keywords, final MyLocation... locations) {

        for (final MyLocation loc : locations) {

            final GeoPoint geoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());

            final Document.Builder docBuilder = Document.newBuilder();
            docBuilder.addField(Field.newBuilder().setName(field_keywords).setText(keywords));
            docBuilder.addField(Field.newBuilder().setName(field_location).setGeoPoint(geoPoint));

            getTestIdx().add(docBuilder.build());
        }
    }

    @Override
    public ArrayList<String> searchLocations(final String keyword, final String lat, final String lng, final String dist) {
        final ArrayList<String> locations = new ArrayList<String>();

        String queryKeyword = "";
        if (!keyword.isEmpty()) {
            queryKeyword = field_keywords + ":\"" + keyword + "\" AND ";
        }

        final String queryTemplate = "distance(%s, geopoint(%s, %s)) < %s";
        final String query = String.format(queryKeyword + queryTemplate, field_location, lat, lng, dist);

        final Results<ScoredDocument> results = getTestIdx().search(query);

        for (final ScoredDocument doc : results) {
            locations.add(doc.getOnlyField(field_keywords).getText());
        }

        return locations;
    }

    @Override
    public ArrayList<String> searchKeyword(final String keyword) {
        final ArrayList<String> results = new ArrayList<String>();

        final String query = field_keywords + ":\"" + keyword + "\"";
        final Results<ScoredDocument> docs = getTestIdx().search(query);

        for (final ScoredDocument doc : docs) {
            results.add(doc.getOnlyField(field_keywords).getText());
        }

        return results;
    }

    @Override
    public void putComment(final String author, final String body, final String labels) {

        final Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setBody(body);
        comment.setLength(body.length());
        comment.setBody(body);
        comment.setLabels(labels);
        dao.ofy().put(comment);

        //        datastoreService.put(entity);
    }

    @Override
    public void inviteToChat(final String bareJid) {

        final JID jid = new JID(bareJid);

        final XMPPService xmppService = XMPPServiceFactory.getXMPPService();
        xmppService.sendInvitation(jid);
    }

    @Override
    public String sendChatMessage(final String text, final String selectedChatUserFullJid) {
        final JID jid = new JID(selectedChatUserFullJid);

        final Message message = new MessageBuilder() //
        .withRecipientJids(jid) //
        .withBody(text) //
        .build();

        final XMPPService xmppService = XMPPServiceFactory.getXMPPService();

        if (xmppService.getPresence(jid).isAvailable()) {
            final SendResponse sendResponse = xmppService.sendMessage(message);

            if (sendResponse.getStatusMap().get(jid) == SendResponse.Status.SUCCESS) {
                return "OK";
            } else {
                return "NOT OK";
            }

        } else {
            return "Unavailable";
        }
    }

    @Override
    public ArrayList<XmppUser> fetchAllUsers() {

        final QueryResultIterable<XmppUser> queryResult = dao.ofy().query(XmppUser.class).fetch();
        final QueryResultIterator<XmppUser> iterator = queryResult.iterator();

        final ArrayList<XmppUser> users = new ArrayList<XmppUser>();
        while (iterator.hasNext()) {
            users.add(iterator.next());
        }

        return users;
    }

    private final ProspectiveSearchService prospectiveSearchService = ProspectiveSearchServiceFactory.getProspectiveSearchService();

    // subscribe, unsubscribe, match

    @Override
    public void subscribeProspectiveSearchOnXmppMessages(final String topic, final String subscriptionId, final String query) {

        final HashMap<String, FieldType> schema = new HashMap<String, FieldType>();
        schema.put("body", FieldType.STRING);

        //        String topic = "Person";
        //        String subscriptionId = "SearchPersonByName";
        //        long leaseTimeInMilliseconds = 24 * 60 * 60 * 1000;
        //        String query = "name:Adriaan";
        //
        prospectiveSearchService.subscribe(topic, subscriptionId, //
                0, query, schema);
    }

    @Override
    public ArrayList<String> fetchTopics() {
        return new ArrayList<String>(prospectiveSearchService.listTopics("", 1000));
    }

    @Override
    public ArrayList<PguSubscription> fetchSubscriptions(final String topic) {
        final ArrayList<PguSubscription> pguSubs = new ArrayList<PguSubscription>();

        final List<Subscription> subscriptions = prospectiveSearchService.listSubscriptions(topic);
        for (final Subscription sub : subscriptions) {

            final PguSubscription pguSub = new PguSubscription();
            pguSub.setId(sub.getId());
            pguSub.setQuery(sub.getQuery());

            final State state = sub.getState();
            pguSub.setState(state == null ? "" : state.toString());

            pguSub.setFullDescription(sub.toString());
            pguSubs.add(pguSub);
        }

        return pguSubs;
    }

    @Override
    public void unsubscribeFromProspectiveSearch(final String topic, final String subId) {
        prospectiveSearchService.unsubscribe(topic, subId);
    }





























}
