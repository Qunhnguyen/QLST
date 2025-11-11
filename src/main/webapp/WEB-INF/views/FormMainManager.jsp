<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:layout title="Bảng điều khiển" activeMenu="main">
    <div class="section-title">
        <span class="section-eyebrow">Tổng quan hệ thống</span>
        <h1>Bảng điều khiển quản lý</h1>
        <p class="section-subtitle">Theo dõi các số liệu chính và truy cập nhanh vào các báo cáo quan trọng.</p>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <span class="summary-card__label">Nhà cung cấp</span>
            <span class="summary-card__value"><c:out value="${requestScope.supplierCount}" default="--"/></span>
            <p class="summary-card__hint">Tổng số nhà cung cấp đang hoạt động</p>
        </div>
        <div class="summary-card">
            <span class="summary-card__label">Sản phẩm</span>
            <span class="summary-card__value"><c:out value="${requestScope.productCount}" default="--"/></span>
            <p class="summary-card__hint">Danh mục hàng hóa hiện có</p>
        </div>
        <div class="summary-card">
            <span class="summary-card__label">Phiếu nhập tháng này</span>
            <span class="summary-card__value"><c:out value="${requestScope.receiptCount}" default="0"/></span>
            <p class="summary-card__hint">Số phiếu nhập đã được ghi nhận</p>
        </div>
    </div>

    <div class="action-grid">
        <a class="action-card" href="${pageContext.request.contextPath}/reports/menu">
            <div class="action-card__meta">
                <span class="badge">Báo cáo</span>
                <h3>Thống kê nhà cung cấp</h3>
                <p>Xem doanh thu, số lượng nhập và lịch sử phiếu nhập của từng nhà cung cấp.</p>
            </div>
            <span class="action-card__cta">Đi tới báo cáo →</span>
        </a>
        <a class="action-card action-card--disabled" href="#" onclick="return false;">
            <div class="action-card__meta">
                <span class="badge badge-muted">Sắp ra mắt</span>
                <h3>Quản lý sản phẩm</h3>
                <p>Tính năng quản lý sản phẩm sẽ sớm được cập nhật.</p>
            </div>
            <span class="action-card__cta">Đang phát triển…</span>
        </a>
    </div>
</ui:layout>
