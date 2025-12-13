package com.boardfullstack.domain.post.service;

import com.boardfullstack.domain.post.dto.request.PostCreateRequest;
import com.boardfullstack.domain.post.dto.request.PostUpdateRequest;
import com.boardfullstack.domain.post.dto.response.PostResponse;
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
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostResponse createPost(Long authorId, PostCreateRequest request){
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.create(user, request.getTitle(), request.getContent());
        Post saved = postRepository.save(post);

        return PostResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findPostByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse updatePost(Long principalId, Long postId, PostUpdateRequest request) {
        Post post = getActivePostOrThrow(postId);
        validateAuthorOrThrow(post, principalId);

        post.update(request.getTitle(), request.getContent());
        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long principalId, Long postId) {
        Post post = getActivePostOrThrow(postId);
        validateAuthorOrThrow(post, principalId);

        post.softDelete();
    }

    private Post getActivePostOrThrow(Long postId) {
        return postRepository.findPostByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private void validateAuthorOrThrow(Post post, Long principalId) {
        Long authorId = post.getAuthor().getId();
        if (!authorId.equals(principalId)) {
            throw new CustomException(ErrorCode.POST_FORBIDDEN);
        }
    }
}
