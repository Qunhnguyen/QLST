<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:layout title="Báo cáo nhà cung cấp" activeMenu="reports">
    <div class="section-title">
        <span class="section-eyebrow">Báo cáo</span>
        <h1>Báo cáo nhà cung cấp</h1>
        <p class="section-subtitle">Trang báo cáo đã được hợp nhất vào trung tâm thống kê mới.</p>
    </div>

    <div class="empty-state">
        <h3>Trang này đã được thay thế</h3>
        <p>Vui lòng sử dụng mục <strong>Báo cáo nhà cung cấp</strong> trong trung tâm thống kê để xem dữ liệu cập nhật.</p>
        <div style="margin-top: 24px;">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/reports/suppliers">Đi tới thống kê nhà cung cấp</a>
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/reports/menu">Trung tâm báo cáo</a>
        </div>
    </div>
</ui:layout>