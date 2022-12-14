package com.manuel.blogrestapi.controllers;

import com.manuel.blogrestapi.services.CommentService;
import com.manuel.blogrestapi.dto.CommentDto;
import com.manuel.blogrestapi.security.annotation.ForUser;
import com.manuel.blogrestapi.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "CRUD REST API for comments resources")
@RestController()
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "Create new comment REST API")
    @ForUser
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long id,
                                                    @Valid @RequestBody CommentDto commentDto,
                                                    @RequestHeader(name = AppConstants.HEADER_NAME) String token) {
        String tokenWithoutPrefix = token.replace(AppConstants.HEADER_VALUE, "");
        return new ResponseEntity<>(commentService.createComment(tokenWithoutPrefix, id, commentDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "GET all comments by post id REST API")
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @ApiOperation(value = "GET comment by post and comment id REST API")
    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") long postId,
                                                     @PathVariable(value = "commentId") long commentId) {
        return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
    }

    @ApiOperation(value = "Update comment by post and comment id REST API")
    @ForUser
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentById(@PathVariable(value = "postId") long postId,
                                                        @PathVariable(value = "commentId") long commentId,
                                                        @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.updateCommentById(commentDto, postId, commentId), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete comment by post and comment id REST API")
    @ForUser
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable(value = "postId") long postId,
                                                    @PathVariable(value = "commentId") long commentId) {
        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully.", HttpStatus.OK);
    }

    @ForUser
    @PatchMapping("/{id}/like")
    public ResponseEntity<String> likeComment(@PathVariable(name = "id") long id,
                                              @RequestHeader(name = AppConstants.HEADER_NAME) String token) {
        commentService.giveLikeByCommentId(token, id);
        return new ResponseEntity<>("You liked this comment.", HttpStatus.OK);
    }
}