package com.example.demo.entity;

import javax.servlet.http.HttpServletRequest;

public class Utility {
    public static String getSiteUrl(HttpServletRequest request){
        String siteUrl = request.getRequestURI().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}
