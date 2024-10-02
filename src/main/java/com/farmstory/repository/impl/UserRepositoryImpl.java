package com.farmstory.repository.impl;

import com.farmstory.dto.PageRequestDTO;
import com.farmstory.entity.QUser;
import com.farmstory.entity.User;
import com.farmstory.repository.custom.UserRepositoryCustom;
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
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private QUser qUser = QUser.user;

    @Override
    public Page<User> selectUserAllForList(PageRequestDTO requestDTO, Pageable pageable) {

        log.debug("*******START*******");
        List<User> users = queryFactory.select(qUser)
                .from(qUser)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qUser.createAt.asc())
                .fetch();
        log.info("users :" +users);

        long total = queryFactory.select(qUser.count())
                .from(qUser)
                .fetchOne();

        return new PageImpl<User>(users, pageable, total);
    }
}
