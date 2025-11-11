package com.qlst.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qlst.dao.GoodsReceiptDAO;
import com.qlst.dao.GoodsReceiptDetailDAO;
import com.qlst.model.GoodsReceipt;
import com.qlst.model.GoodsReceiptDetail;
import com.qlst.model.Invoice;
import com.qlst.model.Supplier;
import com.qlst.util.Page;
import com.qlst.util.PaginationUtil;

@WebServlet(urlPatterns = {"/receipts", "/receipts/detail", "/receipts/invoice"})
public class GoodsReceiptServlet extends HttpServlet {
    private final GoodsReceiptDAO grDAO = new GoodsReceiptDAO();
    private final GoodsReceiptDetailDAO detailDAO = new GoodsReceiptDetailDAO();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        try {
            switch (path) {
                case "/receipts": {
                    String supplierIdParam = req.getParameter("supplierId");
                    String fromParam = req.getParameter("from");
                    String toParam = req.getParameter("to");

                    if (supplierIdParam == null || fromParam == null || toParam == null) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required query parameters");
                        return;
                    }
                    int supplierId = Integer.parseInt(supplierIdParam);
                    LocalDate from = LocalDate.parse(fromParam);
                    LocalDate to   = LocalDate.parse(toParam);
                    List<GoodsReceipt> list = grDAO.findSupplier(supplierId, from, to);
                    Supplier supplier = list.isEmpty() ? null : list.get(0).getSupplier();
                    String supplierName = supplier != null ? supplier.getName() : null;
                    String supplierCode = supplier != null ? supplier.getCode() : null;
                    String supplierTaxCode = supplier != null ? supplier.getTaxCode() : null;
                    String supplierAddress = supplier != null ? supplier.getAddress() : null;

                    BigDecimal grossAmount = BigDecimal.ZERO;
                    for (GoodsReceipt receipt : list) {
                        BigDecimal amount = receipt.getTotalAmount();
                        if (amount != null) {
                            grossAmount = grossAmount.add(amount);
                        }
                    }

                    int pageNumber = PaginationUtil.parsePage(req.getParameter("page"));
                    int pageSize = PaginationUtil.parsePageSize(req.getParameter("size"), PaginationUtil.DEFAULT_PAGE_SIZE);
                    Page<GoodsReceipt> pageResult = PaginationUtil.paginate(list, pageNumber, pageSize);

                    req.setAttribute("receipts", pageResult.getContent());
                    req.setAttribute("receiptsPage", pageResult);
                    req.setAttribute("supplierId", supplierId);
                    req.setAttribute("from", from);
                    req.setAttribute("to", to);
                    req.setAttribute("fromDisplay", from.format(DATE_FORMATTER));
                    req.setAttribute("toDisplay", to.format(DATE_FORMATTER));
                    req.setAttribute("supplierName", supplierName);
                    req.setAttribute("supplierCode", supplierCode);
                    req.setAttribute("supplierTaxCode", supplierTaxCode);
                    req.setAttribute("supplierAddress", supplierAddress);
                    req.setAttribute("supplier", supplier);
                    req.setAttribute("receiptCount", list.size());
                    req.setAttribute("grossAmount", grossAmount);
                    req.getRequestDispatcher("/WEB-INF/views/FormGoodsReceipt.jsp").forward(req, resp);
                    break;
                }
                case "/receipts/detail": {
                    int grId = Integer.parseInt(req.getParameter("grId"));
                    String supplierIdParam = req.getParameter("supplierId");
                    Integer supplierId = (supplierIdParam != null && !supplierIdParam.isBlank())
                            ? Integer.valueOf(supplierIdParam)
                            : null;
                    String supplierName = req.getParameter("supplierName");
                    String supplierCode = req.getParameter("supplierCode");
                    String supplierTaxCode = req.getParameter("supplierTaxCode");
                    String supplierAddress = req.getParameter("supplierAddress");
                    String receiptDate = req.getParameter("receiptDate");
                    LocalDate from = parseOptionalDate(req.getParameter("from"));
                    LocalDate to = parseOptionalDate(req.getParameter("to"));
                    int listPage = PaginationUtil.parsePage(req.getParameter("listPage"));
                    int listSize = PaginationUtil.parsePageSize(req.getParameter("listSize"), PaginationUtil.DEFAULT_PAGE_SIZE);
                    int linePageNumber = PaginationUtil.parsePage(req.getParameter("linePage"));
                    int linePageSize = PaginationUtil.parsePageSize(req.getParameter("lineSize"), PaginationUtil.DEFAULT_PAGE_SIZE);

                    List<GoodsReceiptDetail> lines = detailDAO.findByReceipt(String.valueOf(grId));

                    BigDecimal totalAmount = BigDecimal.ZERO;
                    for (GoodsReceiptDetail line : lines) {
                        totalAmount = totalAmount.add(line.getLineTotal());
                    }

                    Page<GoodsReceiptDetail> linePage = PaginationUtil.paginate(lines, linePageNumber, linePageSize);
                    req.setAttribute("grId", grId);
                    req.setAttribute("lines", linePage.getContent());
                    req.setAttribute("linesPage", linePage);
                    req.setAttribute("lineCount", lines.size());
                    req.setAttribute("totalAmount", totalAmount);
                    req.setAttribute("supplierName", supplierName);
                    req.setAttribute("supplierCode", supplierCode);
                    req.setAttribute("supplierTaxCode", supplierTaxCode);
                    req.setAttribute("supplierAddress", supplierAddress);
                    req.setAttribute("supplier", null);
                    req.setAttribute("supplierId", supplierId);
                    req.setAttribute("from", from);
                    req.setAttribute("to", to);
                    req.setAttribute("receiptDateDisplay", receiptDate);
                    if (from != null) {
                        req.setAttribute("fromDisplay", from.format(DATE_FORMATTER));
                    }
                    if (to != null) {
                        req.setAttribute("toDisplay", to.format(DATE_FORMATTER));
                    }
                    req.setAttribute("listPage", listPage);
                    req.setAttribute("listSize", listSize);
                    req.getRequestDispatcher("/WEB-INF/views/FormReceiptDetail.jsp").forward(req, resp);
                    break;
                }
                case "/receipts/invoice": {
                    int grId = Integer.parseInt(req.getParameter("grId"));
                    String supplierIdParam = req.getParameter("supplierId");
                    Integer supplierId = (supplierIdParam != null && !supplierIdParam.isBlank())
                            ? Integer.valueOf(supplierIdParam)
                            : null;
                    String supplierName = null;
                    String supplierCode = null;
                    String supplierTaxCode = null;
                    String supplierAddress = null;
                    Supplier supplier = null;
                    String supplierNameParam = req.getParameter("supplierName");
                    String supplierCodeParam = req.getParameter("supplierCode");
                    String supplierTaxCodeParam = req.getParameter("supplierTaxCode");
                    String supplierAddressParam = req.getParameter("supplierAddress");
                    String receiptDateParam = req.getParameter("receiptDate");
                    LocalDate from = parseOptionalDate(req.getParameter("from"));
                    LocalDate to = parseOptionalDate(req.getParameter("to"));
                    int listPage = PaginationUtil.parsePage(req.getParameter("listPage"));
                    int listSize = PaginationUtil.parsePageSize(req.getParameter("listSize"), PaginationUtil.DEFAULT_PAGE_SIZE);
                    int linePageNumber = PaginationUtil.parsePage(req.getParameter("linePage"));
                    int linePageSize = PaginationUtil.parsePageSize(req.getParameter("lineSize"), PaginationUtil.DEFAULT_PAGE_SIZE);
                    GoodsReceipt receipt = grDAO.getReceiptInvoiceView(grId);
                    if (receipt != null) {
                        supplierId = receipt.getSupplierId();
                        supplier = receipt.getSupplier();
                    }
                    if (supplier != null) {
                        supplierName = supplier.getName();
                        supplierCode = supplier.getCode();
                        supplierTaxCode = supplier.getTaxCode();
                        supplierAddress = supplier.getAddress();
                    } else {
                        supplierName = supplierNameParam;
                        supplierCode = supplierCodeParam;
                        supplierTaxCode = supplierTaxCodeParam;
                        supplierAddress = supplierAddressParam;
                    }
                    Invoice inv = receipt != null ? receipt.getInvoice() : null;
                    List<GoodsReceiptDetail> lines = receipt != null
                        ? new ArrayList<>(receipt.getDetails())
                        : new ArrayList<>();
                    BigDecimal subtotal = BigDecimal.ZERO;
                    for (GoodsReceiptDetail line : lines) {
                        subtotal = subtotal.add(line.getLineTotal());
                    }
                    BigDecimal grandTotal = subtotal;
                    if (inv != null) {
                        BigDecimal tax = inv.getTax() == null ? BigDecimal.ZERO : inv.getTax();
                        grandTotal = subtotal.add(tax);
                        if (inv.getIssueDate() != null) {
                            req.setAttribute("invoiceDateDisplay", inv.getIssueDate().format(DATE_FORMATTER));
                        }
                    }

                    Page<GoodsReceiptDetail> linePage = PaginationUtil.paginate(lines, linePageNumber, linePageSize);
                    req.setAttribute("grId", grId);
                    req.setAttribute("invoice", inv);
                    req.setAttribute("subtotal", subtotal);
                    req.setAttribute("grandTotal", grandTotal);
                    req.setAttribute("lines", linePage.getContent());
                    req.setAttribute("linesPage", linePage);
                    req.setAttribute("lineCount", lines.size());
                    req.setAttribute("supplierName", supplierName);
                    req.setAttribute("supplierCode", supplierCode);
                    req.setAttribute("supplierTaxCode", supplierTaxCode);
                    req.setAttribute("supplierAddress", supplierAddress);
                    req.setAttribute("supplier", supplier);
                    req.setAttribute("receipt", receipt);
                    req.setAttribute("supplierId", supplierId);
                    req.setAttribute("from", from);
                    req.setAttribute("to", to);
                    if (receipt != null && receipt.getCreatedDate() != null) {
                        req.setAttribute("receiptDateDisplay", receipt.getCreatedDate().format(DATE_FORMATTER));
                    } else if (receiptDateParam != null && !receiptDateParam.isBlank()) {
                        req.setAttribute("receiptDateDisplay", receiptDateParam);
                    }
                    if (from != null) {
                        req.setAttribute("fromDisplay", from.format(DATE_FORMATTER));
                    }
                    if (to != null) {
                        req.setAttribute("toDisplay", to.format(DATE_FORMATTER));
                    }
                    req.setAttribute("listPage", listPage);
                    req.setAttribute("listSize", listSize);
                    req.getRequestDispatcher("/WEB-INF/views/FormInvoice.jsp").forward(req, resp);
                    break;
                }
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid request parameter", e);
        }
    }

    private LocalDate parseOptionalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value);
    }
}
