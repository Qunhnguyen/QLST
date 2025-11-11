package com.qlst.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qlst.model.GoodsReceiptDetail;


public class GoodsReceiptDetailDAO {
    public List<GoodsReceiptDetail> findByReceipt(String goodReceiptId) throws SQLException {
        String sql = """
            SELECT 
                d.id, 
                d.good_receipt_id, 
                d.product_id,
                p.name AS product_name,
                d.quantity_received, 
                d.unit_purchase_price,
                CAST(d.quantity_received AS DECIMAL(10,2)) * d.unit_purchase_price AS line_total
            FROM GoodReceiptDetail d
            JOIN Product p ON p.id = d.product_id
            WHERE d.good_receipt_id = ?
            ORDER BY d.id
        """;
        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(goodReceiptId));
            ResultSet rs = ps.executeQuery();

            List<GoodsReceiptDetail> list = new ArrayList<>();
            while (rs.next()) {
                GoodsReceiptDetail detail = new GoodsReceiptDetail(
                    rs.getInt("id"),
                    rs.getInt("good_receipt_id"),
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity_received"),
                    rs.getBigDecimal("unit_purchase_price"),
                    rs.getBigDecimal("line_total"));
                list.add(detail);
            }
            return list;
        }
    }
}