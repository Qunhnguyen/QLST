package com.qlst.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {
  private final int id;
  private final LocalDate issueDate;
  private final BigDecimal tax;
  private final String discountText;
  private final Integer importedQuantity;

  public Invoice(int id,
                 LocalDate issueDate,
                 BigDecimal tax,
                 String discountText,
                 Integer importedQuantity) {
    this.id = id;
    this.issueDate = issueDate;
    this.tax = tax;
    this.discountText = discountText;
    this.importedQuantity = importedQuantity;
  }

  public int getId(){return id;}
  public LocalDate getIssueDate(){return issueDate;}
  public BigDecimal getTax(){return tax;}
  public String getDiscountText(){return discountText;}
  public Integer getImportedQuantity(){return importedQuantity;}
}
