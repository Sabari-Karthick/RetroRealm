package com.Batman.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class PathChecker {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${allowed.paths}")
    private String[] allowedPaths;

    public boolean isAllowed(String requestPath) {
        for (String pattern : allowedPaths) {
            if (pathMatcher.match(pattern, requestPath)) {
                return true; 
            }
        }
        return false; 
    }
}
