package com.farmstory.service;

import com.farmstory.dto.PageRequestDTO;
import com.farmstory.dto.user.TermsDTO;
import com.farmstory.dto.user.UserDTO;
import com.farmstory.dto.user.UserPageResponseDTO;
import com.farmstory.entity.Terms;
import com.farmstory.entity.User;
import com.farmstory.repository.TermsRepository;
import com.farmstory.repository.UserRepository;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;
    private final JavaMailSender javaMailSender;
    private final EmailService emailService;
    private final HttpSession session;

    // user
    public void insertUser(UserDTO userDTO){
        log.info("userDTO : " + userDTO);
        String encoded = passwordEncoder.encode(userDTO.getPass());
        userDTO.setPass(encoded);
        userRepository.save(userDTO.toEntity());
    }

    public String loginUser(String uid, String password) {
        String endcodedpassword = passwordEncoder.encode(password);
        Optional<User> opt= userRepository.findByUidAndPass(uid,endcodedpassword);
        if(opt.isPresent()) {
            User user = opt.get();
            return user.getUid();
        }
        return null;
    }

    public User selectUserEntity(String uid) {
        Optional<User> optUser = userRepository.findById(uid);
        if(optUser.isPresent()) {
            User user = optUser.get();
            return user;
        }
        return null;
    }

    public UserDTO selectUser(String uid) {
        Optional<User> optUser = userRepository.findById(uid);
        if(optUser.isPresent()) {
            User user = optUser.get();
            // ModelMapper를 사용하여 User 엔티티를 UserDTO로 변환
            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    //유저 정보 전체 가져오기 (admin 관리자 회원목록)
    public List<UserDTO> selectUsers(){

        List<User> userall = userRepository.findAll();

        List<UserDTO> users = userall
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))  // ModelMapper 사용
                .collect(Collectors.toList());
        return users;
    }

    public List<UserDTO> selectAllUsers() {
        List<User> users = userRepository.findAll();
        return null;
    }

    // 페이지네이션 User selectAll
    public UserPageResponseDTO selectUserAll(PageRequestDTO pageRequestDTO) {

        log.info("selectUserAll >>> pageRequestDTO : " + pageRequestDTO);
        Pageable pageable = pageRequestDTO.getPageable("uid",10);

        Page<User> users = userRepository.selectUserAllForList(pageRequestDTO, pageable);
        log.info(users.toString());

        List<UserDTO> userDTOS = users.getContent().stream().map(user -> {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            return userDTO;
        }).toList();
        System.out.println("userDTOS >>>" + userDTOS);

        int total = (int) users.getTotalElements();

        log.info("total >>>" + total);
        return UserPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .userList(userDTOS)
                .total(total)
                .build();
    }

    // terms
    public TermsDTO selectTerms(){
        List<Terms> termsList = termsRepository.findAll();
        return termsList.get(0).toDTO();
    }

    public int selectCountUser(String type, String value){

        int count = 0;

        if(type.equals("uid")){
            count = userRepository.countByUid(value);
        }else if(type.equals("nick")){
            count = userRepository.countByNick(value);
        }else if(type.equals("hp")){
            count = userRepository.countByHp(value);
        }else if(type.equals("email")){
            count = userRepository.countByEmail(value);
        }
        return count;
    }

    public ResponseEntity updateUser(UserDTO userDTO) {
        if(userDTO.getUid() != null) {
            User entity = modelMapper.map(userDTO, User.class);
            userRepository.save(entity);

            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.ok().body(false);
    }

    public ResponseEntity updateUserPass(UserDTO userDTO) {

        if(userDTO.getName() != null && userDTO.getPass() != null) {
            String encoded = passwordEncoder.encode(userDTO.getPass());
            userDTO.setPass(encoded);

            User entity = userDTO.toEntity();
            userRepository.save(entity);

            return ResponseEntity.ok().body(true);
        }else{
            return ResponseEntity.ok().body(false);
        }
    }

    public void deleteUser(){

    }

    /*
        - build.gradle 파일에 spring-boot-starter-mail 의존성 추가 할것
        - application.yml 파일 spring email 관련 설정
     */
    @Value("${spring.mail.username}")
    private String sender;
    public void sendEmailCode(HttpSession session, String receiver){

        log.info("sender : " + sender);

        // MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        // 인증코드 생성 후 세션 저장
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        session.setAttribute("code", String.valueOf(code));
        log.info("code : " + code);

        String title = "farmstory 인증코드 입니다.";
        String content = "<h1>인증코드는 " + code + "입니다.</h1>";

        try {
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            // 메일 발송
            javaMailSender.send(message);
        }catch(Exception e){
            log.error("sendEmailConde : " + e.getMessage());
        }
    }

    //선택한 유저 정보 삭제
    public void deleteUserById(String uid) {
        //Entity 삭제 (데이터베이스 Delete)
        userRepository.deleteById(uid);
    }


    @Transactional
    public UserDTO leaveUser(String uid) {

        Optional<User> opt = userRepository.findByUid(uid);
        if(opt.isPresent()) {
            User user = opt.get();
            LocalDateTime createAt = LocalDateTime.parse(user.getCreateAt());
            LocalDateTime deleteAt = LocalDateTime.now();

            UserDTO userDTO = new UserDTO();
            userDTO.setUid(uid);
            userDTO.setCreatedAt(String.valueOf(createAt));
            userDTO.setDeletedAt(String.valueOf(deleteAt));

            User entity = modelMapper.map(userDTO, User.class);
            User savedUser = userRepository.save(entity);
            return modelMapper.map(savedUser, UserDTO.class);
        }
        return null;
    }

    //유저 등급 수정
    public boolean updateUserGrade(String userUid, String newGrade) {
        // 유저 ID로 유저 찾기
        Optional<User> optionalUser = userRepository.findById(userUid);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(newGrade);  // 유저 등급 업데이트
            userRepository.save(user);  // 데이터베이스에 저장
            return true;  // 성공 시 true 반환
        } else {
            return false;  // 유저가 없을 경우 false 반환
        }
    }

    /**
     * 로그인 로직
     * @param uid 사용자 아이디
     * @param pass 사용자 비밀번호
     * @return UserDTO (성공 시) 또는 null (실패 시)
     */
    public UserDTO login(String uid, String pass) {
        // uid로 사용자 조회
        Optional<User> optionalUser = userRepository.findByUid(uid);
        // 사용자 존재 여부 확인
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 비밀번호 검증
            if (passwordEncoder.matches(pass, user.getPass())) {
                log.info("로그인 성공 - uid: " + uid);
                return modelMapper.map(user, UserDTO.class); // User 엔티티를 UserDTO로 변환하여 반환
            } else {
                log.warn("비밀번호 불일치 - uid: " + uid);
            }
        } else {
            log.warn("존재하지 않는 사용자 - uid: " + uid);
        }
        return null; // 로그인 실패 시 null 반환
    }


    // 아이디 찾기 서비스 추가
    public void receiveCode(String name, String email) {
        // 1. 이름과 이메일로 DB에서 유저 검색
        Optional<User> user = userRepository.findByNameAndEmail(name, email);

        if (user.isEmpty()) {
            throw new RuntimeException("해당 이름과 이메일로 계정을 찾을 수 없습니다.");
        }
        // 2. 이메일 서비스에서 코드 생성 및 이메일 전송 (세션에 코드 저장)
        String verificationCode = emailService.sendMail(email, "/user/email", session);

        // 3. 인증번호를 세션에 저장
        session.setAttribute("code", verificationCode);  // 세션에 인증번호 저장
        session.setAttribute("name", name);  // 세션에 사용자 이름 저장
        session.setAttribute("email", email);  // 세션에 사용자 이메일 저장
    }

    // 인증번호 검증 및 아이디 반환
    public User verifyCodeForUser(String verificationCode, String name, String email) {

        User user = userRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new RuntimeException("해당 이름과 이메일로 계정을 찾을 수 없습니다."));

        return user;  // 유저의 아이디 반환
    }
    // 인증번호 검증 및 아이디 반환
//    public User verifyCodeForUser(String verificationCode, String name, String email) {
//        // 1. 세션에서 저장된 인증번호 및 사용자 정보 가져오기
//        String sessionCode = (String) session.getAttribute("code");  // 세션에 저장된 인증번호 가져오기
//        String sessionName = (String) session.getAttribute("name");
//        String sessionEmail = (String) session.getAttribute("email");
//
//        // 2. 검증: 세션에 저장된 인증번호와 사용자 정보가 입력된 값과 일치하는지 확인
//        if (sessionCode == null || !sessionCode.equals(verificationCode)
//                || !sessionName.equals(name) || !sessionEmail.equals(email)) {
//            throw new RuntimeException("인증번호가 일치하지 않거나 사용자 정보가 일치하지 않습니다.");
//        }
//
//        // 3. 검증 성공 후, 유저의 아이디 반환
//        User user = userRepository.findByNameAndEmail(name, email)
//                .orElseThrow(() -> new RuntimeException("해당 이름과 이메일로 계정을 찾을 수 없습니다."));
//
//        return user;  // 유저의 아이디 반환
//    }

    // 아이디로 회원 엔티티 불러오기
    public UserDTO selectUsers(String uid) {
        Optional<User> optUser = userRepository.findById(uid);
        if (optUser.isPresent()) {
            User user = optUser.get();
            log.info("user >>>" + user);

            UserDTO dto  = modelMapper.map(user, UserDTO.class);
            log.info("UserDTO >>>" + dto.toString());

            return modelMapper.map(user, UserDTO.class);
        }
        return null;
    }

    public UserDTO modifyUser(UserDTO userDTO) {
        Boolean result = userRepository.existsById(userDTO.getUid());

        if (result) {
            User user = modelMapper.map(userDTO, User.class);
            //user.setRegDate(LocalDateTime.parse(userDTO.getRegDate()));
            log.info("user >>>>>" + user);
            User savedUser = userRepository.save(user);
            UserDTO resultUser = modelMapper.map(savedUser, UserDTO.class);
            return resultUser;
        }
        return null;
    }

    // 비밀번호 찾기 서비스 추가
    public String resetCode (String uid, String email){

        // 1. 이름과 이메일로 DB에서 유저 검색
        Optional<User> user = userRepository.findByUidAndEmail(uid, email);
        if (user.isEmpty()) {
            throw new RuntimeException("해당 이름과 이메일로 계정을 찾을 수 없습니다.");
        }
        // 2. 이메일 서비스에서 코드 생성 및 이메일 전송 (세션에 코드 저장)
        String verificationCode = emailService.sendMail(email, "/user/email", session);
        // 3. 인증번호를 세션에 저장
        session.setAttribute("code", verificationCode);  // 세션에 인증번호 저장
        session.setAttribute("uid", uid);  // 세션에 사용자 아이디 저장
        session.setAttribute("email", email);  // 세션에 사용자 이메일 저장

        return verificationCode;
    }

    // 인증번호 검증 및 비밀번호 변경
    public User verifyResetCode (String uid, String newpass){
        // 1. 사용자가 존재하는지 확인 (UID와 Email로 조회)
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("해당 사용자 정보를 찾을 수 없습니다."));

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newpass);

        // 4. 유저의 비밀번호를 암호화된 비밀번호로 업데이트
        user.setPass(encodedPassword);
        userRepository.save(user);  // 비밀번호 저장

        // 5. 비밀번호 변경 완료 후, 유저 정보 반환 (필요한 경우)
        return user;
    }



}
