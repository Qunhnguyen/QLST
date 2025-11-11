-- Script này giúp chuẩn hóa bảng `member` hiện có để dùng chung cho đăng nhập
-- khách hàng và quản trị. Chạy từng câu lệnh trong database `mydb`.

-- 1) Thêm cột phân quyền (nếu chưa có)
ALTER TABLE member
    ADD COLUMN IF NOT EXISTS role ENUM('ADMIN','CUSTOMER') NOT NULL DEFAULT 'CUSTOMER';

-- 2) Thêm cột thời gian tạo để tiện thống kê (tùy chọn)
ALTER TABLE member
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP;

-- 3) (Tùy chọn) chuẩn hóa dữ liệu mẫu: đánh dấu một tài khoản quản trị
-- UPDATE member SET role = 'ADMIN' WHERE username = 'admin';

-- 4) Khi thêm tài khoản mới, nhớ lưu mật khẩu bằng chuỗi băm trả về từ
-- util PasswordUtil.hashPassword.newline
