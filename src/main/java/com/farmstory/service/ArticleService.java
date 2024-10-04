package com.farmstory.service;

import com.farmstory.entity.User;
import com.farmstory.repository.UserRepository;
import com.querydsl.core.Tuple;
import com.farmstory.dto.article.ArticleDTO;
import com.farmstory.dto.article.PageRequestDTO;
import com.farmstory.dto.article.PageResponseDTO;
import com.farmstory.entity.Article;
import com.farmstory.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;

    public int insertArticle(ArticleDTO articleDTO) {

        // ModelMapper를 이용해서 DTO를 Entity로 변환
        Article article = modelMapper.map(articleDTO, Article.class);
        log.info(article);

        User user = userRepository.findByUid(articleDTO.getWriter());
        article.addUser(user);
        // 저장
        Article savedArticle = articleRepository.save(article);

        // 저장된 글번호 리턴
        return savedArticle.getAno();
    }

    public ArticleDTO selectArticle(int no){

        Optional<Article> optArticle = articleRepository.findById(no);

        if(optArticle.isPresent()){
            Article article = optArticle.get();
            log.info(article);

            ArticleDTO dto = modelMapper.map(article, ArticleDTO.class);
            dto.setWriter(article.getUser().getUid());
            dto.setNick(article.getUser().getNick());
            return dto;
        }

        return null;
    }

    public PageResponseDTO selectArticleAll(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("ano");

        Page<Tuple> pageArticle = null;

        if(pageRequestDTO.getKeyword() == null) {
            // 일반 글목록 조회
            log.info(pageRequestDTO);
            pageArticle = articleRepository.selectArticleAllForList(pageRequestDTO, pageable);
        }else {
            // 검색 글목록 조회
            pageArticle = articleRepository.selectArticleForSearch(pageRequestDTO, pageable);
        }

        // 엔티티 리스트를 DTO 리스트 변환
        List<ArticleDTO> articleList = pageArticle.getContent().stream().map(tuple -> {

            Article article = tuple.get(0, Article.class);
            String nick = tuple.get(1, String.class);
            article.addNick(nick);

            return modelMapper.map(article, ArticleDTO.class);

        }).toList();

        int total = (int) pageArticle.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(articleList)
                .total(total)
                .build();
    }

    public void updateArticle(ArticleDTO articleDTO) {

    }

    public void deleteArticle(int no){
        articleRepository.deleteById(no);
    }
}
