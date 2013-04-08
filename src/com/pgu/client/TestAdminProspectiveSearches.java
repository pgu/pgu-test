package com.pgu.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pgu.shared.PguSubscription;

public class TestAdminProspectiveSearches {

    private final GreetingServiceAsync greetingService  = GWT.create(GreetingService.class);

    private final HashMap<String, RadioButton> topic2radioBtn = new HashMap<String, RadioButton>();
    private final HashMap<PguSubscription, RadioButton> subscription2radioBtn = new HashMap<PguSubscription, RadioButton>();

    final Label topicLabel = new Label("topic");
    final TextBox topicBox = new TextBox();

    final Label subscriptionIdLabel = new Label("subscription id");
    final TextBox subscriptionIdBox = new TextBox();

    final Label queryLabel = new Label("query");
    final TextBox queryBox = new TextBox();

    final Button subscribeProspectiveSearchBtn = new Button("Subscribe on body field");
    final Button unsubscribeProspectiveSearchBtn = new Button("Unsubscribe");

    final Button refreshTopicsBtn = new Button("Refresh Topics");
    final Button refreshSubscriptionsBtn = new Button("Refresh Subscriptions");

    final FlowPanel topicsPanel = new FlowPanel();
    final FlowPanel subscriptionsPanel = new FlowPanel();

    final FlowPanel liveResultsPanel = new FlowPanel();

    public VerticalPanel buildUI() {

        subscribeProspectiveSearchAction();
        refreshSubscriptionsAction();
        refreshTopicsAction();
        unsubscribeProspectiveSearchAction();

        final VerticalPanel vp4 = new VerticalPanel();
        vp4.setSpacing(5);
        // -----------------
        // creation of a prospective search
        vp4.add(topicLabel);
        vp4.add(topicBox);
        vp4.add(subscriptionIdLabel);
        vp4.add(subscriptionIdBox);
        vp4.add(queryLabel);
        vp4.add(queryBox);
        vp4.add(subscribeProspectiveSearchBtn);

        // list topics
        vp4.add(refreshTopicsBtn);
        vp4.add(topicsPanel);

        // list subscriptions
        vp4.add(refreshSubscriptionsBtn);
        vp4.add(subscriptionsPanel);

        // unsubscribe
        vp4.add(unsubscribeProspectiveSearchBtn);

        // live results
        vp4.add(liveResultsPanel);

        return vp4;
    }

    private void unsubscribeProspectiveSearchAction() {
        unsubscribeProspectiveSearchBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {

                final String topic = getSelectedTopic();
                String subId = null;

                for (final Entry<PguSubscription, RadioButton> e : subscription2radioBtn.entrySet()) {
                    if (e.getValue().getValue()) {
                        subId = e.getKey().getId();
                        break;
                    }
                }

                if (topic == null //
                        || subId == null) {
                    return;
                }

                greetingService.unsubscribeFromProspectiveSearch( //
                        topic //
                        , subId //
                        , new AsyncCallback<Void>() {

                            @Override
                            public void onFailure(final Throwable caught) {
                                Window.alert("failure");
                            }

                            @Override
                            public void onSuccess(final Void result) {
                                Window.alert("Unsubscribe successful");
                            }

                        });
            }
        });
    }

    private void refreshTopicsAction() {
        refreshTopicsBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                greetingService.fetchTopics(new AsyncCallback<ArrayList<String>>(){

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert("failure");
                    }

                    @Override
                    public void onSuccess(final ArrayList<String> topics) {
                        subscription2radioBtn.clear();
                        topic2radioBtn.clear();

                        subscriptionsPanel.clear();
                        topicsPanel.clear();

                        for (final String topic : topics) {

                            final RadioButton topicRadio = new RadioButton("topic");

                            topic2radioBtn.put(topic, topicRadio);

                            final HorizontalPanel hp = new HorizontalPanel();
                            hp.setSpacing(10);

                            hp.add(topicRadio);
                            hp.add(new Label(topic));
                            topicsPanel.add(hp);
                        }
                    }

                });
            }
        });
    }

    private void refreshSubscriptionsAction() {
        refreshSubscriptionsBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {

                final String topic = getSelectedTopic();

                if (topic == null) {
                    return;
                }

                greetingService.fetchSubscriptions(topic, new AsyncCallback<ArrayList<PguSubscription>>(){

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert("failure");
                    }

                    @Override
                    public void onSuccess(final ArrayList<PguSubscription> result) {
                        subscription2radioBtn.clear();
                        subscriptionsPanel.clear();

                        for (final PguSubscription pguSub : result) {

                            final RadioButton subRadio = new RadioButton("subscription");

                            subscription2radioBtn.put(pguSub, subRadio);

                            final HorizontalPanel hp = new HorizontalPanel();
                            hp.setSpacing(10);

                            hp.add(subRadio);
                            hp.add(new Label(pguSub.getId()));
                            hp.add(new Label(pguSub.getQuery()));
                            hp.add(new Label(pguSub.getState()));
                            hp.add(new Label(pguSub.getFullDescription()));
                            subscriptionsPanel.add(hp);
                        }

                    }

                });
            }

        });
    }

    private String getSelectedTopic() {

        for (final Entry<String, RadioButton> e : topic2radioBtn.entrySet()) {
            if (e.getValue().getValue()) {
                return e.getKey();
            }
        }
        return null;
    }

    private void subscribeProspectiveSearchAction() {
        subscribeProspectiveSearchBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                final String topic = topicBox.getText();
                final String subscriptionId = subscriptionIdBox.getText();
                final String query = queryBox.getText();

                if ("".equals(topic) //
                        || "".equals(subscriptionId) //
                        || "".equals(query) //
                        ) {
                    return;
                }

                greetingService.subscribeProspectiveSearchOnBodyField(
                        topic //
                        , subscriptionId //
                        , query //
                        , new AsyncCallback<Void>() {

                            @Override
                            public void onFailure(final Throwable caught) {
                                Window.alert("failure");
                            }

                            @Override
                            public void onSuccess(final Void result) {
                                console("create prospective search: success");
                            }

                        });
            }
        });
    }

    public native void console(String msg) /*-{
        $wnd.console.log(msg);
    }-*/;

    public void displayUpdate(final String body) {
        final Label reply = new Label(body);

        if (liveResultsPanel.getWidgetCount() == 0) {
            liveResultsPanel.add(reply);

        } else {
            liveResultsPanel.insert(reply, 0);
        }

    }

}
