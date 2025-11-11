package com.qlst.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.qlst.model.User;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/", "", "/index.jsp", "/login", "/register", "/logout"
    );
    private static final Set<String> CUSTOMER_PUBLIC_ENDPOINTS = Set.of(
        "/customer/products",
        "/customer/product"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String contextPath = req.getContextPath();
        String requestUri = req.getRequestURI();
        String path = requestUri.substring(contextPath.length());
        if (path.isEmpty()) {
            path = "/";
        }


        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("authUser");

        if (isPublic(path)) {
            chain.doFilter(req, resp);
            return;
        }

        if (isCustomerPublic(path)) {
            if (user != null && user.isAdmin()) {
                resp.sendRedirect(contextPath + "/main");
                return;
            }
            chain.doFilter(req, resp);
            return;
        }
        if (user == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        if (isAdminPath(path)) {
            if (!user.isAdmin()) {
                resp.sendRedirect(contextPath + "/customer/home");
                return;
            }
        } else if (isCustomerPath(path) && user.isAdmin()) {
            resp.sendRedirect(contextPath + "/main");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void destroy() {
        // no-op
    }

    private boolean isPublic(String path) {
        if (path.startsWith("/resources/") || path.startsWith("/favicon")) {
            return true;
        }
        if (path.startsWith("/login") || path.startsWith("/register")) {
            return true;
        }
        return PUBLIC_ENDPOINTS.contains(path);
    }

    private boolean isAdminPath(String path) {
        return path.startsWith("/main")
                || path.startsWith("/reports")
                || path.startsWith("/receipts");
    }

    private boolean isCustomerPath(String path) {
        return path.startsWith("/customer");
    }

    private boolean isCustomerPublic(String path) {
        return CUSTOMER_PUBLIC_ENDPOINTS.contains(path);
    }
}
