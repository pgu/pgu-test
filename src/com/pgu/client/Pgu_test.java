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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pgu.client.ui.MyViewImpl;

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

        RootPanel.get().add(selection);
        RootPanel.get().add(listBox);

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


        final VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(5);
        vp.add(view);
        vp.add(testViewBtn);
        // -----------------
        vp.add(testFnBtn);
        // -----------------
        vp.add(btnImportLocations);
        vp.add(importLocationsResultBox);
        vp.add(new Label("Keyword"));
        vp.add(keywordBox);
        vp.add(new Label("Lat"));
        vp.add(latitudeBox);
        vp.add(new Label("Lng"));
        vp.add(longitudeBox);
        vp.add(new Label("Dist (m)"));
        vp.add(distanceBox);
        vp.add(searchLocations);
        vp.add(searchKeywords);
        vp.add(resultSearchLocations);
        vp.add(resultSearchKeywords);
        // -----------------
        vp.add(btnImportJson);
        vp.add(importResultBox);
        vp.add(searchBox);
        vp.add(btnSearch);
        vp.add(searchResultBox);
        vp.add(btnCreateBooks);
        vp.add(btnFetchBooks);
        vp.add(btnDeleteBooks);
        vp.add(cellBrowser);
        vp.add(btnTestJSNI);
        vp.add(testJsniBox);
        RootPanel.get().add(vp);

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

    public static native void modifyView(MyViewImpl view) /*-{
        view.@com.pgu.client.ui.MyViewImpl::setText(Ljava/lang/String;)('!! TOTO !!');
    }-*/;

}
