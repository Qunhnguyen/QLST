<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<c:set var="supplierLabel" value="${supplierCode}" />
<c:if test="${empty supplierLabel && not empty supplierId}">
    <c:set var="supplierLabel">NCC #${supplierId}</c:set>
</c:if>
<c:if test="${empty supplierLabel}">
    <c:set var="supplierLabel" value="Không rõ nhà cung cấp" />
</c:if>
<c:set var="fromLabel" value="${empty fromDisplay ? from : fromDisplay}" />
<c:set var="toLabel" value="${empty toDisplay ? to : toDisplay}" />
<c:set var="supplierTaxLabel" value="${empty supplierTaxCode ? (empty supplier.taxCode ? 'Chưa cập nhật' : supplier.taxCode) : supplierTaxCode}" />
<c:set var="supplierAddressLabel" value="${empty supplierAddress ? (empty supplier.address ? 'Chưa cập nhật' : supplier.address) : supplierAddress}" />

<ui:layout title="Danh sách phiếu nhập" activeMenu="reports">
    <div class="section-title">
        <span class="section-eyebrow">Thống kê nhập hàng</span>
        <h1>Phiếu nhập ${supplierLabel}</h1>
        <p class="section-subtitle">
            ${not empty supplierName ? supplierName : 'Không rõ nhà cung cấp'} ·
            <strong>${fromLabel}</strong> → <strong>${toLabel}</strong>
        </p>
        <div class="supplier-meta">
            <span><strong>Mã số thuế:</strong> ${supplierTaxLabel}</span>
            <span><strong>Địa chỉ:</strong> ${supplierAddressLabel}</span>
        </div>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <span class="summary-card__label">Số phiếu nhập</span>
            <span class="summary-card__value">${receiptCount}</span>
        </div>
        <div class="summary-card">
            <span class="summary-card__label">Tổng giá trị</span>
            <span class="summary-card__value">
                <fmt:formatNumber value="${grossAmount}" pattern="#,##0" /> VND
            </span>
        </div>
    </div>

    <div class="table-caption">
        <h2>Danh sách phiếu nhập</h2>
        <c:set var="receiptsPage" value="${receiptsPage}" />
        <span>
            <c:choose>
                <c:when test="${receiptCount == 0}">0 phiếu</c:when>
                <c:otherwise>
                    Hiển thị ${receiptsPage.startItem} - ${receiptsPage.endItem} trong ${receiptCount} phiếu
                </c:otherwise>
            </c:choose>
        </span>
    </div>

    <c:choose>
        <c:when test="${empty receipts}">
            <div class="empty-state">
                <h3>Không tìm thấy phiếu nhập</h3>
                <p>Không có phiếu nhập nào phù hợp với bộ lọc đang chọn.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Mã phiếu</th>
                            <th>Ngày tạo</th>
                            <th class="num">Tổng tiền</th>
                            <th class="center"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="gr" items="${receipts}">
                            <c:url var="detailUrl" value="/receipts/detail">
                                <c:param name="grId" value="${gr.id}" />
                                <c:param name="supplierId" value="${supplierId}" />
                                <c:param name="from" value="${from}" />
                                <c:param name="to" value="${to}" />
                                <c:param name="supplierName" value="${supplierName}" />
                                <c:param name="supplierCode" value="${supplierCode}" />
                                <c:param name="supplierTaxCode" value="${supplierTaxCode}" />
                                <c:param name="supplierAddress" value="${supplierAddress}" />
                                <c:param name="receiptDate" value="${not empty gr.createdDateLabel ? gr.createdDateLabel : gr.createdDate}" />
                                <c:param name="listPage" value="${receiptsPage.pageNumber}" />
                                <c:param name="listSize" value="${receiptsPage.pageSize}" />
                                <c:param name="linePage" value="1" />
                                <c:param name="lineSize" value="${receiptsPage.pageSize}" />
                            </c:url>
                            <c:url var="invoiceUrl" value="/receipts/invoice">
                                <c:param name="grId" value="${gr.id}" />
                                <c:param name="supplierId" value="${supplierId}" />
                                <c:param name="from" value="${from}" />
                                <c:param name="to" value="${to}" />
                                <c:param name="supplierName" value="${supplierName}" />
                                <c:param name="supplierCode" value="${supplierCode}" />
                                <c:param name="supplierTaxCode" value="${supplierTaxCode}" />
                                <c:param name="supplierAddress" value="${supplierAddress}" />
                                <c:param name="receiptDate" value="${not empty gr.createdDateLabel ? gr.createdDateLabel : gr.createdDate}" />
                                <c:param name="listPage" value="${receiptsPage.pageNumber}" />
                                <c:param name="listSize" value="${receiptsPage.pageSize}" />
                                <c:param name="linePage" value="1" />
                                <c:param name="lineSize" value="${receiptsPage.pageSize}" />
                            </c:url>
                            <tr>
                                <td><span class="tag">#${gr.id}</span></td>
                                <td>${not empty gr.createdDateLabel ? gr.createdDateLabel : gr.createdDate}</td>
                                <td class="num"><fmt:formatNumber value="${gr.totalAmount}" pattern="#,##0" /> VND</td>
                                <td class="table-actions center">
                                    <a href="${detailUrl}" class="btn btn-outline">Chi tiết</a>
                                    <a href="${invoiceUrl}" class="btn btn-outline">Hóa đơn</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <c:if test="${receiptCount > 0}">
                <div class="pagination">
                    <div class="pagination__summary">
                        Trang ${receiptsPage.pageNumber}/${receiptsPage.totalPages}
                    </div>
                    <div class="pagination__controls">
                        <c:if test="${receiptsPage.hasPrevious}">
                            <c:url var="prevUrl" value="/receipts">
                                <c:param name="supplierId" value="${supplierId}" />
                                <c:param name="from" value="${from}" />
                                <c:param name="to" value="${to}" />
                                <c:param name="page" value="${receiptsPage.pageNumber - 1}" />
                                <c:param name="size" value="${receiptsPage.pageSize}" />
                            </c:url>
                            <a class="pagination__link" href="${prevUrl}">← Trước</a>
                        </c:if>
                        <span class="pagination__status">${receiptsPage.startItem}-${receiptsPage.endItem}</span>
                        <c:if test="${receiptsPage.hasNext}">
                            <c:url var="nextUrl" value="/receipts">
                                <c:param name="supplierId" value="${supplierId}" />
                                <c:param name="from" value="${from}" />
                                <c:param name="to" value="${to}" />
                                <c:param name="page" value="${receiptsPage.pageNumber + 1}" />
                                <c:param name="size" value="${receiptsPage.pageSize}" />
                            </c:url>
                            <a class="pagination__link" href="${nextUrl}">Sau →</a>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>

    <div class="btn-group" style="margin-top: 24px;">
        <a href="${pageContext.request.contextPath}/reports/suppliers" class="btn btn-outline">← Quay lại</a>
    </div>
</ui:layout>
