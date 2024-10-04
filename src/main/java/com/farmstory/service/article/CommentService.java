package com.farmstory.service.article;


import com.farmstory.dto.article.CommentDTO;
import com.farmstory.entity.Comment;
import com.farmstory.entity.User;
import com.farmstory.repository.UserRepository;
import com.farmstory.repository.article.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    public CommentDTO insertComment(CommentDTO commentDTO) {

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        User user = userRepository.findByUid(commentDTO.getWriter()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);

        return modelMapper.map(savedComment, CommentDTO.class);
    }

    public List<CommentDTO> selectCommentAll() {
        return null;
    }

    public CommentDTO selectComment(int no) {
        return null;
    }

    public CommentDTO updateComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getNo())
                .orElseThrow(() -> new IllegalArgumentException("NO Comment found with id:" + commentDTO.getNo()));

        // 댓글 내용 수정
        comment.setContent(commentDTO.getContent());

        Comment updatedComment = commentRepository.save(comment);
        return modelMapper.map(updatedComment, CommentDTO.class);
    }

    public void deleteComment(int no) {

    }
    public boolean deleteCommentById(int no) {
        try {
            commentRepository.deleteById(no);
            return true;
        } catch (Exception e) {
            log.error("Error while deleting comment: " + e.getMessage());
            return false;
        }
    }
}
