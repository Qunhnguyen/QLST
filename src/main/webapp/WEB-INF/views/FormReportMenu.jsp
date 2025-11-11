<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<ui:layout title="Trung tâm báo cáo" activeMenu="reports">
    <div class="section-title">
        <span class="section-eyebrow">Báo cáo &amp; phân tích</span>
        <h1>Trung tâm báo cáo</h1>
        <p class="section-subtitle">Chọn báo cáo để xem dữ liệu chi tiết theo từng nhóm chức năng.</p>
    </div>

    <div class="summary-grid">
        <div class="summary-card">
            <span class="summary-card__label">Báo cáo nổi bật</span>
            <span class="summary-card__value" style="font-size: 1rem;">Nhà cung cấp theo doanh thu</span>
            <p class="summary-card__hint">Phân tích doanh thu, số lượng nhập và số lần giao dịch của từng nhà cung cấp.</p>
        </div>
        <div class="summary-card">
            <span class="summary-card__label">Chu kỳ cập nhật</span>
            <span class="summary-card__value" style="font-size: 1rem;">Hằng ngày</span>
            <p class="summary-card__hint">Dữ liệu được đồng bộ cuối mỗi ngày làm việc.</p>
        </div>
        <div class="summary-card">
            <span class="summary-card__label">Xuất báo cáo</span>
            <span class="summary-card__value" style="font-size: 1rem;">PDF · Excel</span>
            <p class="summary-card__hint">Xuất nhanh để chia sẻ cho các phòng ban.</p>
        </div>
    </div>

    <div class="action-grid">
        <a class="action-card" href="${pageContext.request.contextPath}/reports/suppliers">
            <div class="action-card__meta">
                <span class="badge">Nổi bật</span>
                <h3>Báo cáo nhà cung cấp</h3>
                <p>Xem chi tiết số lượng nhập, doanh thu và số lần giao dịch của từng nhà cung cấp.</p>
            </div>
            <span class="action-card__cta">Mở báo cáo →</span>
        </a>
        <a class="action-card action-card--disabled" href="#" onclick="return false;">
            <div class="action-card__meta">
                <span class="badge badge-muted">Sắp ra mắt</span>
                <h3>Báo cáo doanh thu theo ngày</h3>
                <p>Đang phát triển: theo dõi doanh thu và chi phí theo từng ngày cụ thể.</p>
            </div>
            <span class="action-card__cta">Đang phát triển…</span>
        </a>
        <a class="action-card action-card--disabled" href="#" onclick="return false;">
            <div class="action-card__meta">
                <span class="badge badge-muted">Sắp ra mắt</span>
                <h3>Báo cáo tồn kho</h3>
                <p>Giúp kiểm soát lượng tồn để tối ưu nhập hàng.</p>
            </div>
            <span class="action-card__cta">Đang phát triển…</span>
        </a>
    </div>

    <div class="btn-group" style="margin-top: 24px;">
        <a href="${pageContext.request.contextPath}/main" class="btn btn-outline">← Quay lại trang chính</a>
    </div>
</ui:layout>
