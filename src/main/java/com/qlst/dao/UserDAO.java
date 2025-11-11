package com.qlst.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import com.qlst.model.User;
import com.qlst.model.UserRole;

public class UserDAO {

    public Optional<User> findByUsername(String username) throws SQLException {
    String sql = "SELECT id, username, password, name, email, phone, address, role, created_at " +
             "FROM member WHERE username = ?";
        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    public boolean usernameExists(String username) throws SQLException {
    String sql = "SELECT 1 FROM member WHERE username = ?";
        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public User insert(User user) throws SQLException {
    String sql = "INSERT INTO member (username, password, name, email, phone, address, role, created_at) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
       try (Connection con = DAO.getConnection();
           PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole() == null ? UserRole.CUSTOMER.name() : user.getRole().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }
            user.setCreatedAt(LocalDateTime.now());
            return user;
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
    user.setPasswordHash(rs.getString("password"));
        user.setFullName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        String roleValue = rs.getString("role");
        if (roleValue != null && !roleValue.isBlank()) {
            user.setRole(UserRole.valueOf(roleValue));
        } else {
            user.setRole(UserRole.CUSTOMER);
        }
        Timestamp created = null;
        try {
            created = rs.getTimestamp("created_at");
        } catch (SQLException ignored) {
            // column may not exist; ignore
        }
        if (created != null) {
            user.setCreatedAt(created.toLocalDateTime());
        }
        return user;
    }
}
