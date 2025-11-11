<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<c:set var="linePageMeta" value="${linesPage}" />
<c:url var="detailUrl" value="/receipts/detail">
    <c:param name="grId" value="${grId}" />
    <c:if test="${not empty supplierId}"><c:param name="supplierId" value="${supplierId}" /></c:if>
    <c:if test="${not empty from}"><c:param name="from" value="${from}" /></c:if>
    <c:if test="${not empty to}"><c:param name="to" value="${to}" /></c:if>
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

<ui:layout title="Hóa đơn phiếu nhập #${grId}" activeMenu="reports">
    <div class="section-title">
        <span class="section-eyebrow">Phiếu nhập</span>
        <h1>Hóa đơn phiếu nhập #${grId}</h1>
        <p class="section-subtitle">
            ${not empty supplierName ? supplierName : 'Không rõ nhà cung cấp'} ·
            ${not empty receiptDateDisplay ? receiptDateDisplay : 'Chưa có ngày xuất'}
        </p>
    </div>

    <c:choose>
        <c:when test="${invoice != null}">
            <div class="detail-grid">
                <div class="info-card">
                    <div class="info-card__header">
                        <h3>Thông tin hóa đơn</h3>
                        <span class="badge">#${invoice.id}</span>
                    </div>
                    <ul class="meta-list">
                        <li><span>Mã phiếu nhập</span><strong>#${grId}</strong></li>
                        <li><span>Ngày xuất hóa đơn</span><strong>${not empty invoiceDateDisplay ? invoiceDateDisplay : invoice.issueDate}</strong></li>
                        <li><span>Thuế</span><strong><fmt:formatNumber value="${invoice.tax}" pattern="#,##0" /> VND</strong></li>
                        <li><span>Chiết khấu</span><strong>${empty invoice.discountText ? 'Không có' : invoice.discountText}</strong></li>
                    </ul>
                </div>

                <div class="info-card">
                    <div class="info-card__header">
                        <h3>Tổng quan thanh toán</h3>
                    </div>
                    <ul class="meta-list">
                        <li><span>Tổng phụ</span><strong><fmt:formatNumber value="${subtotal}" pattern="#,##0" /> VND</strong></li>
                        <li><span>Tổng cộng</span><strong><fmt:formatNumber value="${grandTotal}" pattern="#,##0" /> VND</strong></li>
                        <li><span>Số lượng nhập</span><strong>${invoice.importedQuantity != null ? invoice.importedQuantity : lineCount}</strong></li>
                        <li><span>Khoảng lọc</span><strong>${not empty fromDisplay ? fromDisplay : from} → ${not empty toDisplay ? toDisplay : to}</strong></li>
                    </ul>
                    <div class="info-card__footer">
                        <button type="button" class="btn btn-primary" onclick="window.print()">In hóa đơn</button>
                    </div>
                </div>
            </div>

            <div class="table-caption">
                <div>
                    <h2>Chi tiết sản phẩm</h2>
                    <span>${lineCount} dòng · Tổng phụ <fmt:formatNumber value="${subtotal}" pattern="#,##0" /> VND</span>
                </div>
                <span class="badge badge-muted">Invoice #${invoice.id}</span>
            </div>

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
                        <tr class="table-total">
                            <td colspan="3" style="text-align: right; font-weight: 600;">Tổng cộng</td>
                            <td class="num"><strong><fmt:formatNumber value="${grandTotal}" pattern="#,##0" /> VND</strong></td>
                        </tr>
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
                            <c:url var="prevLineUrl" value="/receipts/invoice">
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
                            <c:url var="nextLineUrl" value="/receipts/invoice">
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
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <h3>Chưa có hóa đơn cho phiếu nhập này</h3>
                <p>Kiểm tra lại dữ liệu hoặc liên hệ bộ phận kế toán để bổ sung.</p>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="btn-group" style="margin-top: 24px;">
        <a href="${detailUrl}" class="btn btn-outline">← Quay lại</a>
        <a href="${backUrl}" class="btn btn-outline">Về danh sách phiếu</a>
    </div>
</ui:layout>
