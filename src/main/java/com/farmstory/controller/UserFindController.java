package com.farmstory.controller;

import com.farmstory.dto.user.UserDTO;
import com.farmstory.entity.User;
import com.farmstory.service.CategoryService;
import com.farmstory.service.EmailService;
import com.farmstory.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Controller
public class UserFindController {

    private final UserService userService;
    private final EmailService emailService;
    private final CategoryService categoryService;

    // user - findId
    @GetMapping("/user/findId")
    public String findUserIdByNameAndEmail() {
        return "/user/findId"; // templates 폴더 안의 findId.html을 렌더링함
    }

    @ResponseBody
    @PostMapping("/user/findId")
    public ResponseEntity<?> findUserIdByNameAndEmail(HttpSession session, @RequestBody Map<String, String> jsonData) {
        String name = jsonData.get("name");
        String email = jsonData.get("email");

        log.info("name : " + name + ", email : " + email);

        // 아이디 찾기 서비스 호출
        userService.receiveCode(name, email);
        log.info("저장한 세션 : {}", session.getAttribute("code"));
        // 응답으로 인증번호 발송 완료 메시지 전송
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 이메일 인증 코드 검증 후 아이디 반환
    @ResponseBody
    @PostMapping("/user/findIdResult")
    public ResponseEntity<?> verifyEmailCode(Model model, HttpSession session, @RequestBody Map<String, String> jsonData) {
        String verificationCode = jsonData.get("code");
        String name = jsonData.get("name");
        String email = jsonData.get("email");

        // 세션에서 인증번호 및 사용자 정보 가져오기
        String sessionCode = (String) session.getAttribute("code");
        String sessionName = (String) session.getAttribute("name");
        String sessionEmail = (String) session.getAttribute("email");

        // 인증번호 및 사용자 정보 검증
        if (sessionCode != null && sessionCode.equals(verificationCode)
                && sessionName.equals(name) && sessionEmail.equals(email)) {

            // 인증 성공: 유저 아이디 반환
            User user = userService.verifyCodeForUser(verificationCode, name, email);

            // 성공 응답
            session.setAttribute("user", user);
            log.info("user: " +user);
//            model.addAttribute("user", user);

            return ResponseEntity.status(HttpStatus.OK).build();

        } else {
            // 실패 응답
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("message", "인증번호가 일치하지 않거나 사용자 정보가 올바르지 않습니다.");
            return ResponseEntity.badRequest().body(resultMap);
        }
    }

    @GetMapping("/user/findIdResult")
    public String findIdResult(Model model, HttpSession session) {
        // user 는 찾아지나 화면에 뜨지 않은 이유는 세션에 있는 user를 html에서 쓸 수 있도록
        // 세션에 저장된 user를 addAttribute(user) 를 해주면 화면에 user가 뜹니다.
        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
        }

        return "/user/findIdResult";
    }


    @GetMapping("/user/findPass")
    public String findUserByUidAndEmail(Model model, HttpSession session) {
        return "/user/findPass";
    }

    @ResponseBody
    @PostMapping("/user/findPass")
    public ResponseEntity<?> findUserByUidAndEmail(HttpSession session, @RequestBody Map<String, String> jsonData) {
        String uid = jsonData.get("uid");
        String email = jsonData.get("email");

        // 비밀번호 찾기 서비스 호출 (아이디와 이메일을 받아서 인증 코드를 전송)
        String verificationCode = userService.resetCode(uid, email);

        // 세션에 인증번호 저장
        session.setAttribute("code", verificationCode);
        session.setAttribute("uid", uid);
        session.setAttribute("email", email);

        // 응답으로 인증번호 발송 완료 메시지 전송
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호 재설정 인증 코드가 이메일로 전송되었습니다.");
    }

    // 비밀번호 변경
//    @ResponseBody
//    @PostMapping("/user/findPassChange")
//    public ResponseEntity<?> resetPassword(HttpSession session, @RequestBody Map<String, String> jsonData) {
////        String verificationCode = jsonData.get("code");
//        String uid = jsonData.get("uid");
////        String email = jsonData.get("email");
//        String newpass = jsonData.get("newpass");
//
//        // 세션에서 인증번호 및 사용자 정보 가져오기
//        String sessionCode = (String) session.getAttribute("code");
//        String sessionUid = (String) session.getAttribute("uid");
////        String sessionEmail = (String) session.getAttribute("email");
//
////        System.out.println("sessionCode = " + sessionCode);
////        System.out.println("verificationCode = " + verificationCode);
//        System.out.println("sessionUid = " + sessionUid);
//        System.out.println("uid = " + uid);
////        System.out.println("sessionEmail = " + sessionEmail);
////        System.out.println("email = " + email);
//
//        // 인증번호 및 사용자 정보 검증
//        if (sessionUid.equals(uid)) {
//            // 인증 성공: 비밀번호 변경 서비스 호출
//            userService.verifyResetCode(uid, newpass);
//
//            // 성공 응답
//            return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 성공적으로 변경되었습니다.");
//
//        } else {
//            // 실패 응답
//            Map<String, Object> resultMap = new HashMap<>();
//            resultMap.put("message", "인증번호가 일치하지 않거나 사용자 정보가 올바르지 않습니다.");
//            return ResponseEntity.badRequest().body(resultMap);
//        }
//    }

    // 이메일로 보낸 코드와 내가 입력한 코드가 맞는지 확인
    @ResponseBody
    @PostMapping("/user/verifypass")
    public ResponseEntity<?> verifypass(HttpSession session, @RequestBody Map<String, String> jsonData) {
        String verificationCode = jsonData.get("code");
        String uid = jsonData.get("uid");
        String email = jsonData.get("email");

        // 세션에서 인증번호 및 사용자 정보 가져오기
        String sessionCode = (String) session.getAttribute("code");
        String sessionUid = (String) session.getAttribute("uid");
        String sessionEmail = (String) session.getAttribute("email");

        // 인증번호 및 사용자 정보 검증
        if (sessionCode != null && sessionCode.equals(verificationCode)
                && sessionUid.equals(uid) && sessionEmail.equals(email)
        ) {
            // 성공 응답
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            // 실패 응답
            return ResponseEntity.badRequest().build();
        }
    }

    // 비밀번호 찾기 결과 start -----------
    @GetMapping("/user/findPassChange")
    public String UserFindPassResult(HttpSession session, Model model) {
        session.setMaxInactiveInterval(60);
        String uid = (String) session.getAttribute("uid");

        if(uid == null) {
            return "redirect:/user/findPass";
        }
        model.addAttribute("uid", uid);
        return "/user/findPassChange";
    }

    @PostMapping("/user/findPassChange")
    public String UserFindPassResult(UserDTO userDTO){
        log.info("Password Change "+userDTO);
        UserDTO resultUser = userService.selectUser(userDTO.getUid());
        log.info("Password Change - resultUser:"+resultUser);
        resultUser.setPass(userDTO.getPass());

        userService.updateUserPass(resultUser);

        return "redirect:/user/login";
    }
    // 비밀번호 찾기 결과 end -----------


}
