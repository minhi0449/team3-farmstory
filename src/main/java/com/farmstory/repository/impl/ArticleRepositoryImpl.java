package com.farmstory.repository.impl;

import com.farmstory.dto.article.PageRequestDTO;
import com.farmstory.entity.QArticle;
import com.farmstory.entity.QUser;
import com.farmstory.repository.custom.ArticleRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QArticle qArticle = QArticle.article;
    private QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectArticleAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        List<Tuple> content  = queryFactory
                                        .select(qArticle, qUser.nick)
                                        .from(qArticle)
                                        .join(qUser)
                                        .on(qArticle.user.uid.eq(qUser.uid))
                                        .offset(pageable.getOffset())
                                        .limit(pageable.getPageSize())
                                        .orderBy(qArticle.ano.desc())
                                        .fetch();

        log.info("content : " + content);

        long total = queryFactory
                        .select(qArticle.count())
                        .from(qArticle)
                        .fetchOne();

        log.info("total : " + total);

        // 페이징 처리를 위해 page 객체 리턴
        return new PageImpl<Tuple>(content, pageable, total);
    }

    @Override
    public Page<Tuple> selectArticleForSearch(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        // 검색 선택 조건에 따라 where 조건 표현식 생성
        BooleanExpression expression = null;

        if(type.equals("title")){
            expression = qArticle.title.contains(keyword);
            log.info(expression);

        }else if(type.equals("content")){
            expression = qArticle.content.contains(keyword);
            log.info(expression);

        }else if(type.equals("title_content")){

            BooleanExpression titleExpression = qArticle.title.contains(keyword);
            BooleanExpression contentExpression = qArticle.content.contains(keyword);

            expression = titleExpression.or(contentExpression);
            log.info(expression);

        }else if(type.equals("writer")){
            expression = qUser.nick.contains(keyword);
            log.info(expression);
        }

        List<Tuple> content  = queryFactory
                                .select(qArticle, qUser.nick)
                                .from(qArticle)
                                .join(qUser)
                                .on(qArticle.user.uid.eq(qUser.uid))
                                .where(expression)
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .orderBy(qArticle.ano.desc())
                                .fetch();

        log.info("content : " + content);

        long total = queryFactory
                        .select(qArticle.count())
                        .from(qArticle)
                        .where(expression)
                        .join(qUser)
                        .on(qArticle.user.uid.eq(qUser.uid))
                        .fetchOne();

        // 페이징 처리를 위해 page 객체 리턴
        return new PageImpl<Tuple>(content, pageable, total);
    }
}
