package com.qlst.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.qlst.model.Supplier;

public class SupplierDAO {


    public Supplier findById(int supplierId) throws SQLException {
        String sql = "SELECT id, code, name, tax_code, address FROM Supplier WHERE id = ?";
        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Supplier(
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("tax_code"),
                    rs.getString("address"));
            }
        }
    }
}
