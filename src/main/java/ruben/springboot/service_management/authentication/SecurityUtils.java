package ruben.springboot.service_management.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        Object details = auth.getDetails();
        if (details == null) return null;

        if (details instanceof Long) return (Long) details;
        if (details instanceof Integer) return ((Integer) details).longValue();
        if (details instanceof String) return Long.parseLong((String) details);

        return null;
    }

    public static String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? null : auth.getName();
    }
    
}
