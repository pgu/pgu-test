package com.pgu.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class MyTreeViewModel implements TreeViewModel {

    private final ListDataProvider<String> alphaDataProvider = new ListDataProvider<String>();

    public MyTreeViewModel() {
        for (char i = 'a'; i <= 'z'; i++) {
            alphaDataProvider.getList().add(Character.toString(i));
        }
    }

    public static class AlphaCell extends AbstractCell<String> {

        @Override
        public void render(final com.google.gwt.cell.client.Cell.Context context, final String value,
                final SafeHtmlBuilder sb) {
            sb.appendEscaped(value.toUpperCase());
        }

    }

    public static class AuthorCell extends AbstractCell<String> {

        @Override
        public void render(final com.google.gwt.cell.client.Cell.Context context, final String value,
                final SafeHtmlBuilder sb) {
            sb.appendEscaped(value);
        }

    }

    @Override
    public <T> NodeInfo<?> getNodeInfo(final T value) {
        if (null == value) {
            GWT.log("getnode info  null");
            return new DefaultNodeInfo<String>(alphaDataProvider, new AlphaCell());
        } else if (value instanceof String) {
            GWT.log("getnode info  string");
            final String str = (String) value;
            if (str.length() == 1) {

                final ListDataProvider<String> authors = new ListDataProvider<String>();
                authors.getList().add(value + " John");
                authors.getList().add(value + " Jane");
                return new DefaultNodeInfo<String>(authors, new AuthorCell());

            } else {
                // this is an author. nothing to do.
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean isLeaf(final Object value) {
        if (value != null && value instanceof String) {
            return ((String) value).length() > 1;
        }
        return false;
    }

}
