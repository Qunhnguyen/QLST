<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<c:set var="mode" value="${empty mode ? 'revenue' : mode}" />
<c:set var="filterApplied" value="${filterApplied}" />

<ui:layout title="Báo cáo nhà cung cấp" activeMenu="reports">
    <div class="section-title">
        <span class="section-eyebrow">Quản lý / Thống kê nhà cung cấp</span>
        <h1>Thống kê nhà cung cấp</h1>
        <p class="section-subtitle">Theo dõi doanh thu và sản lượng nhập từ các nhà cung cấp theo từng giai đoạn.</p>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/reports/suppliers" class="filter-grid">
        <div class="field-pill">
            <label for="from">Từ ngày</label>
            <input class="input-pill" type="date" id="from" name="from" value="${from}">
        </div>
        <div class="field-pill">
            <label for="to">Đến ngày</label>
            <input class="input-pill" type="date" id="to" name="to" value="${to}">
        </div>
        <div class="field-pill">
            <label for="mode">Kiểu thống kê</label>
            <select class="input-pill" id="mode" name="mode">
                <option value="revenue" ${mode == 'revenue' ? 'selected' : ''}>Theo doanh thu</option>
                <option value="quantity" ${mode == 'quantity' ? 'selected' : ''}>Theo số lượng nhập</option>
            </select>
        </div>
        <div class="filter-actions">
            <c:url var="clearUrl" value="/reports/suppliers" />
            <a href="${clearUrl}" class="btn btn-outline">Xóa bộ lọc</a>
            <button class="btn btn-primary" type="submit">Xem</button>
        </div>
    </form>

    <c:if test="${filterApplied}">
        <div class="summary-grid">
            <div class="summary-card">
                <span class="summary-card__label">Tổng số NCC</span>
                <span class="summary-card__value">${supplierTotal}</span>
            </div>
            <div class="summary-card">
                <span class="summary-card__label">Tổng số lần nhập</span>
                <span class="summary-card__value">${totalReceipts}</span>
            </div>
            <div class="summary-card">
                <span class="summary-card__label">Tổng SL nhập</span>
                <span class="summary-card__value">
                    <fmt:formatNumber value="${totalQuantity}" pattern="#,##0" />
                </span>
            </div>
            <div class="summary-card">
                <span class="summary-card__label">Tổng doanh thu</span>
                <span class="summary-card__value">
                    <fmt:formatNumber value="${totalRevenue}" pattern="#,##0" /> VND
                </span>
            </div>
        </div>

        <div class="table-caption">
            <div>
                <h2>Danh sách nhà cung cấp</h2>
                <span>${fromDisplay} → ${toDisplay}</span>
            </div>
            <c:set var="pageMeta" value="${suppliersPage}" />
            <span class="badge badge-muted">Sắp xếp theo: ${mode == 'quantity' ? 'Số lượng nhập' : 'Doanh thu'}</span>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not filterApplied}">
            <div class="empty-state">
                <h3>Chọn khoảng thời gian cần thống kê</h3>
                <p>Vui lòng nhập “Từ ngày” và “Đến ngày” rồi nhấn Xem để hiển thị dữ liệu.</p>
            </div>
        </c:when>
        <c:when test="${empty suppliers}">
            <div class="empty-state">
                <h3>Chưa có dữ liệu trong khoảng thời gian này</h3>
                <p>Hãy thử chọn thời gian khác hoặc kiểm tra lại nguồn dữ liệu.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                                <tr>
                                    <th class="stt">#</th>
                                    <th>Mã NCC</th>
                                    <th>Tên NCC</th>
                                    <th class="num">Tổng SL nhập</th>
                                    <th class="num">Tổng doanh thu</th>
                                    <th class="num">Số lần nhập</th>
                                    <th class="center"></th>
                                </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${suppliers}" varStatus="st">
                            <c:url var="detailUrl" value="/receipts">
                                <c:param name="supplierId" value="${s.id}" />
                                <c:param name="from" value="${from}" />
                                <c:param name="to" value="${to}" />
                                <c:param name="page" value="1" />
                                <c:param name="size" value="${pageMeta != null ? pageMeta.pageSize : 6}" />
                            </c:url>
                            <tr>
                                        <td class="stt">${(pageMeta != null ? pageMeta.startItem : 1) + st.index}</td>
                                        <td><span class="tag">${not empty s.code ? s.code : ('#' + s.id)}</span></td>
                                        <td style="font-weight: 600;">
                                            ${s.name}
                                            <c:if test="${not empty duplicateNames and duplicateNames.contains(s.name)}">
                                                <small class="text-muted"> · ${s.code}</small>
                                            </c:if>
                                        </td>
                                        <td class="num"><fmt:formatNumber value="${s.totalQuantity}" pattern="#,##0" /></td>
                                        <td class="num"><fmt:formatNumber value="${s.revenue}" pattern="#,##0" /> VND</td>
                                        <td class="num">${s.receiptCount}</td>
                                <td class="table-actions center">
                                    <a class="btn btn-outline" href="${detailUrl}">Xem phiếu nhập</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <c:if test="${supplierTotal > 0}">
                <div class="pagination">
                    <div class="pagination__summary">
                        <c:choose>
                            <c:when test="${pageMeta != null}">
                                Trang ${pageMeta.pageNumber}/${pageMeta.totalPages} · Hiển thị ${pageMeta.startItem}-${pageMeta.endItem} / ${supplierTotal}
                            </c:when>
                            <c:otherwise>
                                Tổng cộng ${supplierTotal} nhà cung cấp
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="pagination__controls">
                        <c:if test="${pageMeta != null && pageMeta.hasPrevious}">
                            <c:url var="prevUrl" value="/reports/suppliers">
                                <c:param name="from" value="${from}" />
                                <c:param name="to" value="${to}" />
                                <c:param name="mode" value="${mode}" />
                                <c:param name="page" value="${pageMeta.pageNumber - 1}" />
                                <c:param name="size" value="${pageMeta.pageSize}" />
                            </c:url>
                            <a class="pagination__link" href="${prevUrl}">← Trước</a>
                        </c:if>
                        <c:if test="${pageMeta != null && pageMeta.hasNext}">
                            <c:url var="nextUrl" value="/reports/suppliers">
                                <c:param name="from" value="${from}" />
                                <c:param name="to" value="${to}" />
                                <c:param name="mode" value="${mode}" />
                                <c:param name="page" value="${pageMeta.pageNumber + 1}" />
                                <c:param name="size" value="${pageMeta.pageSize}" />
                            </c:url>
                            <a class="pagination__link" href="${nextUrl}">Sau →</a>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>

    <div class="btn-group" style="margin-top: 24px;">
        <a href="${pageContext.request.contextPath}/reports/menu" class="btn btn-outline">← Quay lại Menu</a>
    </div>
</ui:layout>

