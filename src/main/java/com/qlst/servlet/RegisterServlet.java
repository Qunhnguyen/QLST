package com.qlst.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qlst.dao.UserDAO;
import com.qlst.model.User;
import com.qlst.model.UserRole;
import com.qlst.util.PasswordUtil;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/Register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");
        String fullName = trim(req.getParameter("fullName"));
        String email = trim(req.getParameter("email"));
        String phone = trim(req.getParameter("phone"));
    String address = trim(req.getParameter("address"));

        List<String> errors = new ArrayList<>();
        if (username == null || username.length() < 4) {
            errors.add("Tên đăng nhập phải có ít nhất 4 ký tự.");
        }
        if (password == null || password.length() < 6) {
            errors.add("Mật khẩu phải có ít nhất 6 ký tự.");
        }
        if (password != null && !password.equals(confirm)) {
            errors.add("Xác nhận mật khẩu không khớp.");
        }

        try {
            if (errors.isEmpty() && userDAO.usernameExists(username)) {
                errors.add("Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
            }
        } catch (SQLException e) {
            throw new ServletException("Không thể kiểm tra tài khoản hiện có", e);
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("username", username);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.setAttribute("phone", phone);
            req.setAttribute("address", address);
            req.getRequestDispatcher("/WEB-INF/views/auth/Register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(UserRole.CUSTOMER);
        user.setPasswordHash(PasswordUtil.hashPassword(password));

        try {
            userDAO.insert(user);
        } catch (SQLException e) {
            throw new ServletException("Không thể tạo tài khoản lúc này", e);
        }

        resp.sendRedirect(req.getContextPath() + "/login?registered=1");
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
