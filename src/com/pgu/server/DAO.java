package com.pgu.server;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;
import com.pgu.shared.Book;
import com.pgu.shared.UserXmpp;

public class DAO extends DAOBase {

    static {
        ObjectifyService.register(Book.class);
        ObjectifyService.register(UserXmpp.class);
    }

}
