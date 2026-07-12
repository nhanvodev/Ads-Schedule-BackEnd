# Ad Scheduler Backend

Backend quản lý lịch chiếu quảng cáo trên màn hình Android Box.

## Cấu hình

- Java 25, Spring Boot 4.1.0, Maven
- Spring Web MVC, Spring Data JPA, Spring Validation, Spring WebSocket
- Lombok
- Database: Microsoft SQL Server Express
- Swagger UI (springdoc-openapi)

## Nghiệp vụ

- Admin upload video quảng cáo, lưu trên server (không lưu ở client/box).
- Admin tạo **lịch chiếu (schedule)**: chọn khung giờ (`startTime` → `endTime`) và chọn nhiều video để chiếu theo thứ tự (playlist).
- Hết `endTime`, lịch tự động dừng để nhường cho lịch kế tiếp.
- Admin có thể tắt đột ngột 1 lịch đang chiếu bất cứ lúc nào.
- Playlist trong 1 khung giờ lặp lại (loop) liên tục tới khi hết `endTime`.

Lưu ý
1. **Scheduler tự động**: dùng `@Scheduled` quét DB mỗi 10 giây để chuyển trạng thái lịch (`PENDING → PLAYING → FINISHED`) theo thời gian thực và tự bắn lệnh PLAY/STOP qua WebSocket.

2. Vì không viết app Android thật, dùng **Postman WebSocket client** (hoặc file `box-simulator.html` tự tạo riêng để test có hình ảnh video thật) để giả lập box nhận lệnh.

## Cài đặt

### 1. Clone project

```bash
git clone <repo-url>
cd ads_schedule
```

### 2. Tạo database

Mở SSMS, tạo database mới tên `ads_schedule` (rỗng, không cần tạo bảng — Hibernate tự tạo lúc chạy app lần đầu).

### 3. Cấu hình kết nối DB

Mở `src/main/resources/application.properties`, cập nhật:

```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=ads_schedule;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=<mat_khau_sa_cua_ban>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect

spring.servlet.multipart.max-file-size=500MB
spring.servlet.multipart.max-request-size=500MB

app.storage.video-dir=./video-storage
```

### 4. Build & chạy

App chạy ở `http://localhost:8080`. Hibernate sẽ tự tạo các bảng `videos`, `schedules`, `schedule_videos` trong lần chạy đầu tiên.

Sau khi chạy app, mở:

```
http://localhost:8080/swagger-ui/index.html
```

## Danh sách API chính

### Video

| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/api/videos` | Upload video (multipart: `title`, `file`) |
| GET | `/api/videos` | Danh sách video |
| GET | `/api/videos/{id}` | Chi tiết 1 video |
| DELETE | `/api/videos/{id}` | Xoá video (cả DB record lẫn file vật lý) |
| GET | `/api/videos/{id}/stream` | Stream video, hỗ trợ HTTP Range |

### Schedule

| Method | Endpoint | Mô tả |
|---|---|---|
| POST | `/api/schedules` | Tạo lịch chiếu (kèm danh sách video + thứ tự) |
| GET | `/api/schedules` | Danh sách lịch |
| GET | `/api/schedules/{id}` | Chi tiết 1 lịch (kèm playlist) |
| GET | `/api/schedules/{id}/playlist` | Playlist theo thứ tự của 1 lịch |
| PATCH | `/api/schedules/{id}/stop` | Tắt thủ công 1 lịch đang chiếu |
| DELETE | `/api/schedules/{id}` | Xoá lịch |

## WebSocket

Kết nối: `ws://localhost:8080/ws/box?deviceId=box-01`

Nhận 2 loại lệnh dạng JSON:

```json
{ "type": "PLAY", "scheduleId": 1, "endTime": "2026-07-13T09:00:00", "loop": true, "playlist": [...] }
```

```json
{ "type": "STOP", "scheduleId": 1, "reason": "END_TIME_REACHED" }
```

Test bằng Postman WebSocket client, hoặc mở file giả lập box (file `box-simulator.html`)

## Việc chưa hoàn thiện

- Spring Security + JWT cho admin (đang tạm bỏ qua, ưu tiên hoàn thiện các module chính trước)
- Nâng cấp scheduler từ `@Scheduled` sang Quartz nếu cần độ chính xác cao hơn / lịch phức tạp hơn
