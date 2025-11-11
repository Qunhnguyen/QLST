package com.qlst.model;

import java.math.BigDecimal;

public class GoodsReceiptDetail {
    private final int id;
    private final int goodsReceiptId;
    private final int productId;
    private final String productName;
    private final int quantityReceived;
    private final BigDecimal unitPurchasePrice;
    private final BigDecimal lineTotal;

    public GoodsReceiptDetail(int id,
                              int goodsReceiptId,
                              int productId,
                              String productName,
                              int quantityReceived,
                              BigDecimal unitPurchasePrice,
                              BigDecimal lineTotal) {
        this.id = id;
        this.goodsReceiptId = goodsReceiptId;
        this.productId = productId;
        this.productName = productName;
        this.quantityReceived = quantityReceived;
        this.unitPurchasePrice = unitPurchasePrice;
        this.lineTotal = lineTotal;
    }

    public int getId() {
        return id;
    }

    public int getGoodsReceiptId() {
        return goodsReceiptId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantityReceived() {
        return quantityReceived;
    }

    public BigDecimal getUnitPurchasePrice() {
        return unitPurchasePrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }
}
