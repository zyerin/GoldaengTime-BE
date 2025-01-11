package com.goldang.goldangtime.service;

import com.goldang.goldangtime.dto.CommentRequestDto;
import com.goldang.goldangtime.dto.CommentResponseDto;
import com.goldang.goldangtime.entity.Comment;
import com.goldang.goldangtime.entity.FoundPost;
import com.goldang.goldangtime.entity.LostPost;
import com.goldang.goldangtime.entity.Users;
import com.goldang.goldangtime.repository.CommentRepository;
import com.goldang.goldangtime.repository.FoundPostRepository;
import com.goldang.goldangtime.repository.LostPostRepository;
import com.goldang.goldangtime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final LostPostRepository lostPostRepository;
    private final FoundPostRepository foundPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto addComment(String email, CommentRequestDto requestDto) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = Comment.builder()
                .user(user)
                .text(requestDto.getText())
                .secret(requestDto.getSecret())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        Long postId = null;
        String postType = null;

        // 부모 댓글이 있는 경우 (답글)
        if (requestDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
            comment.setParentComment(parentComment);

            // 부모 댓글에서 postId와 postType 가져오기
            if (parentComment.getLostPost() != null) {
                postId = parentComment.getLostPost().getId();
                postType = "Lost";
                comment.setLostPost(parentComment.getLostPost());
            } else if (parentComment.getFoundPost() != null) {
                postId = parentComment.getFoundPost().getId();
                postType = "Found";
                comment.setFoundPost(parentComment.getFoundPost());
            } else {
                throw new IllegalArgumentException("Parent comment is not associated with any post");
            }
        } else {
            // LostPost인지 FoundPost인지 확인
            if ("lost".equalsIgnoreCase(requestDto.getPostType())) {
                LostPost lostPost = lostPostRepository.findById(requestDto.getPostId())
                        .orElseThrow(() -> new IllegalArgumentException("Lost post not found"));
                comment.setLostPost(lostPost);
                postId = lostPost.getId();
                postType = "Lost";
            } else if ("found".equalsIgnoreCase(requestDto.getPostType())) {
                FoundPost foundPost = foundPostRepository.findById(requestDto.getPostId())
                        .orElseThrow(() -> new IllegalArgumentException("Found post not found"));
                comment.setFoundPost(foundPost);
                postId = foundPost.getId();
                postType = "Found";
            } else {
                throw new IllegalArgumentException("Invalid post type");
            }
        }

        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .id(savedComment.getId())
                .username(savedComment.getUser().getNickname())
                .text(savedComment.getText())
                .secret(savedComment.getSecret())
                .createdDate(savedComment.getCreatedDate())
                .modifiedDate(savedComment.getModifiedDate())
                .postId(postId)
                .postType(postType)
                .build();
    }



    @Transactional
    public CommentResponseDto updateComment(Long commentId, String email, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("Unauthorized user");
        }

        comment.setText(requestDto.getText());
        comment.setSecret(requestDto.getSecret());

        Comment updatedComment = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .id(updatedComment.getId())
                .username(updatedComment.getUser().getNickname())
                .text(updatedComment.getText())
                .secret(updatedComment.getSecret())
                .modifiedDate(updatedComment.getModifiedDate())
                .build();
    }

    @Transactional
    public String deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        log.info("Delete Comment: {}", comment);

        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("Unauthorized user");
        }

        commentRepository.delete(comment);

        return "Comment deleted successfully!";
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long postId, String postType) {
        List<Comment> comments;

        if ("lost".equalsIgnoreCase(postType)) {
            comments = commentRepository.findAllByLostPostIdAndParentCommentIsNull(postId);
        } else if ("found".equalsIgnoreCase(postType)) {
            comments = commentRepository.findAllByFoundPostIdAndParentCommentIsNull(postId);
        } else {
            throw new IllegalArgumentException("Invalid post type");
        }

        return comments.stream()
                .map(comment -> mapToResponseDto(comment, postId, postType))
                .collect(Collectors.toList());
    }

    private CommentResponseDto mapToResponseDto(Comment comment, Long postId, String postType) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .username(comment.getUser().getNickname())
                .text(comment.getText())
                .secret(comment.getSecret())
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate())
                .replies(comment.getReplies().stream()
                        .map(reply -> mapToResponseDto(reply, postId, postType))
                        .collect(Collectors.toList()))
                .postId(postId)
                .postType(postType)
                .build();
    }


}
