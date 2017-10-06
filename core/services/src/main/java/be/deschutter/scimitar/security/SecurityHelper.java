package be.deschutter.scimitar.security;

import be.deschutter.scimitar.user.ScimitarUser;

public interface SecurityHelper {
    ScimitarUser getLoggedInUser();

    String getAnonymousUserName();
}
