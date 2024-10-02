package com.farmstory.repository.custom;

import com.farmstory.dto.PageRequestDTO;
import com.farmstory.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    public Page<User> selectUserAllForList(PageRequestDTO requestDTO, Pageable pageable);

    User selectUserById(String uid);
}


