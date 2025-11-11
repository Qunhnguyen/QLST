package com.qlst.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qlst.model.SupplierStatistics;
import com.qlst.service.SupplierStatisticsService;
import com.qlst.util.Page;
import com.qlst.util.PaginationUtil;

@WebServlet(urlPatterns = {"/main", "/reports/menu", "/reports/suppliers"})
public class SupplierStatisticsServlet extends HttpServlet {
    private final SupplierStatisticsService supplierStatisticsService = new SupplierStatisticsService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        try {
            switch (path) {
                case "/main":           
                    req.getRequestDispatcher("/WEB-INF/views/FormMainManager.jsp")
                       .forward(req, resp);
                    break;

                case "/reports/menu":   
                    req.getRequestDispatcher("/WEB-INF/views/FormReportMenu.jsp")
                       .forward(req, resp);
                    break;

                case "/reports/suppliers": { 
                    String fromStr = req.getParameter("from");
                    String toStr   = req.getParameter("to");
                    String mode    = req.getParameter("mode");

            boolean hasFilter = fromStr != null && !fromStr.isBlank()
                && toStr != null && !toStr.isBlank();

            LocalDate from = null;
            LocalDate to = null;
            if (hasFilter) {
            from = LocalDate.parse(fromStr);
            to = LocalDate.parse(toStr);
            }

                    String selectedMode = (mode == null || mode.isBlank()) ? "revenue" : mode;
            List<SupplierStatistics> rows = hasFilter
                ? new ArrayList<>(supplierStatisticsService.getStatistics(from, to))
                : new ArrayList<>();

                    Comparator<SupplierStatistics> comparator;
                    if ("quantity".equalsIgnoreCase(selectedMode)) {
                        comparator = Comparator.comparing(
                                s -> s.getTotalQuantity() == null ? BigDecimal.ZERO : s.getTotalQuantity(),
                                Comparator.reverseOrder());
                    } else {
                        comparator = Comparator.comparing(
                                s -> s.getRevenue() == null ? BigDecimal.ZERO : s.getRevenue(),
                                Comparator.reverseOrder());
                    }
                    rows.sort(comparator.thenComparing(SupplierStatistics::getName));

                    BigDecimal totalRevenue = BigDecimal.ZERO;
                    BigDecimal totalQuantity = BigDecimal.ZERO;
                    int totalReceipts = 0;
                    for (SupplierStatistics supplier : rows) {
                        BigDecimal revenue = supplier.getRevenue() == null ? BigDecimal.ZERO : supplier.getRevenue();
                        BigDecimal quantity = supplier.getTotalQuantity() == null ? BigDecimal.ZERO : supplier.getTotalQuantity();
                        totalRevenue = totalRevenue.add(revenue);
                        totalQuantity = totalQuantity.add(quantity);
                        totalReceipts += supplier.getReceiptCount();
                    }

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    req.setAttribute("from", from);
                    req.setAttribute("to", to);
                    req.setAttribute("fromDisplay", hasFilter && from != null ? from.format(dateFormatter) : null);
                    req.setAttribute("toDisplay", hasFilter && to != null ? to.format(dateFormatter) : null);
                    int pageNumber = PaginationUtil.parsePage(req.getParameter("page"));
                    int pageSize = PaginationUtil.parsePageSize(req.getParameter("size"), PaginationUtil.DEFAULT_PAGE_SIZE);
                    Page<SupplierStatistics> pageResult = PaginationUtil.paginate(rows, pageNumber, pageSize);

                    req.setAttribute("suppliers", pageResult.getContent());
                    req.setAttribute("suppliersPage", pageResult);
                    req.setAttribute("mode", selectedMode);
                    req.setAttribute("supplierTotal", rows.size());
                    req.setAttribute("totalRevenue", totalRevenue);
                    req.setAttribute("totalQuantity", totalQuantity);
                    req.setAttribute("totalReceipts", totalReceipts);
                    req.setAttribute("filterApplied", hasFilter);

                    
                    Map<String, Integer> nameCount = new HashMap<>();
                    for (SupplierStatistics s : rows) {
                        String n = s.getName() == null ? "" : s.getName().trim();
                        nameCount.put(n, nameCount.getOrDefault(n, 0) + 1);
                    }
                    Set<String> duplicateNames = new HashSet<>();
                    for (Map.Entry<String, Integer> e : nameCount.entrySet()) {
                        if (e.getValue() > 1) duplicateNames.add(e.getKey());
                    }
                    req.setAttribute("duplicateNames", duplicateNames);

                    req.getRequestDispatcher("/WEB-INF/views/FormSupplierStatistics.jsp")
                       .forward(req, resp);
                    break;
                }
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
