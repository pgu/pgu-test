package com.pgu.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PguSubscription implements IsSerializable {

    private String   id;
    private String   query;
    private String   state;
    private String   fullDescription;

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
        final PguSubscription other = (PguSubscription) obj;
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
        return "PguSubscription [id=" + id + ", query=" + query + ", state=" + state + ", fullDescription="
                + fullDescription + "]";
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(final String fullDescription) {
        this.fullDescription = fullDescription;
    }

}
