package com.grusker.publicmessageboard.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static String getLoginUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
