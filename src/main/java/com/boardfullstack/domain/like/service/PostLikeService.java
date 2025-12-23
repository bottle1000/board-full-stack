package com.boardfullstack.domain.like.service;

import com.boardfullstack.domain.like.entity.PostLike;
import com.boardfullstack.domain.like.repository.PostLikeRepository;
import com.boardfullstack.domain.post.entity.Post;
import com.boardfullstack.domain.post.repository.PostRepository;
import com.boardfullstack.domain.user.entity.User;
import com.boardfullstack.domain.user.repository.UserRepository;
import com.boardfullstack.global.exception.CustomException;
import com.boardfullstack.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void likePost(Long principalId, Long postId) {
        User user = getUserOrThrow(principalId);
        Post post = getActivePostOrThrow(postId);

        if (postLikeRepository.existsByPostIdAndUserId(postId, principalId)) {
            throw new CustomException(ErrorCode.POST_LIKE_ALREADY_EXISTS);
        }

        try {
            PostLike postLike = PostLike.create(post, user);
            postLikeRepository.save(postLike);
            post.increaseLikeCount();
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.POST_LIKE_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void unlikePost(Long principalId, Long postId) {
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, principalId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_LIKE_NOT_FOUND));

        Post post = getActivePostOrThrow(postId);

        postLikeRepository.delete(postLike);
        post.decreaseLikeCount();
    }

    private Post getActivePostOrThrow(Long postId) {
        return postRepository.findPostByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private User getUserOrThrow(Long principalId) {
        return userRepository.findById(principalId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
