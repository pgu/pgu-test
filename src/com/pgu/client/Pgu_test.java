package com.pgu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pgu_test implements EntryPoint {

    private final GreetingServiceAsync greetingService  = GWT.create(GreetingService.class);

    private static String channelToken = null;

    public static String getChannelToken() {
        return channelToken;
    }

    private native void exportJS() /*-{
        $wnd.pgu_geo_channel_on_message = $entry(@com.pgu.client.Pgu_test::onMessageFromChannel(Ljava/lang/String;Ljava/lang/String;));
    }-*/;


    public static void onMessageFromChannel(final String type, final String body) {
        if ("chat".equalsIgnoreCase(type)) {
            static_self.testChat.displayReply(body);

        } else if ("search".equalsIgnoreCase(type)) {
            static_self.testAdminProspectiveSearches.displayUpdate(body);

        } else {
            throw new IllegalArgumentException("Unknown type: " + type + ", " + body);
        }
    }

    private static Pgu_test static_self;
    private TestAdminProspectiveSearches testAdminProspectiveSearches;
    private TestChat testChat ;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {

        static_self = this;
        exportJS();

        final DisclosurePanel dp1 = new DisclosurePanel("test Others");
        dp1.setAnimationEnabled(true);
        dp1.setContent(new TestA().buildUI());

        final DisclosurePanel dp2 = new DisclosurePanel("test Comment");
        dp2.setAnimationEnabled(true);
        dp2.setContent(new TestCreateComment().buildUI());

        testChat = new TestChat();
        final DisclosurePanel dp3 = new DisclosurePanel("test Chat");
        dp3.setAnimationEnabled(true);
        dp3.setContent(testChat.buildUI());

        testAdminProspectiveSearches = new TestAdminProspectiveSearches();
        final DisclosurePanel dp4 = new DisclosurePanel("test Prospective search");
        dp4.setAnimationEnabled(true);
        dp4.setContent(testAdminProspectiveSearches.buildUI());

        final HorizontalPanel hp = new HorizontalPanel();
        hp.add(dp1);
        hp.add(dp2);
        hp.add(dp3);
        hp.add(dp4);
        RootPanel.get().add(hp);

        greetingService.askChannelToken(
                new AsyncCallback<String>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert("failure");
                    }

                    @Override
                    public void onSuccess(final String token) {
                        channelToken = token;
                        openChannel(token);
                    }


                });
    }

    private native void openChannel(String token) /*-{
        $wnd.pgu_test_open_channel(token);
    }-*/;

    public native void console(String msg) /*-{
        $wnd.console.log(msg);
    }-*/;

}
