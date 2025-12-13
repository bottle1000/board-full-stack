package com.boardfullstack.domain.post.repository;

import com.boardfullstack.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostByIdAndDeletedFalse(Long id);
}
