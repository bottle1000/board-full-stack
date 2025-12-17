package com.boardfullstack.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentCreateRequest {

    @NotBlank(message = "내용은 필수입니다.")
    @Size(min = 2, max = 200, message = "내용은 2자 ~ 200자여야 합니다.")
    private String content;
}
