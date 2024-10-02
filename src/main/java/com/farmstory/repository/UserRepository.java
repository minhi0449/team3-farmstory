package com.farmstory.repository;

import com.farmstory.dto.PageRequestDTO;
import com.farmstory.entity.User;
import com.farmstory.repository.custom.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> ,UserRepositoryCustom {
    public Optional<User> findByUidAndPass(String uid, String pass);

    public int countByUid(String uid);
    public int countByNick(String nick);
    public int countByEmail(String email);
    public int countByHp(String hp);

    User findByUid(String uid);
    User findByEmail(String email);


    // 이름과 이메일로 유저 검색
    public Optional<User> findByNameAndEmail(String name, String email);
    public Optional<User> findByUidAndEmail(String uid, String email);

    Page<User> selectUserAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);
}
