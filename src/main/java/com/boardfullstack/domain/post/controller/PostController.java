package com.boardfullstack.domain.post.controller;

import com.boardfullstack.domain.like.service.PostLikeService;
import com.boardfullstack.domain.post.dto.request.PostCreateRequest;
import com.boardfullstack.domain.post.dto.request.PostUpdateRequest;
import com.boardfullstack.domain.post.dto.response.PostResponse;
import com.boardfullstack.domain.post.service.PostService;
import com.boardfullstack.global.common.response.ApiResponse;
import com.boardfullstack.global.common.response.PagedResponse;
import com.boardfullstack.global.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(@AuthenticationPrincipal UserPrincipal principal,
                                                @RequestBody @Valid PostCreateRequest request) {
        PostResponse post = postService.createPost(principal.getId(), request);
        return ApiResponse.ok(post);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse post = postService.getPost(postId);
        return ApiResponse.ok(post);
    }

    @GetMapping
    public ApiResponse<PagedResponse<PostResponse>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<PostResponse> posts = postService.getPosts(page, size);

        return ApiResponse.ok(posts);
    }

    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long postId,
                                                @RequestBody @Valid PostUpdateRequest request,
                                                @AuthenticationPrincipal UserPrincipal principal) {
        PostResponse post = postService.updatePost(principal.getId(), postId, request);
        return ApiResponse.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserPrincipal principal) {
        postService.deletePost(principal.getId(), postId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{postId}/like")
    public ApiResponse<Void> likePost(@PathVariable Long postId,
                                      @AuthenticationPrincipal UserPrincipal principal) {
        postLikeService.likePost(principal.getId(), postId);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{postId}/like")
    public ApiResponse<Void> unlikePost(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserPrincipal principal) {
        postLikeService.unlikePost(principal.getId(), postId);
        return ApiResponse.ok(null);
    }
}

