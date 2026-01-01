package com.boardfullstack;

import com.boardfullstack.domain.like.service.PostLikeService;
import com.boardfullstack.domain.post.entity.Post;
import com.boardfullstack.domain.post.repository.PostRepository;
import com.boardfullstack.domain.post.service.PostService;
import com.boardfullstack.domain.user.entity.User;
import com.boardfullstack.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PostLikeConcurrencyTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("좋아요 동시성 테스트 : 100명이 동시에 누르면 100개가 되어야 한다.")
    @Rollback(value = true)
    void likeConcurrencyTest() throws InterruptedException {
        User testAuthor = userRepository.save(User.createUser("testAuthor5@test.com", "1234", "testAuthor"));
        Post post = postRepository.save(Post.create(testAuthor, "동시성 테스트", "내용"));

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            users.add(userRepository.save(User.createUser("test" + i + "@t1est"+ i +".com", "1234", "user" + i)));
        }

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (User user : users) {
            executorService.submit(() -> {
                try {
                    postLikeService.likePost(user.getId(), post.getId());
                } catch (Exception e) {

                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Post findPost = postRepository.findById(post.getId()).orElseThrow();

        System.out.println("기대값 : 100, 실제값: " + findPost.getLikeCount());

        assertThat(findPost.getLikeCount()).isEqualTo(100);
    }
}
