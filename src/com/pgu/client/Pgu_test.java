package com.pgu.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pgu.client.ui.MyViewImpl;
import com.pgu.shared.XmppUser;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pgu_test implements EntryPoint {
    private final GreetingServiceAsync greetingService  = GWT.create(GreetingService.class);
    private boolean                    isCtrlSAvailable = true;

    private final ListBox              listBox          = new ListBox();
    private final Label                selection        = new Label();

    /**
     * This is the entry point method.
     */
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

        listBox.addItem("dummy", (String) null);
        listBox.addItem("item0");
        listBox.addItem("item1");

        listBox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(final ChangeEvent event) {
                final String value = listBox.getValue(listBox.getSelectedIndex());
                selection.setText(value + ", isNull? " + value.equals("null"));
            }
        });

        final CellBrowser cellBrowser = new CellBrowser(new MyTreeViewModel(), null);
        cellBrowser.setWidth("300px");
        cellBrowser.setHeight("300px");
        cellBrowser.setAnimationEnabled(true);

        final Button btnImportJson = new Button("Import json");
        final HTML importResultBox = new HTML();
        final TextBox searchBox = new TextBox();
        final Button btnSearch = new Button("Search");
        final HTML searchResultBox = new HTML();

        btnImportJson.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                greetingService.importProfileJson(new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        importResultBox.setHTML("KO<br/>" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(final Void result) {
                        importResultBox.setHTML("OK");
                    }
                });
            }
        });

        btnSearch.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {

                if ("".equals(searchBox.getText().trim())) {
                    return;
                }

                greetingService.searchProfile(searchBox.getText(), new AsyncCallback<String>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        searchResultBox.setHTML("KO<br/>" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(final String result) {
                        searchResultBox.setHTML(result);
                    }

                });
            }
        });

        final Button btnImportLocations = new Button("Import geopoints");
        final HTML importLocationsResultBox = new HTML();

        btnImportLocations.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                greetingService.importGeopoints(new AsyncCallback<String>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        importLocationsResultBox.setHTML("KO<br/>" + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(final String result) {
                        importLocationsResultBox.setHTML(result);
                    }

                });
            }
        });

        final TextBox keywordBox = new TextBox();
        final TextBox latitudeBox = new TextBox();
        final TextBox longitudeBox = new TextBox();
        final TextBox distanceBox = new TextBox();
        final Button searchLocations = new Button("Search locations");
        final Button searchKeywords = new Button("Search keywords");
        final HTML resultSearchLocations = new HTML();
        final HTML resultSearchKeywords = new HTML();

        searchLocations.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                final String keyword = keywordBox.getText().trim();
                final String lat = latitudeBox.getText().trim();
                final String lng = longitudeBox.getText().trim();
                final String dist = distanceBox.getText().trim();

                if (!lat.isEmpty() //
                        && !lng.isEmpty() //
                        && !dist.isEmpty() //
                        ) {

                    resultSearchLocations.setHTML("");

                    greetingService.searchLocations(keyword, lat, lng, dist, new AsyncCallback<ArrayList<String>>() {

                        @Override
                        public void onFailure(final Throwable caught) {
                            resultSearchLocations.setHTML("KO<br/>" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(final ArrayList<String> result) {
                            final StringBuilder sb = new StringBuilder();
                            for (final String string : result) {
                                sb.append(string);
                                sb.append("<br/>");
                            }
                            resultSearchLocations.setHTML(sb.toString());
                        }

                    });
                }
            }
        });

        searchKeywords.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {

                final String keyword = keywordBox.getText().trim();
                if (!keyword.isEmpty()) {

                    resultSearchKeywords.setHTML("");

                    greetingService.searchKeyword(keyword, new AsyncCallback<ArrayList<String>>() {

                        @Override
                        public void onFailure(final Throwable caught) {
                            resultSearchKeywords.setHTML("KO<br/>" + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(final ArrayList<String> result) {
                            final StringBuilder sb = new StringBuilder();
                            for (final String string : result) {
                                sb.append(string);
                                sb.append("<br/>");
                            }
                            resultSearchKeywords.setHTML(sb.toString());
                        }

                    });
                }
            }

        });

        final Button btnTestJSNI = new Button("test jsni");
        final Label testJsniBox = new Label();

        btnTestJSNI.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                testJsniBox.setText("");

                final String test = "{\"userId\":\"123465\", \"friends\":[{\"name\":\"toto\",\"color\":\"red\"},{\"name\":\"tata\",\"color\":\"blue\"},{\"name\":\"titi\",\"color\":\"green\"}]}";
                final String colorsHTML = showColors(Pgu_test.this, test);
                testJsniBox.setText(colorsHTML);
            }
        });

        final Button testFnBtn = new Button("Test fn");
        testFnBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                final JavaScriptObject fn = giveMeAFunction();
                useThisFunction(fn);

            }
        });

        final MyViewImpl view = new MyViewImpl();
        final Button testViewBtn = new Button("test view");
        testViewBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                modifyView(view);
            }
        });

        // ----------------- form for prospective search
        final Label authorLabel = new Label("author");
        final TextBox authorBox = new TextBox();

        final Label bodyLabel = new Label("body");
        final TextBox bodyBox = new TextBox();

        final Label labelsLabel = new Label("labels");
        final TextBox labelsBox = new TextBox();

        final Button commentSaveBtn = new Button("Save");

        commentSaveBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                if ("".equals(authorBox.getText())) {
                    return;
                }

                greetingService.putComment( //
                        authorBox.getValue() //
                        , bodyBox.getValue() //
                        , labelsBox.getValue() //
                        , new AsyncCallback<Void>() {

                            @Override
                            public void onFailure(final Throwable caught) {
                                Window.alert("failure");
                            }

                            @Override
                            public void onSuccess(final Void result) {
                                console("success");
                            }
                        });
            }
        });

        // -----------------
        final ListBox chatUsersList = new ListBox();

        final Label addresseeLabel = new Label("Addressee");
        final TextBox addresseeBox = new TextBox();
        final Button inviteToChatBtn = new Button("Invite to chat");

        final Button refreshChatsUserBtn = new Button("Refresh");

        inviteToChatBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                if ("".equals(addresseeBox.getText())) {
                    return;
                }

                greetingService.inviteToChat(addresseeBox.getText() //
                        , new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert("failure");
                    }

                    @Override
                    public void onSuccess(final Void result) {
                        console("invitation is sent");
                        chatUsersList.setEnabled(false);
                        greetingService.fetchAllConnectedUsers(new AsyncCallback<ArrayList<XmppUser>>() {

                            @Override
                            public void onFailure(final Throwable caught) {
                                chatUsersList.setEnabled(true);
                                Window.alert("failure");
                            }

                            @Override
                            public void onSuccess(final ArrayList<XmppUser> result) {
                                chatUsersList.clear();

                                for (final XmppUser xmppUser : result) {
                                    chatUsersList.addItem(xmppUser.getBareJid(), xmppUser.getFullJid());
                                }
                                chatUsersList.setEnabled(true);
                            }

                        });
                    }

                });
            }
        });

        refreshChatsUserBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                greetingService.fetchAllConnectedUsers(new AsyncCallback<ArrayList<XmppUser>>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert("failure");
                    }

                    @Override
                    public void onSuccess(final ArrayList<XmppUser> result) {
                        chatUsersList.clear();

                        for (final XmppUser xmppUser : result) {
                            chatUsersList.addItem(xmppUser.getBareJid(), xmppUser.getFullJid());
                        }
                    }
                });
            }
        });

        final Label chatMessageLabel = new Label("Chat message");
        final TextBox chatMessageBox = new TextBox();
        final Button sendMessageToChatBtn = new Button("Send");
        final Label resultSendMessageLabel = new Label("");

        sendMessageToChatBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                resultSendMessageLabel.setText("");

                if ("".equals(chatMessageBox.getText())) {
                    return;
                }

                final int selectedIndex = chatUsersList.getSelectedIndex();
                if (selectedIndex == -1) {
                    return;
                }

                final String selectedChatUserFullJid = chatUsersList.getValue(selectedIndex);

                greetingService.sendChatMessage(chatMessageBox.getText() //
                        , selectedChatUserFullJid //
                        , new AsyncCallback<String>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert("failure");
                    }

                    @Override
                    public void onSuccess(final String result) {
                        console("message is sent " + result);
                        resultSendMessageLabel.setText(result);
                    }
                });
            }
        });

        // -----------------
        final VerticalPanel vp1 = new VerticalPanel();
        vp1.setSpacing(5);
        // -----------------
        vp1.add(selection);
        vp1.add(listBox);
        // -----------------
        vp1.add(view);
        vp1.add(testViewBtn);
        // -----------------
        vp1.add(testFnBtn);
        // -----------------
        vp1.add(btnImportLocations);
        vp1.add(importLocationsResultBox);
        vp1.add(new Label("Keyword"));
        vp1.add(keywordBox);
        vp1.add(new Label("Lat"));
        vp1.add(latitudeBox);
        vp1.add(new Label("Lng"));
        vp1.add(longitudeBox);
        vp1.add(new Label("Dist (m)"));
        vp1.add(distanceBox);
        vp1.add(searchLocations);
        vp1.add(searchKeywords);
        vp1.add(resultSearchLocations);
        vp1.add(resultSearchKeywords);
        // -----------------
        vp1.add(btnImportJson);
        vp1.add(importResultBox);
        vp1.add(searchBox);
        vp1.add(btnSearch);
        vp1.add(searchResultBox);
        vp1.add(btnCreateBooks);
        vp1.add(btnFetchBooks);
        vp1.add(btnDeleteBooks);
        vp1.add(cellBrowser);
        vp1.add(btnTestJSNI);
        vp1.add(testJsniBox);

        // -----------------
        final VerticalPanel vp2 = new VerticalPanel();
        vp2.setSpacing(5);
        // -----------------
        vp2.add(authorLabel);
        vp2.add(authorBox);

        vp2.add(bodyLabel);
        vp2.add(bodyBox);

        vp2.add(labelsLabel);
        vp2.add(labelsBox);

        vp2.add(commentSaveBtn);

        // -----------------
        final VerticalPanel vp3 = new VerticalPanel();
        vp3.setSpacing(5);
        // -----------------
        vp3.add(addresseeLabel);
        vp3.add(addresseeBox);
        vp3.add(inviteToChatBtn);

        vp3.add(chatMessageLabel);
        vp3.add(chatMessageBox);
        vp3.add(sendMessageToChatBtn);
        vp3.add(resultSendMessageLabel);

        vp3.add(refreshChatsUserBtn);
        vp3.add(chatUsersList);

        // -----------------
        final HorizontalPanel hp = new HorizontalPanel();
        hp.add(vp1);
        hp.add(vp2);
        hp.add(vp3);
        RootPanel.get().add(hp);

    }

    public native String showColors(Pgu_test view, String json) /*-{

		var obj = JSON.parse(json);
		$wnd.console.log('userId ' + obj.userId);

		var friends = obj.friends;

		return view.@com.pgu.client.Pgu_test::showColors(Lcom/google/gwt/core/client/JavaScriptObject;)(friends);

    }-*/;

    public native String showColors(JavaScriptObject friends) /*-{
		$wnd.console.log('friends ' + friends.length);

		var colors = [];

		for ( var i in friends) {
			var friend = friends[i];

			colors.push(friend.color);
			colors.push('<br/>');
		}

		return colors.join('');
    }-*/;

    public static native JavaScriptObject giveMeAFunction() /*-{
		var callback = function() {
			$wnd.console.log('Hello de LU');
		};

		return callback;
    }-*/;

    public static native void useThisFunction(JavaScriptObject fn) /*-{
		fn();
    }-*/;

    public native void modifyView(MyViewImpl view) /*-{
        view.@com.pgu.client.ui.MyViewImpl::setText(Ljava/lang/String;) //
        ('!! TOTO !!');

        this.@com.pgu.client.Pgu_test::console(Ljava/lang/String;)('toto');
    }-*/;

    public native void console(String msg) /*-{
        $wnd.console.log(msg);
    }-*/;

}
