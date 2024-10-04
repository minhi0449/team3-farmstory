package com.farmstory.controller;

import com.farmstory.dto.article.CommentDTO;
import com.farmstory.service.article.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value ="/comment/write")
    public ResponseEntity<CommentDTO> write(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {

        String regip = request.getRemoteAddr();
        commentDTO.setRegip(regip);
        log.info(commentDTO);

        CommentDTO dto = commentService.insertComment(commentDTO);

        return ResponseEntity
                .ok()
                .body(dto);
    }


    @DeleteMapping("/comment/delete")
    public boolean deleteComment(@RequestParam("no") int no) {
        log.info("CommentDTO article no : " + no);

        return commentService.deleteCommentById(no);
    }


    // 댓글 수정
    @PutMapping("/comment/modify")
    public ResponseEntity<CommentDTO> update(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        log.info("comment Modify commentDTO : "+commentDTO);
        try {
            CommentDTO updatedComment = commentService.updateComment(commentDTO);
            log.info("comment Modify UpdatedCommentDTO : "+updatedComment);
            return ResponseEntity.ok(updatedComment);

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    }



