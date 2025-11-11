package com.qlst.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.qlst.dao.UserDAO;
import com.qlst.model.User;
import com.qlst.util.PasswordUtil;

@WebServlet(name = "AuthServlet", urlPatterns = "/login")
public class AuthServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            User existing = (User) session.getAttribute("authUser");
            if (existing != null) {
                resp.sendRedirect(req.getContextPath() + landingPath(existing));
                return;
            }
        }

        String next = req.getParameter("next");
        if (next != null && !next.isBlank()) {
            req.setAttribute("next", next);
        }

        if ("1".equals(req.getParameter("registered"))) {
            req.setAttribute("message", "Đăng ký thành công, vui lòng đăng nhập.");
        }
        if ("1".equals(req.getParameter("logout"))) {
            req.setAttribute("message", "Bạn đã đăng xuất.");
        }

        req.getRequestDispatcher("/WEB-INF/views/auth/Login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String next = req.getParameter("next");

        if (next != null && !next.isBlank()) {
            req.setAttribute("next", next);
        }

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            if (username != null) {
                req.setAttribute("username", username.trim());
            }
            req.getRequestDispatcher("/WEB-INF/views/auth/Login.jsp").forward(req, resp);
            return;
        }

        try {
            String normalizedUsername = username.trim();
            Optional<User> userOpt = userDAO.findByUsername(normalizedUsername);
            if (userOpt.isEmpty()) {
                req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                req.setAttribute("username", normalizedUsername);
                req.getRequestDispatcher("/WEB-INF/views/auth/Login.jsp").forward(req, resp);
                return;
            }

            User user = userOpt.get();
            String storedHash = user.getPasswordHash();
            boolean authenticated = false;
            if (storedHash != null) {
                if (storedHash.contains(":")) {
                    authenticated = PasswordUtil.verifyPassword(password, storedHash);
                } else {
                    authenticated = password.equals(storedHash.trim());
                }
            }

            if (!authenticated) {
                req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                req.setAttribute("username", normalizedUsername);
                req.getRequestDispatcher("/WEB-INF/views/auth/Login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("authUser", user);
            String redirectPath = resolveRedirect(next, user, req.getContextPath());
            resp.sendRedirect(redirectPath);
        } catch (SQLException e) {
            throw new ServletException("Không thể đăng nhập lúc này", e);
        }
    }

    private String landingPath(User user) {
        if (user.isAdmin()) {
            return "/main";
        }
        return "/customer/home";
    }

    private String resolveRedirect(String next, User user, String contextPath) {
        if (next != null && !next.isBlank()) {
            String trimmed = next.trim();
            if (trimmed.startsWith("/") && !trimmed.startsWith("//")) {
                // Ngăn người dùng admin truy cập vùng khách hàng sau đăng nhập
                if (user.isAdmin() && trimmed.startsWith("/customer")) {
                    return contextPath + landingPath(user);
                }
                return contextPath + trimmed;
            }
        }
        return contextPath + landingPath(user);
    }
}
