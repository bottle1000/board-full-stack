package com.boardfullstack.domain.post.controller;

import com.boardfullstack.domain.post.dto.request.PostCreateRequest;
import com.boardfullstack.domain.post.dto.response.PostResponse;
import com.boardfullstack.domain.post.service.PostService;
import com.boardfullstack.global.common.response.ApiResponse;
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
}

