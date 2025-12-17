package com.boardfullstack.domain.comment.repository;


import com.boardfullstack.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndDeletedFalse(Long id);

    @EntityGraph(attributePaths = "author")
    List<Comment> findAllByPostIdAndDeletedFalseOrderByCreatedAtAsc(Long postId);
}
