package com.kkoz.sadogorod.security.method_security;

import com.kkoz.sadogorod.services.ServiceUzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
public class MethodAccessVoter implements AccessDecisionVoter {

    private final String POST   = "POST";
    private final String GET    = "GET";
    private final String PUT    = "PUT";
    private final String PATCH  = "PATCH";
    private final String DELETE = "DELETE";

    private final String[] openEndpoints = {

    };

    private final ServiceUzer serviceUzer;

    public MethodAccessVoter(ServiceUzer serviceUzer) {
        this.serviceUzer = serviceUzer;
    }


    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class aClass) {
        return true;
    }

    @Override
    public int vote(Authentication authentication,
                    Object object, Collection collection) {

        FilterInvocation filterInvocation = (FilterInvocation) object;
        String requestUrl    = filterInvocation.getRequestUrl();
        String requestMethod = filterInvocation.getRequest().getMethod();


        if (requestUrl.startsWith("/auth/")) {
            return 1;
        }

        if (checkForOpenEndpoint(requestUrl)) {
            return 1;
        }

        return 1;
    }

    private boolean checkForOpenEndpoint(String endpoint) {
        return Arrays.stream(openEndpoints).anyMatch(endpoint::contains);
    }
}
