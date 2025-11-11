<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<c:set var="supplierBadge" value="${supplierCode}" />
<c:if test="${empty supplierBadge && not empty supplierId}">
    <c:set var="supplierBadge">NCC #${supplierId}</c:set>
</c:if>
<c:if test="${empty supplierBadge}">
    <c:set var="supplierBadge" value="---" />
</c:if>

<c:set var="linePageMeta" value="${linesPage}" />
<c:url var="invoiceUrl" value="/receipts/invoice">
    <c:param name="grId" value="${grId}" />
    <c:if test="${not empty supplierId}"><c:param name="supplierId" value="${supplierId}" /></c:if>
    <c:if test="${not empty from}"><c:param name="from" value="${from}" /></c:if>
    <c:if test="${not empty to}"><c:param name="to" value="${to}" /></c:if>
    <c:if test="${not empty supplierName}"><c:param name="supplierName" value="${supplierName}" /></c:if>
    <c:if test="${not empty supplierCode}"><c:param name="supplierCode" value="${supplierCode}" /></c:if>
    <c:if test="${not empty supplierTaxCode}"><c:param name="supplierTaxCode" value="${supplierTaxCode}" /></c:if>
    <c:if test="${not empty supplierAddress}"><c:param name="supplierAddress" value="${supplierAddress}" /></c:if>
    <c:if test="${not empty receiptDateDisplay}"><c:param name="receiptDate" value="${receiptDateDisplay}" /></c:if>
    <c:param name="listPage" value="${listPage}" />
    <c:param name="listSize" value="${listSize}" />
    <c:param name="linePage" value="${linePageMeta.pageNumber}" />
    <c:param name="lineSize" value="${linePageMeta.pageSize}" />
</c:url>
<c:url var="backUrl" value="/receipts">
    <c:if test="${not empty supplierId}"><c:param name="supplierId" value="${supplierId}" /></c:if>
    <c:if test="${not empty from}"><c:param name="from" value="${from}" /></c:if>
    <c:if test="${not empty to}"><c:param name="to" value="${to}" /></c:if>
    <c:param name="page" value="${listPage}" />
    <c:param name="size" value="${listSize}" />
</c:url>

<ui:layout title="Chi tiết phiếu nhập #${grId}" activeMenu="reports">
    <div class="section-title">
        <span class="section-eyebrow">Phiếu nhập</span>
        <h1>Chi tiết phiếu nhập #${grId}</h1>
        
    </div>

    <div class="detail-grid">
        <div class="info-card">
            <div class="info-card__header">
                <h3>Thông tin chung</h3>
                <span class="badge">${supplierBadge}</span>
            </div>
            <ul class="meta-list">
                <li><span>Mã phiếu</span><strong>#${grId}</strong></li>
                <li><span>Ngày tạo</span><strong>${not empty receiptDateDisplay ? receiptDateDisplay : '---'}</strong></li>
                <li><span>Nhà cung cấp</span><strong>${not empty supplierName ? supplierName : '---'}</strong></li>
                <li><span>Tổng giá trị</span><strong><fmt:formatNumber value="${totalAmount}" pattern="#,##0" /> VND</strong></li>
            </ul>
        </div>

        <div class="info-card">
            <div class="info-card__header">
                <h3>Thông tin lọc</h3>
            </div>
            <ul class="meta-list">
                <li><span>Thời gian</span><strong>${not empty fromDisplay ? fromDisplay : from} → ${not empty toDisplay ? toDisplay : to}</strong></li>
                <li><span>Số dòng sản phẩm</span><strong>${lineCount}</strong></li>
            </ul>
        </div>
    </div>

    <div class="table-caption">
        <div>
            <h2>Danh sách sản phẩm</h2>
            <span>${lineCount} mặt hàng</span>
        </div>
        <span class="badge badge-muted">Phiếu #${grId}</span>
    </div>

    <c:choose>
        <c:when test="${empty lines}">
            <div class="empty-state">
                <h3>Phiếu nhập không có sản phẩm</h3>
                <p>Vui lòng kiểm tra lại cấu hình dữ liệu.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Sản phẩm</th>
                            <th class="num">Số lượng</th>
                            <th class="num">Đơn giá</th>
                            <th class="num">Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="line" items="${lines}">
                            <tr>
                                <td style="font-weight: 600;">${line.productName}</td>
                                <td class="num">${line.quantityReceived}</td>
                                <td class="num"><fmt:formatNumber value="${line.unitPurchasePrice}" pattern="#,##0" /> VND</td>
                                <td class="num"><fmt:formatNumber value="${line.lineTotal}" pattern="#,##0" /> VND</td>
                            </tr>
                        </c:forEach>
                    
                    </tbody>
                </table>
            </div>
            <c:if test="${lineCount > 0}">
                <div class="pagination">
                    <div class="pagination__summary">
                        Trang ${linePageMeta.pageNumber}/${linePageMeta.totalPages} · Hiển thị ${linePageMeta.startItem}-${linePageMeta.endItem} / ${lineCount}
                    </div>
                    <div class="pagination__controls">
                        <c:if test="${linePageMeta.hasPrevious}">
                            <c:url var="prevLineUrl" value="/receipts/detail">
                                <c:param name="grId" value="${grId}" />
                                <c:if test="${not empty supplierId}"><c:param name="supplierId" value="${supplierId}" /></c:if>
                                <c:if test="${not empty from}"><c:param name="from" value="${from}" /></c:if>
                                <c:if test="${not empty to}"><c:param name="to" value="${to}" /></c:if>
                                <c:param name="listPage" value="${listPage}" />
                                <c:param name="listSize" value="${listSize}" />
                                <c:param name="linePage" value="${linePageMeta.pageNumber - 1}" />
                                <c:param name="lineSize" value="${linePageMeta.pageSize}" />
                            </c:url>
                            <a class="pagination__link" href="${prevLineUrl}">← Trước</a>
                        </c:if>
                        <c:if test="${linePageMeta.hasNext}">
                            <c:url var="nextLineUrl" value="/receipts/detail">
                                <c:param name="grId" value="${grId}" />
                                <c:if test="${not empty supplierId}"><c:param name="supplierId" value="${supplierId}" /></c:if>
                                <c:if test="${not empty from}"><c:param name="from" value="${from}" /></c:if>
                                <c:if test="${not empty to}"><c:param name="to" value="${to}" /></c:if>
                                <c:param name="listPage" value="${listPage}" />
                                <c:param name="listSize" value="${listSize}" />
                                <c:param name="linePage" value="${linePageMeta.pageNumber + 1}" />
                                <c:param name="lineSize" value="${linePageMeta.pageSize}" />
                            </c:url>
                            <a class="pagination__link" href="${nextLineUrl}">Sau →</a>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>

    <div class="btn-group" style="margin-top: 24px;">
        <a href="${invoiceUrl}" class="btn btn-primary">Xem hóa đơn</a>
        <a href="${backUrl}" class="btn btn-outline">← Quay lại</a>
    </div>
</ui:layout>
