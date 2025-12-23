package com.boardfullstack.domain.like.entity;

import com.boardfullstack.domain.post.entity.Post;
import com.boardfullstack.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "post_like",
        uniqueConstraints = @UniqueConstraint(name = "uq_post_like_post_user", columnNames = {"post_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static PostLike create(Post post, User user) {
        PostLike postLike = new PostLike();
        postLike.post = post;
        postLike.user = user;
        postLike.createdAt = LocalDateTime.now();
        return postLike;
    }
}
