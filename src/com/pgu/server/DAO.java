package com.pgu.server;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;
import com.pgu.shared.Book;
import com.pgu.shared.Comment;
import com.pgu.shared.XmppMessage;
import com.pgu.shared.XmppUser;

public class DAO extends DAOBase {

    static {
        ObjectifyService.register(Book.class);
        ObjectifyService.register(Comment.class);

        ObjectifyService.register(XmppUser.class);
        ObjectifyService.register(XmppMessage.class);
    }

}
