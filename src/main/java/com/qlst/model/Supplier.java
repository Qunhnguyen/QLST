package com.qlst.model;

import java.math.BigDecimal;

public class Supplier {
    private final int id;
    private final String code;
    private final String name;
    private final String taxCode;
    private final String address;
    private final BigDecimal revenue;
    private final BigDecimal totalQuantity;
    private final int receiptCount;

    public Supplier(int id,
                    String code,
                    String name,
                    String taxCode,
                    String address) {
        this(id, code, name, taxCode, address, null, null, 0);
    }

    public Supplier(int id,
                    String code,
                    String name,
                    String taxCode,
                    String address,
                    BigDecimal revenue,
                    BigDecimal totalQuantity,
                    int receiptCount) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.taxCode = taxCode;
        this.address = address;
        this.revenue = revenue != null ? revenue : BigDecimal.ZERO;
        this.totalQuantity = totalQuantity != null ? totalQuantity : BigDecimal.ZERO;
        this.receiptCount = receiptCount;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public int getReceiptCount() {
        return receiptCount;
    }
}
