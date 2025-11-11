package com.qlst.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.qlst.model.SupplierStatistics;

public class SupplierStatisticsDAO {

    private static final String BASE_SELECT =
        "SELECT s.id AS supplier_id, s.code AS supplier_code, s.name AS supplier_name, " +
        "       s.tax_code AS supplier_tax_code, s.address AS supplier_address, " +
        "       COUNT(DISTINCT gr.id) AS receipt_count, " +
        "       COALESCE(SUM(grd.quantity_received), 0) AS total_quantity, " +
        "       COALESCE(SUM(grd.quantity_received * grd.unit_purchase_price), 0) AS total_purchase " +
        "FROM Supplier s " +
        "LEFT JOIN GoodReceipt gr ON gr.supplier_id = s.id AND gr.created_date BETWEEN ? AND ? " +
        "LEFT JOIN GoodReceiptDetail grd ON grd.good_receipt_id = gr.id ";

    private static final String GROUP_BY =
        "GROUP BY s.id, s.code, s.name, s.tax_code, s.address";

    private SupplierStatistics mapRow(ResultSet rs, LocalDate from, LocalDate to) throws SQLException {
        BigDecimal totalPurchase = rs.getBigDecimal("total_purchase");
        if (totalPurchase == null) {
            totalPurchase = BigDecimal.ZERO;
        }
        BigDecimal totalQuantity = rs.getBigDecimal("total_quantity");
        if (totalQuantity == null) {
            totalQuantity = BigDecimal.ZERO;
        }
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal outstanding = totalPurchase.subtract(totalPaid);

        return new SupplierStatistics(
            rs.getInt("supplier_id"),
            rs.getString("supplier_code"),
            rs.getString("supplier_name"),
            rs.getString("supplier_tax_code"),
            rs.getString("supplier_address"),
            from,
            to,
            rs.getInt("receipt_count"),
            totalPurchase,
            totalPaid,
            outstanding,
            totalQuantity);
    }

    public List<SupplierStatistics> findByRange(LocalDate from, LocalDate to) throws SQLException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from/to must not be null");
        }
        String sql = BASE_SELECT + GROUP_BY + " ORDER BY total_purchase DESC, supplier_name";
        try (Connection cn = DAO.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                List<SupplierStatistics> result = new ArrayList<>();
                while (rs.next()) {
                    SupplierStatistics stats = mapRow(rs, from, to);
                    if (stats.getReceiptCount() > 0 || stats.getTotalQuantity().compareTo(BigDecimal.ZERO) > 0) {
                        result.add(stats);
                    }
                }
                return result;
            }
        }
    }

    public SupplierStatistics findOne(int supplierId, LocalDate from, LocalDate to) throws SQLException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from/to must not be null");
        }
        String sql = BASE_SELECT + "WHERE s.id = ? " + GROUP_BY;
        try (Connection cn = DAO.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ps.setInt(3, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs, from, to);
                }
            }
        }
        return null;
    }
}
