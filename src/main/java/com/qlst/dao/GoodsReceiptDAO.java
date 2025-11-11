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

import com.qlst.model.GoodsReceipt;
import com.qlst.model.GoodsReceiptDetail;
import com.qlst.model.Invoice;
import com.qlst.model.Supplier;

public class GoodsReceiptDAO {
    public GoodsReceipt findById(int receiptId) throws SQLException {
        String sql = """
            SELECT
                gr.id,
                gr.supplier_id,
                gr.created_date,
                gr.total_amount,
                gr.warehouse_staff_id,
                s.code  AS supplier_code,
                s.name  AS supplier_name,
                s.tax_code AS supplier_tax_code,
                s.address AS supplier_address,
                inv.id  AS invoice_id,
                inv.issue_date AS invoice_issue_date,
                inv.tax AS invoice_tax,
                inv.discount_text AS invoice_discount,
                inv.imported_quantity AS invoice_imported_quantity
            FROM GoodReceipt gr
            LEFT JOIN Supplier s ON gr.supplier_id = s.id
            LEFT JOIN Invoice inv ON gr.invoice_id = inv.id
            WHERE gr.id = ?
        """;

        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, receiptId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapReceipt(rs);
            }
        }
    }

    public GoodsReceipt getReceiptInvoiceView(int goodsReceiptId) throws SQLException {
        String sql = """
            SELECT
                gr.id,
                gr.supplier_id,
                gr.created_date,
                gr.total_amount,
                gr.warehouse_staff_id,
                s.code  AS supplier_code,
                s.name  AS supplier_name,
                s.tax_code AS supplier_tax_code,
                s.address AS supplier_address,
                inv.id  AS invoice_id,
                inv.issue_date AS invoice_issue_date,
                inv.tax AS invoice_tax,
                inv.discount_text AS invoice_discount,
                inv.imported_quantity AS invoice_imported_quantity,
                d.id    AS detail_id,
                d.product_id AS detail_product_id,
                p.name  AS detail_product_name,
                d.quantity_received AS detail_quantity,
                d.unit_purchase_price AS detail_unit_price
            FROM GoodReceipt gr
            LEFT JOIN Supplier s ON gr.supplier_id = s.id
            LEFT JOIN Invoice inv ON gr.invoice_id = inv.id
            LEFT JOIN GoodReceiptDetail d ON gr.id = d.good_receipt_id
            LEFT JOIN Product p ON d.product_id = p.id
            WHERE gr.id = ?
            ORDER BY d.id
        """;

        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, goodsReceiptId);
            try (ResultSet rs = ps.executeQuery()) {
                ReceiptAggregate aggregate = null;
                while (rs.next()) {
                    if (aggregate == null) {
                        aggregate = new ReceiptAggregate(rs);
                    }
                    aggregate.appendDetail(rs);
                }
                return aggregate != null ? aggregate.toReceipt() : null;
            }
        }
    }

    public List<GoodsReceipt> findSupplier(int supplierId, LocalDate from, LocalDate to) throws SQLException {
        String sql = """
            SELECT
                gr.id,
                gr.supplier_id,
                gr.created_date,
                gr.warehouse_staff_id,
                COALESCE(SUM(grd.quantity_received * grd.unit_purchase_price), 0) AS total_amount,
                s.code  AS supplier_code,
                s.name  AS supplier_name,
                s.tax_code AS supplier_tax_code,
                s.address AS supplier_address,
                inv.id  AS invoice_id,
                inv.issue_date AS invoice_issue_date,
                inv.tax AS invoice_tax,
                inv.discount_text AS invoice_discount,
                inv.imported_quantity AS invoice_imported_quantity
            FROM GoodReceipt gr
            LEFT JOIN Supplier s ON gr.supplier_id = s.id
            LEFT JOIN Invoice inv ON gr.invoice_id = inv.id
            LEFT JOIN GoodReceiptDetail grd ON gr.id = grd.good_receipt_id
            WHERE gr.supplier_id = ? AND gr.created_date BETWEEN ? AND ?
            GROUP BY
                gr.id,
                gr.supplier_id,
                gr.created_date,
                gr.warehouse_staff_id,
                s.code,
                s.name,
                s.tax_code,
                s.address,
                inv.id,
                inv.issue_date,
                inv.tax,
                inv.discount_text,
                inv.imported_quantity
            ORDER BY gr.created_date DESC, gr.id DESC
        """;

        try (Connection con = DAO.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                List<GoodsReceipt> receipts = new ArrayList<>();
                while (rs.next()) {
                    receipts.add(mapReceipt(rs));
                }
                return receipts;
            }
        }
    }

    private GoodsReceipt mapReceipt(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int supplierId = rs.getInt("supplier_id");
        java.sql.Date dateValue = rs.getDate("created_date");
        LocalDate createdDate = dateValue != null ? dateValue.toLocalDate() : null;
        BigDecimal totalAmount = rs.getBigDecimal("total_amount");
        Integer warehouseStaffId = rs.getObject("warehouse_staff_id", Integer.class);
        Invoice invoice = mapInvoice(rs);
        Supplier supplier = mapSupplier(rs, supplierId);
        return new GoodsReceipt(id, supplierId, createdDate, totalAmount, warehouseStaffId, invoice, supplier, null);
    }

    private Invoice mapInvoice(ResultSet rs) throws SQLException {
        Integer invoiceId = rs.getObject("invoice_id", Integer.class);
        if (invoiceId == null) {
            return null;
        }
        java.sql.Date issueDate = rs.getDate("invoice_issue_date");
        return new Invoice(
            invoiceId,
            issueDate != null ? issueDate.toLocalDate() : null,
            rs.getBigDecimal("invoice_tax"),
            rs.getString("invoice_discount"),
            rs.getObject("invoice_imported_quantity", Integer.class));
    }

    private Supplier mapSupplier(ResultSet rs, int supplierId) throws SQLException {
        String code = rs.getString("supplier_code");
        String name = rs.getString("supplier_name");
        String taxCode = rs.getString("supplier_tax_code");
        String address = rs.getString("supplier_address");
        if (code == null && name == null && taxCode == null && address == null) {
            return null;
        }
        return new Supplier(supplierId, code, name, taxCode, address);
    }

    private static class ReceiptAggregate {
        private final int id;
        private final int supplierId;
        private final LocalDate createdDate;
        private final Integer warehouseStaffId;
        private BigDecimal totalAmount;
        private final Supplier supplier;
        private final Invoice invoice;
        private final List<GoodsReceiptDetail> details = new ArrayList<>();

        ReceiptAggregate(ResultSet rs) throws SQLException {
            this.id = rs.getInt("id");
            this.supplierId = rs.getInt("supplier_id");
            java.sql.Date created = rs.getDate("created_date");
            this.createdDate = created != null ? created.toLocalDate() : null;
            this.totalAmount = rs.getBigDecimal("total_amount");
            this.warehouseStaffId = rs.getObject("warehouse_staff_id", Integer.class);
            this.supplier = new Supplier(
                supplierId,
                rs.getString("supplier_code"),
                rs.getString("supplier_name"),
                rs.getString("supplier_tax_code"),
                rs.getString("supplier_address"));
            this.invoice = buildInvoice(rs);
        }

        private Invoice buildInvoice(ResultSet rs) throws SQLException {
            Integer invoiceId = rs.getObject("invoice_id", Integer.class);
            if (invoiceId == null) {
                return null;
            }
            java.sql.Date issueDate = rs.getDate("invoice_issue_date");
            return new Invoice(
                invoiceId,
                issueDate != null ? issueDate.toLocalDate() : null,
                rs.getBigDecimal("invoice_tax"),
                rs.getString("invoice_discount"),
                rs.getObject("invoice_imported_quantity", Integer.class));
        }

        void appendDetail(ResultSet rs) throws SQLException {
            Integer detailId = rs.getObject("detail_id", Integer.class);
            if (detailId == null) {
                return;
            }
            int productId = rs.getInt("detail_product_id");
            String productName = rs.getString("detail_product_name");
            int quantity = rs.getInt("detail_quantity");
            BigDecimal unitPrice = rs.getBigDecimal("detail_unit_price");
            BigDecimal lineTotal = BigDecimal.ZERO;
            if (unitPrice != null) {
                lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            }
            details.add(new GoodsReceiptDetail(
                detailId,
                id,
                productId,
                productName,
                quantity,
                unitPrice,
                lineTotal));
        }

        GoodsReceipt toReceipt() {
            BigDecimal amount = totalAmount;
            if ((amount == null || amount.compareTo(BigDecimal.ZERO) == 0) && !details.isEmpty()) {
                amount = BigDecimal.ZERO;
                for (GoodsReceiptDetail detail : details) {
                    if (detail.getLineTotal() != null) {
                        amount = amount.add(detail.getLineTotal());
                    }
                }
            }
            return new GoodsReceipt(id, supplierId, createdDate, amount, warehouseStaffId, invoice, supplier, details);
        }
    }
}