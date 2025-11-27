package com.qlst.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qlst.dao.ProductDAO;
import com.qlst.model.Product;
import com.qlst.util.Page;
import com.qlst.util.PaginationUtil;

@WebServlet(urlPatterns = {"/customer/products", "/customer/product"})
public class ProductSearchServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            switch (path) {
                case "/customer/products":
                    handleSearch(req, resp);
                    break;
                case "/customer/product":
                    handleDetail(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Không thể tải dữ liệu sản phẩm", e);
        }
    }

    private void handleSearch(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        String keyword = req.getParameter("q");
        List<Product> products = productDAO.searchProducts(keyword);
        int pageNumber = PaginationUtil.parsePage(req.getParameter("page"));
        int pageSize = PaginationUtil.parsePageSize(req.getParameter("size"), PaginationUtil.DEFAULT_PAGE_SIZE);
        Page<Product> productsPage = PaginationUtil.paginate(products, pageNumber, pageSize);
        req.setAttribute("keyword", keyword);
        req.setAttribute("products", productsPage.getContent());
        req.setAttribute("productsPage", productsPage);
        req.setAttribute("totalProducts", productsPage.getTotalItems());
        req.getRequestDispatcher("/WEB-INF/views/customer/ProductSearch.jsp")
           .forward(req, resp);
    }

    private void handleDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/products");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            resp.sendRedirect(req.getContextPath() + "/customer/products");
            return;
        }

        Optional<Product> opt = productDAO.findById(id);
        if (opt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customer/products?notfound=1");
            return;
        }
        req.setAttribute("product", opt.get());
        req.getRequestDispatcher("/WEB-INF/views/customer/ProductDetail.jsp")
           .forward(req, resp);
    }
}
