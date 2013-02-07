package com.pgu.shared;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Comment implements IsSerializable {

    @Id
    private Long   id;
    private String author;
    private String body;
    private int length;
    private String labels;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Comment [id=" + id + ", author=" + author + ", body=" + body + ", length=" + length + ", labels="
                + labels + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(final String labels) {
        this.labels = labels;
    }

}
