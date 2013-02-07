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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.pgu.shared.XmppUser;

public class TestChat {

    private final GreetingServiceAsync greetingService  = GWT.create(GreetingService.class);

    private final HashMap<XmppUser, RadioButton> user2radioButton = new HashMap<XmppUser, RadioButton>();

    // invite a user
    final Label addresseeLabel = new Label("Addressee");
    final TextBox addresseeBox = new TextBox();
    final Button inviteToChatBtn = new Button("Invite to chat");

    // send a chat message
    final Label chatMessageLabel = new Label("Chat message");
    final TextBox chatMessageBox = new TextBox();
    final Button sendMessageToChatBtn = new Button("Send");
    final Label resultSendMessageLabel = new Label("");

    // panel of chat users
    final HTMLPanel chatUsersPanel = new HTMLPanel("");
    final Button refreshChatsUserBtn = new Button("Refresh");

    public VerticalPanel buildUI() {

        inviteAction();
        refreshChatUsersAction();
        sendMessageToOneUserAction();

        // -----------------
        final VerticalPanel vp3 = new VerticalPanel();
        vp3.setSpacing(5);
        // -----------------

        // invite a user to chat with the webapp pgu-test
        vp3.add(addresseeLabel);
        vp3.add(addresseeBox);
        vp3.add(inviteToChatBtn);

        // send message to a user from webapp pgu-test
        vp3.add(chatMessageLabel);
        vp3.add(chatMessageBox);
        vp3.add(sendMessageToChatBtn);
        vp3.add(resultSendMessageLabel);

        // chat users (selection, user jid, user presence status, subscription status)
        vp3.add(refreshChatsUserBtn);
        vp3.add(chatUsersPanel);

        return vp3;
    }

    public native void console(String msg) /*-{
        $wnd.console.log(msg);
    }-*/;

    private void inviteAction() {
        inviteToChatBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                final String jid = addresseeBox.getText();
                if ("".equals(jid)) {
                    return;
                }

                greetingService.inviteToChat(jid //
                        , new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert("failure");
                    }

                    @Override
                    public void onSuccess(final Void result) {
                        Window.alert("Invitation has been successfully sent");
                    }
                });
            }
        });
    }

    private void refreshChatUsersAction() {
        refreshChatsUserBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                greetingService.fetchAllUsers(new AsyncCallback<ArrayList<XmppUser>>() {

                    @Override
                    public void onFailure(final Throwable caught) {
                        Window.alert("failure");
                    }

                    @Override
                    public void onSuccess(final ArrayList<XmppUser> result) {
                        user2radioButton.clear();
                        chatUsersPanel.clear();

                        for (final XmppUser xmppUser : result) {

                            final RadioButton radioButton = new RadioButton("xmpp_user");
                            user2radioButton.put(xmppUser, radioButton);

                            final Label fullJidLabel = new Label(xmppUser.getFullJid());
                            final Label presStatusLabel = new Label(xmppUser.getPresenceStatus());
                            final Label subsStatusLabel = new Label(xmppUser.getSubscriptionStatus());

                            final HorizontalPanel hp = new HorizontalPanel();
                            hp.setSpacing(10);

                            hp.add(radioButton);
                            hp.add(fullJidLabel);
                            hp.add(presStatusLabel);
                            hp.add(subsStatusLabel);
                            chatUsersPanel.add(hp);
                        }
                    }
                });
            }
        });
    }

    private void sendMessageToOneUserAction() {
        sendMessageToChatBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                resultSendMessageLabel.setText("");

                if ("".equals(chatMessageBox.getText())) {
                    return;
                }

                XmppUser selectedUser = null;
                for (final Entry<XmppUser, RadioButton> e : user2radioButton.entrySet()) {
                    if (e.getValue().getValue()) {
                        selectedUser = e.getKey();
                        break;
                    }
                }

                if (selectedUser == null) {
                    return;
                }

                final String selectedChatUserFullJid = selectedUser.getFullJid();

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
    }

}
