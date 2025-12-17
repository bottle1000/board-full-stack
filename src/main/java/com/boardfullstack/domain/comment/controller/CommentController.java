package com.boardfullstack.domain.comment.controller;

import com.boardfullstack.domain.comment.dto.request.CommentCreateRequest;
import com.boardfullstack.domain.comment.dto.response.CommentResponse;
import com.boardfullstack.domain.comment.service.CommentService;
import com.boardfullstack.global.common.response.ApiResponse;
import com.boardfullstack.global.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ApiResponse<CommentResponse> createComment(@AuthenticationPrincipal UserPrincipal principal,
                                                      @PathVariable Long postId,
                                                      @RequestBody @Valid CommentCreateRequest request) {
        return ApiResponse.ok(commentService.createComment(principal.getId(), postId, request));
    }
}
