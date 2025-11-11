package com.qlst.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SupplierStatistics {
    private final int supplierId;
    private final String supplierCode;
    private final String supplierName;
    private final String supplierTaxCode;
    private final String supplierAddress;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final int receiptCount;
    private final BigDecimal totalPurchase;
    private final BigDecimal totalPaid;
    private final BigDecimal outstandingAmount;
    private final BigDecimal totalQuantity;

    public SupplierStatistics(int supplierId,
                              String supplierCode,
                              String supplierName,
                              String supplierTaxCode,
                              String supplierAddress,
                              LocalDate fromDate,
                              LocalDate toDate,
                              int receiptCount,
                              BigDecimal totalPurchase,
                              BigDecimal totalPaid,
                              BigDecimal outstandingAmount,
                              BigDecimal totalQuantity) {
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.supplierTaxCode = supplierTaxCode;
        this.supplierAddress = supplierAddress;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.receiptCount = receiptCount;
        this.totalPurchase = totalPurchase != null ? totalPurchase : BigDecimal.ZERO;
        this.totalPaid = totalPaid != null ? totalPaid : BigDecimal.ZERO;
        this.outstandingAmount = outstandingAmount != null ? outstandingAmount : BigDecimal.ZERO;
        this.totalQuantity = totalQuantity != null ? totalQuantity : BigDecimal.ZERO;
    }

    public SupplierStatistics(int supplierId,
                              LocalDate fromDate,
                              LocalDate toDate,
                              int receiptCount,
                              BigDecimal totalPurchase,
                              BigDecimal totalQuantity) {
        this(supplierId, null, null, null, null, fromDate, toDate, receiptCount, totalPurchase, BigDecimal.ZERO,
            totalPurchase, totalQuantity);
    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getId() {
        return supplierId;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getCode() {
        return supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getName() {
        return supplierName;
    }

    public String getSupplierTaxCode() {
        return supplierTaxCode;
    }

    public String getTaxCode() {
        return supplierTaxCode;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public String getAddress() {
        return supplierAddress;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public int getReceiptCount() {
        return receiptCount;
    }

    public BigDecimal getTotalPurchase() {
        return totalPurchase;
    }

    public BigDecimal getRevenue() {
        return totalPurchase;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }
}