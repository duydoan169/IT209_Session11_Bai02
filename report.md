# Báo cáo Thực hành - Bài 2: Biên dịch đóng gói tệp ứng dụng .jar với Gradle và lưu trữ Artifacts

## 1. Mục tiêu bài lab
- Thực thi câu lệnh biên dịch và đóng gói ứng dụng Spring Boot thành tệp thực thi `.jar` bằng Gradle Wrapper (`./gradlew bootJar`).
- Lưu trữ thành phẩm biên dịch (`.jar`) từ thư mục `build/libs/` lên GitHub Actions bằng Action `actions/upload-artifact@v4`.
- Tự động hóa toàn bộ quy trình: Checkout mã nguồn $\rightarrow$ Cài đặt Java 17 $\rightarrow$ Chạy JUnit Test $\rightarrow$ Đóng gói `.jar` $\rightarrow$ Tải lên Artifacts.

---

## 2. Cấu trúc thư mục dự án
```text
Lesson2/
├── .github/
│   └── workflows/
│       └── ci.yml               # File cấu hình GitHub Actions Workflow (Bài 2)
├── homework/
│   └── session_11/
│       └── ex2/
│           └── ci.yml           # Tệp nộp bài theo đường dẫn gợi ý
├── src/
│   ├── main/java/com/example/demo/
│   │   └── DemoApplication.java # Lớp khởi chạy Spring Boot
│   └── test/java/com/example/demo/
│       └── DemoApplicationTests.java # Lớp kiểm thử JUnit 5
├── build.gradle                 # Cấu hình Gradle dependencies và plugins
├── settings.gradle              # Cấu hình tên project
├── gradlew                      # Gradle wrapper script (Linux/macOS)
├── gradlew.bat                  # Gradle wrapper script (Windows)
└── report.md                    # Báo cáo thực hiện bài lab
```

---

## 3. Nội dung tệp cấu hình `.github/workflows/ci.yml`

```yaml
name: Java Spring Boot CI (Gradle)

on:
  push:
    branches: [ "main" ]

jobs:
  test:
    name: Run Unit Tests & Build JAR
    runs-on: ubuntu-latest

    steps:
      - name: Checkout mã nguồn
        uses: actions/checkout@v5

      - name: Thiết lập môi trường Java JDK 17
        uses: actions/setup-java@v5
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cấp quyền cho Gradle Wrapper
        run: chmod +x gradlew

      - name: Thực thi JUnit Test với Gradle
        run: ./gradlew test

      - name: Đóng gói tệp JAR với Gradle
        run: ./gradlew bootJar

      - name: Lưu trữ thành phẩm JAR Artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-jar
          path: build/libs/*.jar
```

---

## 4. Giải thích các bước bổ sung so với Bài 1 (EX1)

1. **Bước 1: Đóng gói tệp JAR với Gradle**
   ```yaml
   - name: Đóng gói tệp JAR với Gradle
     run: ./gradlew bootJar
   ```
   - Câu lệnh `bootJar` do plugin `org.springframework.boot` cung cấp, biên dịch mã nguồn và đóng gói tất cả các dependencies cùng file class thành một tệp thực thi độc lập (executable fat JAR) đặt tại thư mục `build/libs/` (ví dụ: `build/libs/spring-boot-gradle-ci-0.0.1-SNAPSHOT.jar`).

2. **Bước 2: Lưu trữ thành phẩm JAR Artifact**
   ```yaml
   - name: Lưu trữ thành phẩm JAR Artifact
     uses: actions/upload-artifact@v4
     with:
       name: app-jar
       path: build/libs/*.jar
   ```
   - Sử dụng Action chính thức `actions/upload-artifact@v4` để thu thập các tệp `.jar` trong thư mục `build/libs/` và lưu trữ lên hệ thống GitHub Actions dưới tên artifact `app-jar`.
   - Artifact này sẽ tồn tại và có thể tải về trực tiếp từ trang **Summary** của Workflow Run sau khi hoàn tất.

---

## 5. Hướng dẫn kiểm tra và nộp bài

### Bước 1: Đẩy mã nguồn lên GitHub
Mở terminal tại thư mục dự án và thực hiện các lệnh git:
```bash
git add .
git commit -m "feat: add bootJar step and upload-artifact for app-jar"
git push origin main
```

### Bước 2: Kiểm tra kết quả trên GitHub Actions
1. Vào GitHub Repository $\rightarrow$ chọn tab **Actions**.
2. Chọn luồng chạy workflow mới nhất vừa được kích hoạt bởi commit trên nhánh `main`.
3. Kiểm tra các bước trong Job:
   - `Run Unit Tests` $\rightarrow$ Pass.
   - `Đóng gói tệp JAR với Gradle` (`./gradlew bootJar`) $\rightarrow$ Pass.
   - `Lưu trữ thành phẩm JAR Artifact` (`upload-artifact@v4`) $\rightarrow$ Pass.
4. Chuyển sang phần **Summary** của workflow:
   - Cuộn xuống mục **Artifacts** ở dưới cùng trang Summary.
   - Xác nhận có xuất hiện mục **`app-jar`** (dạng file nén `.zip` chứa file `.jar`).
   - Nhấn tải về để kiểm tra file `.jar` có thể mở hoặc chạy bình thường (`java -jar ...jar`).

### Bước 3: Sản phẩm nộp bài
- Tệp kịch bản: `homework/session_11/ex2/ci.yml` (hoặc `.github/workflows/ci.yml`).
- Ảnh chụp màn hình: Trang **Summary** trên tab Actions hiển thị Job thành công (tích xanh) và mục **Artifacts** hiển thị tệp `app-jar`.
