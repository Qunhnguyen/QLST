<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<c:set var="user" value="${sessionScope.authUser}" />

<ui:layout title="Trang khách hàng" activeMenu="customer-home">
    <div class="section-title">
        <span class="section-eyebrow">Chào mừng trở lại</span>
        <h1>Xin chào, ${user != null ? user.displayName() : 'Khách hàng'}!</h1>
        <p class="section-subtitle">
            Bạn có thể xem nhanh các mặt hàng đang còn trong kho và thông tin chi tiết từng sản phẩm.
        </p>
    </div>

    <div class="card-grid">
        <div class="card card--feature">
            <h3>Tìm kiếm sản phẩm</h3>
            <p>Nhập từ khóa để lọc theo tên mặt hàng, giá bán và xem mô tả chi tiết từng sản phẩm.</p>
            <a href="${pageContext.request.contextPath}/customer/products" class="btn btn-primary">Bắt đầu tìm kiếm</a>
        </div>
        <div class="card card--feature muted">
            <h3>Đặt mua trực tuyến</h3>
            <p>Tính năng đặt mua sẽ sớm ra mắt. Vui lòng liên hệ nhân viên để được hỗ trợ trong thời gian chờ đợi.</p>
        </div>
    </div>
</ui:layout>
