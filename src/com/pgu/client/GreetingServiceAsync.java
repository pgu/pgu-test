package com.pgu.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pgu.shared.PguSubscription;
import com.pgu.shared.XmppUser;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
    void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

    void createBooks(AsyncCallback<Void> asyncCallback);

    void fetchBooks(AsyncCallback<Void> asyncCallback);

    void deleteBooks(AsyncCallback<Void> asyncCallback);

    void importProfileJson(AsyncCallback<Void> asyncCallback);

    void searchProfile(String text, AsyncCallback<String> asyncCallback);

    void importGeopoints(AsyncCallback<String> asyncCallback);

    void searchLocations(String keyword, String lat, String lng, String dist,
            AsyncCallback<ArrayList<String>> asyncCallback);

    void searchKeyword(String keyword, AsyncCallback<ArrayList<String>> asyncCallback);

    void putComment(String author, String body, String labels, AsyncCallback<Void> asyncCallback);

    void sendChatMessage(String message, String selectedChatUserFullJid, AsyncCallback<String> asyncCallback);

    void inviteToChat(String text, AsyncCallback<Void> asyncCallback);

    void fetchAllUsers(AsyncCallback<ArrayList<XmppUser>> asyncCallback);

    void subscribeProspectiveSearchOnXmppMessages(String topic, String subscriptionId, String query,
            AsyncCallback<Void> asyncCallback);

    void fetchTopics(AsyncCallback<ArrayList<String>> asyncCallback);

    void fetchSubscriptions(String topic, AsyncCallback<ArrayList<PguSubscription>> asyncCallback);

    void unsubscribeFromProspectiveSearch(String topic, String subId, AsyncCallback<Void> asyncCallback);

}
