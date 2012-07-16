package com.pgu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pgu_test implements EntryPoint {
    private final GreetingServiceAsync greetingService  = GWT.create(GreetingService.class);
    private boolean                    isCtrlSAvailable = true;

    @Override
    public void onModuleLoad() {

        final Button btnCreateBooks = new Button("create 1000 books");
        final Button btnFetchBooks = new Button("fetch 1000 books");
        final Button btnDeleteBooks = new Button("delete all books");

        btnCreateBooks.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                greetingService.createBooks(new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(final Void result) {
                        Window.alert("successfully created");
                    }

                });
            }
        });

        btnFetchBooks.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                greetingService.fetchBooks(new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(final Void result) {
                        Window.alert("successfully fetched");
                    }

                });
            }
        });

        btnDeleteBooks.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                greetingService.deleteBooks(new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(final Void result) {
                        Window.alert("successfully deleted");
                    }

                });
            }
        });

        Event.addNativePreviewHandler(new NativePreviewHandler() {

            @Override
            public void onPreviewNativeEvent(final NativePreviewEvent event) {
                final NativeEvent ne = event.getNativeEvent();
                final int keyCode = ne.getKeyCode();

                if (ne.getCtrlKey() //
                        && ('s' == keyCode || 'S' == keyCode)) {

                    GWT.log("ctrl s");
                    ne.preventDefault();

                    if (isCtrlSAvailable) {
                        isCtrlSAvailable = false;
                        new Timer() {

                            @Override
                            public void run() {
                                isCtrlSAvailable = true;
                            }

                        }.schedule(500);

                        doSomethingOnCtrlS();
                    }
                }
            }

            private void doSomethingOnCtrlS() {
                GWT.log(" " + System.currentTimeMillis());
            }
        });

        final CellBrowser cellBrowser = new CellBrowser(new MyTreeViewModel(), null);
        cellBrowser.setWidth("300px");
        cellBrowser.setHeight("300px");
        cellBrowser.setAnimationEnabled(true);

        final VerticalPanel vp = new VerticalPanel();
        vp.add(btnCreateBooks);
        vp.add(btnFetchBooks);
        vp.add(btnDeleteBooks);
        vp.add(cellBrowser);
        RootPanel.get().add(vp);
    }
}
