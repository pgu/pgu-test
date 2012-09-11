package com.pgu.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

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
}
