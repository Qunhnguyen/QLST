<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<c:set var="product" value="${product}" />
<c:set var="currentUser" value="${sessionScope.authUser}" />

<ui:layout title="Chi tiết sản phẩm" activeMenu="customer-products">
    <div class="section-title">
        <span class="section-eyebrow">Khách hàng / Tìm kiếm sản phẩm</span>
        <h1>${product.name}</h1>
        <p class="section-subtitle">Thông tin chi tiết về mặt hàng đang chọn.</p>
    </div>

    <div class="product-detail">
        <div class="product-detail__image">
            <c:set var="img" value="${product.imageUrl}" />
            <c:set var="imgSrc" value="${img}" />
            <c:if test="${not empty img && !fn:startsWith(fn:toLowerCase(img), 'http')}">
                <c:set var="imgSrc" value="${pageContext.request.contextPath}${img}" />
            </c:if>
            <c:choose>
                <c:when test="${not empty imgSrc}">
                    <div class="product-detail__image-holder" style="background-image: url('${imgSrc}');"></div>
                </c:when>
                <c:otherwise>
                    <div class="product-detail__image-holder no-image"></div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="product-detail__info">
            <ul class="meta-list">
                <li><span>Mã sản phẩm</span><strong>#${product.id}</strong></li>
                <li><span>Thương hiệu</span><strong><c:out value="${product.brand}" default="Đang cập nhật"/></strong></li>
                <li><span>Giá bán</span><strong><fmt:formatNumber value="${product.salePrice}" pattern="#,##0" /> VND</strong></li>
                <li><span>Tồn kho</span><strong>${product.stockQuantity}</strong></li>
            </ul>
            <div class="product-detail__description">
                <h3>Mô tả sản phẩm</h3>
                <p><c:out value="${empty product.description ? 'Thông tin đang được cập nhật.' : product.description}"/></p>
            </div>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/customer/products" class="btn btn-outline">← Quay lại</a>
                    <c:choose>
                        <c:when test="${empty currentUser}">
                            <c:url var="loginUrl" value="/login">
                                <c:param name="next" value="${'/customer/product?id='}${product.id}" />
                            </c:url>
                            <a class="btn btn-primary" href="${loginUrl}">Mua</a>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-primary" disabled>Đặt mua (đang phát triển)</button>
                        </c:otherwise>
                    </c:choose>
            </div>
        </div>
    </div>
</ui:layout>
