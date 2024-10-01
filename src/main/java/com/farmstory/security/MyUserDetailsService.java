package com.farmstory.security;

import com.farmstory.entity.User;
import com.farmstory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Log4j2
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {

        // 사용자가 입력한 아이디로 사용자 조회, 비밀번호에 대한 검증은 이전 컴포넌트인 AuthenticationProvider에서 수행
        Optional<User> optUser = userRepository.findById(uid);
        log.info(optUser);
        if(optUser.isPresent()) {
            // 시큐리티 사용자 인증객체 생성 후 반환
            MyUserDetails myUserDetails = MyUserDetails.builder()
                    .user(optUser.get())
                    .build();

            log.info(myUserDetails);
            return myUserDetails;
        }
        log.info("here...");
        // 사용자가 입력한 아이디가 없을 경우
        return null;
    }

}
