🚀 Code Judge (Pet project)  - Microservices Architecture

📖 Giới thiệu (Introduction)
Code Judge là một hệ thống chấm điểm code tự động (tương tự LeetCode, HackerRank) được xây dựng theo kiến trúc Microservices. Dự án cho phép người dùng đăng nhập, xem danh sách bài tập, nộp mã nguồn và nhận kết quả chấm điểm (Accepted, Wrong Answer, Time Limit Exceeded,...) một cách tự động và bất đồng bộ.

🏗️ Kiến trúc Hệ thống (Architecture)
Hệ thống được chia nhỏ thành các dịch vụ độc lập, giao tiếp với nhau thông qua mạng nội bộ và Message Queue:

eureka-server (Port 8761): Service Registry. Đóng vai trò là "danh bạ" định tuyến cho toàn bộ hệ thống.

api-gateway (Port 8080): Cửa ngõ duy nhất (Entry point) giao tiếp với Frontend/Client. Chịu trách nhiệm phân luồng (Routing) và xác thực JWT (Authentication Filter).

user-service (Port 8081): Quản lý thông tin người dùng và cấp phát JWT Token.

problem-service (Port 8082): Quản lý đề bài và các bộ Test Cases.

submission-service (Port 8083): Tiếp nhận mã nguồn từ người dùng, lưu trữ trạng thái ban đầu và đẩy yêu cầu chấm điểm vào Message Queue.

judge-service: Lắng nghe Message từ RabbitMQ, gọi nội bộ sang problem-service (qua Eureka) để lấy Test Case, thực thi mã nguồn và cập nhật kết quả.

common-service: Thư viện dùng chung chứa các DTO chuẩn (ApiResponse) và cấu hình xử lý lỗi tập trung (GlobalExceptionHandler).

✨ Tính năng Nổi bật (Key Features)
Bảo mật tập trung (Centralized Security): Xác thực JWT ở tầng Gateway. Các Service bên trong hoàn toàn không chứa logic bảo mật, chỉ xác thực qua header X-Username được Gateway truyền xuống.

Chuẩn hóa Giao tiếp (Standardized API): Mọi API đều trả về một format chuẩn duy nhất (Generic API Response) và lỗi được gom về một mối xử lý.

Xử lý Bất đồng bộ (Asynchronous Processing): Áp dụng RabbitMQ để xử lý hàng đợi chấm bài, đảm bảo hệ thống không bị nghẽn khi có hàng ngàn user nộp bài cùng lúc.

Cân bằng tải nội bộ (Client-side Load Balancing): Các service giao tiếp với nhau bằng tên (ví dụ: lb://problem-service) thay vì Hardcode IP tĩnh nhờ @LoadBalanced RestTemplate.

🛠️ Công nghệ sử dụng (Tech Stack)
Backend Core: Java 17, Spring Boot 3.2.5

Microservices Ecosystem: Spring Cloud Gateway, Netflix Eureka

Security: JJWT (0.12.6)

Message Broker: RabbitMQ

Database: MySQL & Spring Data JPA

Utilities: Lombok, Maven

🚀 Hướng dẫn Cài đặt và Chạy dự án (How to run)
1. Yêu cầu hệ thống (Prerequisites)
Java 17 (JDK 17)

Maven 3.x

MySQL Server (Chạy ở cổng 3306)

RabbitMQ (Local hoặc CloudAMQP)

2. Cấu hình Database
Đảm bảo bạn đã tạo các database trống tương ứng trong MySQL. Hibernate sẽ tự động tạo bảng (ddl-auto: update):

SQL
CREATE DATABASE oj_user_db;
CREATE DATABASE oj_problem_db;
CREATE DATABASE oj_submission_db;
3. Cài đặt thư viện dùng chung (Common Service)
Vì các service khác phụ thuộc vào common-service, bạn bắt buộc phải build nó đầu tiên:

Bash
cd common-service
mvn clean install
4. Khởi chạy các Services
Thứ tự khởi chạy cực kỳ quan trọng trong kiến trúc Microservices. Hãy chạy lần lượt theo thứ tự sau:

Chạy eureka-server (Đợi load xong hoàn toàn).

Chạy api-gateway.

Chạy các service còn lại: user-service, problem-service, submission-service, judge-service.

Mẹo: Bạn có thể kiểm tra danh bạ các service đã đăng ký thành công hay chưa bằng cách truy cập: http://localhost:8761
