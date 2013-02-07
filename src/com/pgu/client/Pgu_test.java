package com.pgu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pgu_test implements EntryPoint {

    private final GreetingServiceAsync greetingService  = GWT.create(GreetingService.class);

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {

        final DisclosurePanel dp1 = new DisclosurePanel("test A");
        dp1.setAnimationEnabled(true);
        dp1.setContent(new TestA().buildUI());

        final DisclosurePanel dp2 = new DisclosurePanel("test B");
        dp2.setAnimationEnabled(true);
        dp2.setContent(new TestB().buildUI());

        final VerticalPanel vp3 = new TestChat().buildUI();
        final VerticalPanel vp4 = new TestAdminProspectiveSearches().buildUI();

        final HorizontalPanel hp = new HorizontalPanel();
        hp.add(dp1);
        hp.add(dp2);
        hp.add(vp3);
        hp.add(vp4);
        RootPanel.get().add(hp);

    }

    public native void console(String msg) /*-{
        $wnd.console.log(msg);
    }-*/;

}
