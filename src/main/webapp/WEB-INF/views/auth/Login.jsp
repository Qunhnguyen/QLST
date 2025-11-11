<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="nextDestination" value="${not empty requestScope.next ? requestScope.next : param.next}" />
<c:set var="prefillUsername" value="${not empty requestScope.username ? requestScope.username : param.username}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="${ctx}/resources/css/app.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <div class="auth-card">
            <h1>Đăng nhập</h1>
            <p class="auth-subtitle">Truy cập hệ thống quản lý siêu thị.</p>

            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form method="post" action="${ctx}/login" class="auth-form">
                <c:if test="${not empty nextDestination}">
                    <input type="hidden" name="next" value="${nextDestination}">
                </c:if>
                <div class="form-field">
                    <label for="username">Tên đăng nhập</label>
                    <input id="username" name="username" type="text" placeholder="Nhập tên đăng nhập" required autofocus value="${prefillUsername}">
                </div>
                <div class="form-field">
                    <label for="password">Mật khẩu</label>
                    <input id="password" name="password" type="password" placeholder="Nhập mật khẩu" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Đăng nhập</button>
            </form>

            <p class="auth-footnote">
                Chưa có tài khoản? <a href="${ctx}/register">Đăng ký khách hàng</a>
            </p>
        </div>
    </div>
</body>
</html>
