package com.boardfullstack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class DataBulkInsertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저 1만명 + 게시글 10만개 넣기")
    @Rollback(value = false)
    void bulkInsert() {

        String sqlUser = "INSERT INTO users (email, password, name, role, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        String defaultPassword = passwordEncoder.encode("1234");
        LocalDateTime now = LocalDateTime.now();

        // 1만명 데이터 준비
        List<Object[]> userArgs = IntStream.range(0, 10000)
                .mapToObj(i -> new Object[]{
                        "user" + i + "@test.com",
                        defaultPassword,
                        "tester" + i,
                        "USER",
                        "ACTIVE",
                        Timestamp.valueOf(now),
                })
                .toList();

        jdbcTemplate.batchUpdate(sqlUser, userArgs);
        System.out.println("유저 1만명 삽입 완료");

        // 게시글 10만개 생성 (작성자는 랜덤)
        String sqlPost = "INSERT INTO post (author_id, title, content, is_deleted, like_count, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> postArgs = IntStream.range(0, 100000)
                .mapToObj(i -> new Object[]{
                        (long) (Math.random() * 10000) + 1,
                        "테스트 게시글 제목 " + i,
                        "테스트 게시글 내용입니다. " + i,
                        false,
                        0,
                        Timestamp.valueOf(now.minusMinutes(i)),
                        Timestamp.valueOf(now)
                })
                .toList();

        jdbcTemplate.batchUpdate(sqlPost, postArgs);
        System.out.println("게시글 10만개 삽입 완료");
    }
}
