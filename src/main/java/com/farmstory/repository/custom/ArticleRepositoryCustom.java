package com.farmstory.repository.custom;

import com.querydsl.core.Tuple;
import com.farmstory.dto.article.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    public Page<Tuple> selectArticleAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<Tuple> selectArticleForSearch(PageRequestDTO pageRequestDTO, Pageable pageable);
    Page<Tuple> selectArticleForListByCate(PageRequestDTO pageRequestDTO, Pageable pageable, String cate);
}
