package com.qlst.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.qlst.model.Product;

public class ProductDAO {

    public List<Product> searchProducts(String keyword) throws SQLException {
        String baseSql = """
            SELECT
                p.id,
                p.name,
                p.brand,
                COALESCE(p.sale_price, 0) AS sale_price,
                COALESCE(p.stock_quantity, 0) AS stock_quantity,
                p.description,
                p.image_url
            FROM Product p
        """;
        String trimmedKeyword = null;
        boolean hasKeyword = false;
        if (keyword != null) {
            trimmedKeyword = keyword.trim();
            hasKeyword = !trimmedKeyword.isEmpty();
        }
        StringBuilder sql = new StringBuilder(baseSql);
        if (hasKeyword) {
            sql.append(" WHERE p.name LIKE ?");
        }
        sql.append(" ORDER BY p.name");

        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            if (hasKeyword && trimmedKeyword != null) {
                ps.setString(1, "%" + trimmedKeyword + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    public Optional<Product> findById(int id) throws SQLException {
        String sql = """
            SELECT
                p.id,
                p.name,
                p.brand,
                COALESCE(p.sale_price, 0) AS sale_price,
                COALESCE(p.stock_quantity, 0) AS stock_quantity,
                p.description,
                p.image_url
            FROM Product p
            WHERE p.id = ?
        """;

        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setSalePrice(rs.getDouble("sale_price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));
    p.setDescription(rs.getString("description"));
    p.setImageUrl(rs.getString("image_url"));
        return p;
    }
}
