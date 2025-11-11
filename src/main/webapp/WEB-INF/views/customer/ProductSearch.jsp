<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>

<c:set var="keyword" value="${empty keyword ? '' : keyword}" />

<ui:layout title="Tìm kiếm sản phẩm" activeMenu="customer-products">
    <div class="section-title">
        <span class="section-eyebrow">Khách hàng / Tìm kiếm sản phẩm</span>
        <h1>Tìm kiếm sản phẩm</h1>
        <p class="section-subtitle">Nhập tên mặt hàng để tra cứu nhanh các sản phẩm hiện có.</p>
    </div>

    <form class="product-search-bar" method="get" action="${pageContext.request.contextPath}/customer/products">
        <input type="text" name="q" placeholder="Nhập tên sản phẩm..." value="${keyword}" />
        <button type="submit" class="btn btn-primary">Tìm</button>
        <c:if test="${not empty keyword}">
            <a href="${pageContext.request.contextPath}/customer/products" class="btn btn-outline">Xóa bộ lọc</a>
        </c:if>
    </form>

    <c:if test="${param.notfound == '1'}">
        <div class="alert alert-danger" style="margin-bottom: 18px;">
            Sản phẩm bạn tìm không còn tồn tại hoặc đã bị xóa.
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty products}">
            <div class="empty-state">
                <h3>Không tìm thấy sản phẩm phù hợp</h3>
                <p>Hãy thử nhập từ khóa khác hoặc kiểm tra lại chính tả.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="product-summary-row">
                <span><strong>${fn:length(products)}</strong> sản phẩm được tìm thấy</span>
                <c:if test="${not empty keyword}">
                    <span class="badge badge-muted">Từ khóa: "${keyword}"</span>
                </c:if>
            </div>
            <div class="product-grid">
                <c:forEach var="item" items="${products}">
                    <a class="product-card" href="${pageContext.request.contextPath}/customer/product?id=${item.id}">
                        <c:set var="img" value="${item.imageUrl}" />
                        <c:set var="imgSrc" value="${img}" />
                        <c:if test="${not empty img && !fn:startsWith(fn:toLowerCase(img), 'http')}">
                            <c:set var="imgSrc" value="${pageContext.request.contextPath}${img}" />
                        </c:if>
                        <c:choose>
                            <c:when test="${not empty imgSrc}">
                                <div class="product-card__image" style="background-image: url('${imgSrc}');"></div>
                            </c:when>
                            <c:otherwise>
                                <div class="product-card__image no-image"></div>
                            </c:otherwise>
                        </c:choose>
                        <div class="product-card__body">
                            <h3>${item.name}</h3>
                            <p class="product-card__brand"><c:out value="${item.brand}" default=""/></p>
                            <div class="product-card__meta">
                                <span class="price"><fmt:formatNumber value="${item.salePrice}" pattern="#,##0" /> VND</span>
                                
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</ui:layout>
