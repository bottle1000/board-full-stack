package com.boardfullstack.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_INVALID_REQUEST", "잘못된 요청입니다."),

    // 인증/인가
    AUTH_INVALID_CREDENTIAL(HttpStatus.UNAUTHORIZED, "AUTH_INVALID_CREDENTIAL", "이메일 또는 비밀번호가 올바르지 않습니다."),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_UNAUTHORIZED", "인증이 필요합니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_FORBIDDEN", "해당 리소스에 대한 권한이 없습니다."),

    // 사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    USER_BANNED(HttpStatus.FORBIDDEN, "USER_BANNED", "정지된 사용자입니다."),
    USER_ALREADY_ACTIVE(HttpStatus.CONFLICT, "USER_ALREADY_ACTIVE", "이미 활동 중인 사용자입니다."),

    // 게시글
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "게시글을 찾을 수 없습니다."),
    POST_FORBIDDEN(HttpStatus.FORBIDDEN, "POST_FORBIDDEN", "이 게시글을 수정/삭제할 권한이 없습니다."),

    // 댓글
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND", "댓글을 찾을 수 없습니다."),
    COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMENT_FORBIDDEN", "이 댓글을 수정/삭제할 권한이 없습니다."),

    // 좋아요
    POST_LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "POST_LIKE_ALREADY_EXISTS", "이미 좋아요한 게시글입니다."),
    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_LIKE_NOT_FOUND", "좋아요를 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
