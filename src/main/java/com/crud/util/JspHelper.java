package com.crud.util;

public class JspHelper {
    private static final String JSP_FORMAT = "/WEB_INF/jsp/%s.jsp";
    public static String getPath(String path) {
        return String.format("/WEB_INF/jsp/%s.jsp", path);
    }
}
