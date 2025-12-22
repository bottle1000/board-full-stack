package com.boardfullstack.domain.comment.service;

import com.boardfullstack.domain.comment.dto.request.CommentCreateRequest;
import com.boardfullstack.domain.comment.dto.response.CommentResponse;
import com.boardfullstack.domain.comment.entity.Comment;
import com.boardfullstack.domain.comment.repository.CommentRepository;
import com.boardfullstack.domain.post.entity.Post;
import com.boardfullstack.domain.post.repository.PostRepository;
import com.boardfullstack.domain.user.entity.User;
import com.boardfullstack.domain.user.repository.UserRepository;
import com.boardfullstack.global.common.response.PagedResponse;
import com.boardfullstack.global.exception.CustomException;
import com.boardfullstack.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse createComment(Long principalId, Long postId, CommentCreateRequest request) {

        Post post = getActivePostOrThrow(postId);

        User user = getUserOrThrow(principalId);

        Comment comment = Comment.createComment(user, post, request.getContent());
        Comment saved = commentRepository.save(comment);

        return CommentResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {

        getActivePostOrThrow(postId);

        return commentRepository.findAllByPostIdAndDeletedFalseOrderByCreatedAtAsc(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void deleteComment(Long principalId, Long postId , Long commentId) {
        getActivePostOrThrow(postId);
        Comment comment = getActiveCommentOrThrow(commentId);

        validateCommentAuthorOrThrow(comment, principalId);

        commentRepository.delete(comment);
    }

    private Post getActivePostOrThrow(Long postId) {
        return postRepository.findPostByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private User getUserOrThrow(Long principalId) {
        return userRepository.findById(principalId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Comment getActiveCommentOrThrow(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateCommentAuthorOrThrow(Comment comment, Long principalId) {
        if (!comment.getAuthor().getId().equals(principalId)) {
            throw new CustomException(ErrorCode.COMMENT_FORBIDDEN);
        }
    }
}
