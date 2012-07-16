package com.pgu.server;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;
import com.pgu.shared.Book;

public class DAO extends DAOBase {

    static {
        ObjectifyService.register(Book.class);
    }

}
