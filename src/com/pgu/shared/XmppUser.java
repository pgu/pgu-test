package com.pgu.shared;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;

public class XmppUser implements IsSerializable {

    // fullJid <-> node@domain/resource
    // a node (such as the name of a user of a chat service),
    // a domain (the domain name of the service), and
    // an optional resource

    @Id
    private String fullJid;
    private String subscriptionStatus;
    private String presenceStatus;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (fullJid == null ? 0 : fullJid.hashCode());
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
        final XmppUser other = (XmppUser) obj;
        if (fullJid == null) {
            if (other.fullJid != null) {
                return false;
            }
        } else if (!fullJid.equals(other.fullJid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserXmpp [jid=" + fullJid + "]";
    }

    public String getFullJid() {
        return fullJid;
    }

    public void setFullJid(final String fullJid) {
        this.fullJid = fullJid;
    }

    public void setSubscriptionStatus(final String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setPresenceStatus(final String presenceStatus) {
        this.presenceStatus = presenceStatus;
    }

    public String getPresenceStatus() {
        return presenceStatus;
    }

}
