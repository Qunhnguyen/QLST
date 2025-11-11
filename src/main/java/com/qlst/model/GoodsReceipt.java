package com.qlst.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoodsReceipt {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int id;
    private final int supplierId;
    private final LocalDate createdDate;
    private final BigDecimal totalAmount;
    private final Integer warehouseStaffId;
    private final String createdDateLabel;
    private final Invoice invoice;
    private final Supplier supplier;
    private final List<GoodsReceiptDetail> details;

    public GoodsReceipt(int id,
                        int supplierId,
                        LocalDate createdDate,
                        BigDecimal totalAmount,
                        Integer warehouseStaffId) {
        this(id, supplierId, createdDate, totalAmount, warehouseStaffId, null, null, null);
    }

    public GoodsReceipt(int id,
                        int supplierId,
                        LocalDate createdDate,
                        BigDecimal totalAmount,
                        Integer warehouseStaffId,
                        Invoice invoice) {
        this(id, supplierId, createdDate, totalAmount, warehouseStaffId, invoice, null, null);
    }

    public GoodsReceipt(int id,
                        int supplierId,
                        LocalDate createdDate,
                        BigDecimal totalAmount,
                        Integer warehouseStaffId,
                        Invoice invoice,
                        Supplier supplier,
                        List<GoodsReceiptDetail> details) {
        this.id = id;
        this.supplierId = supplierId;
        this.createdDate = createdDate;
        this.totalAmount = totalAmount;
        this.warehouseStaffId = warehouseStaffId;
        this.createdDateLabel = createdDate != null ? createdDate.format(DISPLAY_FORMAT) : null;
        this.invoice = invoice;
        this.supplier = supplier;
        if (details == null || details.isEmpty()) {
            this.details = Collections.emptyList();
        } else {
            this.details = Collections.unmodifiableList(new ArrayList<>(details));
        }
    }

    public int getId() {
        return id;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Integer getWarehouseStaffId() {
        return warehouseStaffId;
    }

    public String getCreatedDateLabel() {
        return createdDateLabel;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public List<GoodsReceiptDetail> getDetails() {
        return details;
    }
}
