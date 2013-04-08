package com.pgu.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TestCreateComment {

    private final GreetingServiceAsync greetingService  = GWT.create(GreetingService.class);

    public VerticalPanel buildUI() {

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
                if ("".equals(authorBox.getText()) //
                        || "".equals(bodyBox.getText()) //
                        || "".equals(labelsBox.getText()) //
                        ) {
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

        return vp2;
    }

    public native void console(String msg) /*-{
        $wnd.console.log(msg);
    }-*/;

}
