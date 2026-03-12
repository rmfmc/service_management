package ruben.springboot.service_management.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ruben.springboot.service_management.authentication.dto.CurrentUserDto;

@Component
public class SecurityUtils {

    public static Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        Object details = auth.getDetails();
        if (!(details instanceof CurrentUserDto currentUser)) return null;

        return currentUser.getCurrentUserId();
    }

    public static String currentUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        Object details = auth.getDetails();
        if (!(details instanceof CurrentUserDto currentUser)) return null;

        return currentUser.getCurrentUserName();
    }

    public static String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? null : auth.getName();
    }
    
}
