package com.boardfullstack.domain.comment.service;

import com.boardfullstack.domain.comment.dto.request.CommentCreateRequest;
import com.boardfullstack.domain.comment.dto.response.CommentResponse;
import com.boardfullstack.domain.comment.entity.Comment;
import com.boardfullstack.domain.comment.repository.CommentRepository;
import com.boardfullstack.domain.post.entity.Post;
import com.boardfullstack.domain.post.repository.PostRepository;
import com.boardfullstack.domain.user.entity.User;
import com.boardfullstack.domain.user.repository.UserRepository;
import com.boardfullstack.global.exception.CustomException;
import com.boardfullstack.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse createComment(Long principalId, Long postId, CommentCreateRequest request) {

        Post post = postRepository.findPostByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(principalId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.createComment(user, post, request.getContent());
        Comment saved = commentRepository.save(comment);

        return CommentResponse.from(saved);
    }
}
