package be.deschutter.scimitar.security;

import be.deschutter.scimitar.user.ScimitarUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityHelperImpl implements SecurityHelper {

    @Override
    public ScimitarUser getLoggedInUser() {
        final Object principal = SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        if (principal instanceof ScimitarUser)
            return (ScimitarUser) principal;
        else
            throw new AccessDeniedException("Access denied for " + principal);
    }

    @Override
    public String getAnonymousUserName() {
        final Object principal = SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        if (principal instanceof String)
            return (String) principal;
        else
            throw new AccessDeniedException("Access denied for " + principal);
    }
}
