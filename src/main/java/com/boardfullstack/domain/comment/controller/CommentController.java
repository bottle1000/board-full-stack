package com.boardfullstack.domain.comment.controller;

import com.boardfullstack.domain.comment.dto.request.CommentCreateRequest;
import com.boardfullstack.domain.comment.dto.response.CommentResponse;
import com.boardfullstack.domain.comment.service.CommentService;
import com.boardfullstack.global.common.response.ApiResponse;
import com.boardfullstack.global.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ApiResponse<CommentResponse> createComment(@AuthenticationPrincipal UserPrincipal principal,
                                                      @PathVariable Long postId,
                                                      @RequestBody @Valid CommentCreateRequest request) {
        return ApiResponse.ok(commentService.createComment(principal.getId(), postId, request));
    }

    @GetMapping("/comments")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ApiResponse.ok(commentService.getComments(postId));
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId,
                                           @PathVariable Long postId,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        commentService.deleteComment(principal.getId(), postId, commentId);
        return ApiResponse.ok(null);
    }
}
