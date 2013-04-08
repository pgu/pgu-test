package com.pgu.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pgu.shared.PguSubscription;
import com.pgu.shared.XmppUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
    String greetServer(String name) throws IllegalArgumentException;

    void createBooks();

    void fetchBooks();

    void deleteBooks();

    void importProfileJson();

    String searchProfile(String text);

    String importGeopoints();

    ArrayList<String> searchLocations(String keyword, String lat, String lng, String dist);

    ArrayList<String> searchKeyword(String keyword);

    void putComment(String author, String body, String labels);

    String sendChatMessage(String message, String selectedChatUserFullJid);

    void inviteToChat(String text);

    ArrayList<XmppUser> fetchAllUsers();

    void subscribeProspectiveSearchOnBodyField(String topic, String subscriptionId, String query);

    ArrayList<String> fetchTopics();

    ArrayList<PguSubscription> fetchSubscriptions(String topic);

    void unsubscribeFromProspectiveSearch(String topic, String subId);

    String askChannelToken();
}
