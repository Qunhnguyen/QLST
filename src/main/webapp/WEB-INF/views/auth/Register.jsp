<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký khách hàng</title>
    <link rel="stylesheet" href="${ctx}/resources/css/app.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <div class="auth-card">
            <h1>Tạo tài khoản khách hàng</h1>
            <p class="auth-subtitle">Đăng ký để theo dõi đơn hàng và chương trình ưu đãi.</p>

            <c:if test="${not empty errors}">
                <div class="alert alert-danger">
                    <ul>
                        <c:forEach var="err" items="${errors}">
                            <li>${err}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <form method="post" action="${ctx}/register" class="auth-form">
                <div class="form-field">
                    <label for="username">Tên đăng nhập *</label>
                    <input id="username" name="username" type="text" value="${username}" required minlength="4">
                </div>
                <div class="form-field">
                    <label for="fullName">Họ và tên</label>
                    <input id="fullName" name="fullName" type="text" value="${fullName}">
                </div>
                <div class="form-field">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" value="${email}">
                </div>
                <div class="form-field">
                    <label for="address">Địa chỉ</label>
                    <input id="address" name="address" type="text" value="${address}">
                </div>
                <div class="form-field">
                    <label for="phone">Số điện thoại</label>
                    <input id="phone" name="phone" type="text" value="${phone}">
                </div>
                <div class="form-field">
                    <label for="password">Mật khẩu *</label>
                    <input id="password" name="password" type="password" required minlength="6">
                </div>
                <div class="form-field">
                    <label for="confirmPassword">Xác nhận mật khẩu *</label>
                    <input id="confirmPassword" name="confirmPassword" type="password" required minlength="6">
                </div>
                <button type="submit" class="btn btn-primary btn-block">Đăng ký</button>
            </form>

            <p class="auth-footnote">
                Đã có tài khoản? <a href="${ctx}/login">Đăng nhập</a>
            </p>
        </div>
    </div>
</body>
</html>
