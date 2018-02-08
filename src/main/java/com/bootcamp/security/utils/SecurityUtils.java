/**
 * Copyright (c) 2017, AskLytics and/or its affiliates. All rights reserved.
 * <p>
 * ASKLYTICS PROPRIETARY/CONFIDENTIAL. Use is subject to Non-Disclosure Agreement.
 * <p>
 * Created by bilalshah on 04/03/2017
 */
package com.bootcamp.security.utils;

import com.bootcamp.security.PagSecurityUser;
import org.springframework.security.core.Authentication;

public class SecurityUtils {

    public static Integer getUserId(Authentication authentication){
        PagSecurityUser asklyticsUser = (PagSecurityUser) authentication.getPrincipal();
        return asklyticsUser.getUserId();
    }

}
