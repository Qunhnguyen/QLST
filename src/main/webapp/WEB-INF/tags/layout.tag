<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="title" required="false" %>
<%@ attribute name="activeMenu" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="currentUser" value="${sessionScope.authUser}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${empty title ? 'Quản lý Siêu Thị' : title}"/></title>
    <link rel="stylesheet" href="${ctx}/resources/css/app.css">
</head>
<body>
    <div class="app-shell">
        <header class="app-header">
            <div class="app-brand">Quản lý Siêu Thị</div>
            <c:if test="${not empty currentUser}">
                <nav class="app-nav">
                    <c:choose>
                        <c:when test="${currentUser.role == 'ADMIN'}">
                            <a href="${ctx}/main" class="app-nav__link ${activeMenu == 'main' ? 'is-active' : ''}">Bảng điều khiển</a>
                            <a href="${ctx}/reports/menu" class="app-nav__link ${activeMenu == 'reports' ? 'is-active' : ''}">Thống kê NCC</a>
                        </c:when>
                        <c:when test="${currentUser.role == 'CUSTOMER'}">
                            <a href="${ctx}/customer/home" class="app-nav__link ${activeMenu == 'customer-home' ? 'is-active' : ''}">Trang khách hàng</a>
                            <a href="${ctx}/customer/products" class="app-nav__link ${activeMenu == 'customer-products' ? 'is-active' : ''}">Tìm sản phẩm</a>
                        </c:when>
                    </c:choose>
                </nav>
            </c:if>
            <div class="app-header__actions">
                <c:choose>
                    <c:when test="${not empty currentUser}">
                        <span class="user-pill">${currentUser.displayName}</span>
                        <a href="${ctx}/logout" class="btn btn-outline">Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/login" class="btn btn-outline">Đăng nhập</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>
        <main class="app-main">
            <section class="card">
                <jsp:doBody/>
            </section>
        </main>
    </div>
</body>
</html>
