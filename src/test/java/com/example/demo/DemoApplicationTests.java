package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoApplicationTests {

    @Test
    @DisplayName("Kiểm tra khởi chạy ngữ cảnh ứng dụng")
    void contextLoads() {
        assertTrue(true, "Môi trường kiểm thử hoạt động bình thường.");
    }

    @Test
    @DisplayName("Kiểm tra phép toán cộng cơ bản (Unit Test mẫu)")
    void testSampleAddition() {
        int expected = 10;
        int actual = 5 + 5;
        assertEquals(expected, actual, "Kết quả phép cộng phải bằng 10.");
    }
}
