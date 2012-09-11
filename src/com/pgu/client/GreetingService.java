package com.pgu.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
}
