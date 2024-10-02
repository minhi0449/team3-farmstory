package com.farmstory.controller;

import com.farmstory.dto.user.TermsDTO;
import com.farmstory.dto.user.UserDTO;
import com.farmstory.entity.User;
import com.farmstory.service.CategoryService;
import com.farmstory.service.EmailService;
import com.farmstory.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final CategoryService categoryService;

    // user
    @GetMapping("/user/login")
    public String login() {
        return "/user/login";
    }

    // 로그인
    @PostMapping("/user/login")
    public String login(HttpServletRequest req, @RequestParam("uid") String uid, @RequestParam("pass") String pass, Model model) {
        log.info("Login attempt: uid = " + uid);

        // UserService를 통해 로그인 처리
        UserDTO user = userService.login(uid, pass);

        if (user != null) {
            // 로그인 성공: 세션에 사용자 정보 저장
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            log.info("Login successful: " + user.getUid());
            return "redirect:/"; // 로그인 후 리다이렉트할 페이지
        } else {
            // 로그인 실패 시 에러 메시지와 함께 로그인 페이지로 이동
            model.addAttribute("loginError", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "/user/login?loginError=103";
        }
    }
    
    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션 무효화
        session.invalidate();

        // 성공적으로 로그아웃되었다는 메시지를 리다이렉트 속성에 추가
        redirectAttributes.addFlashAttribute("success", 101);

        // 메인 페이지 또는 로그인 페이지로 리다이렉트
        return "redirect:/"; // 리다이렉트할 페이지로 이동
    }


    // Terms 이용약관
    @GetMapping("/user/terms")
    public String terms(Model model) {
        TermsDTO termsDTO = userService.selectTerms();
        log.info(termsDTO);
        model.addAttribute("terms", termsDTO);
        return "/user/terms";
    }
    
    // 회원가입
    @GetMapping("/user/register")
    public String register() {
        return "/user/register";
    }

    @PostMapping("/user/register")
    public String register(HttpServletRequest req, UserDTO userDTO) {
        log.info(userDTO.toString());

//        if(userDTO == null){
//            return "/user/register?success=202";
//        }

        String regip= req.getRemoteAddr();
        userDTO.setRegip(regip);
        userService.insertUser(userDTO);
        return "redirect:/user/login?success=200";
    }


    @ResponseBody
    @GetMapping("/user/{type}/{value}")
    public ResponseEntity<?> checkUser(HttpSession session,
                                       @PathVariable("type")  String type,
                                       @PathVariable("value") String value){

        log.info("type : " + type + ", value : " + value);

        int count = userService.selectCountUser(type, value);
        log.info("count : " + count);

        // 중복 없으면 이메일 인증코드 발송
        if(count == 0 && type.equals("email")){
            log.info("email : " + value);
            userService.sendEmailCode(session, value);
        }

        // Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", count);

        return ResponseEntity.ok().body(resultMap);
    }

    // 이메일 인증 코드 검사
    @ResponseBody
    @PostMapping("/email")
    public ResponseEntity<?> checkEmail(HttpSession session, @RequestBody Map<String, String> jsonData){

        log.info("checkEmail code : " + jsonData);

        String receiveCode = jsonData.get("code");
        log.info("checkEmail receiveCode : " + receiveCode);

        String sessionCode = (String) session.getAttribute("code");

        if(sessionCode.equals(receiveCode)){
            // Json 생성
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", true);

            return ResponseEntity.ok().body(resultMap);
        }else{
            // Json 생성
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", false);

            return ResponseEntity.ok().body(resultMap);
        }
    }


    @GetMapping("/user/findId")
    public String findUserIdByNameAndEmail() {
        return "/user/findId";  // templates 폴더 안의 findId.html을 렌더링함
    }

    // 아이디 찾기: 이름과 이메일을 입력받아 인증번호를 *이메일로 전송 ("/findid") 괄호 안 경로
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
    public ResponseEntity<?> verifyEmailCode(HttpSession session, @RequestBody Map<String, String> jsonData) {
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

            return ResponseEntity.status(HttpStatus.OK).build();

        } else {
            // 실패 응답
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("message", "인증번호가 일치하지 않거나 사용자 정보가 올바르지 않습니다.");
            return ResponseEntity.badRequest().body(resultMap);
        }
    }

    @GetMapping("/user/findUserByUidAndEmail")
    public String findUserByUidAndEmail() {
        return "/user/findPass";  // templates 폴더 안의 findId.html을 렌더링함
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

    @ResponseBody
    @PostMapping("/user/changepass")
    public ResponseEntity<?> resetPassword(HttpSession session, @RequestBody Map<String, String> jsonData) {
//        String verificationCode = jsonData.get("code");
        String uid = jsonData.get("uid");
//        String email = jsonData.get("email");
        String newpass = jsonData.get("newpass");

        // 세션에서 인증번호 및 사용자 정보 가져오기
        String sessionCode = (String) session.getAttribute("code");
        String sessionUid = (String) session.getAttribute("uid");
//        String sessionEmail = (String) session.getAttribute("email");

//        System.out.println("sessionCode = " + sessionCode);
//        System.out.println("verificationCode = " + verificationCode);
        System.out.println("sessionUid = " + sessionUid);
        System.out.println("uid = " + uid);
//        System.out.println("sessionEmail = " + sessionEmail);
//        System.out.println("email = " + email);

        // 인증번호 및 사용자 정보 검증
        if (sessionUid.equals(uid)) {
            // 인증 성공: 비밀번호 변경 서비스 호출
            userService.verifyResetCode(uid, newpass);

            // 성공 응답
            return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 성공적으로 변경되었습니다.");

        } else {
            // 실패 응답
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("message", "인증번호가 일치하지 않거나 사용자 정보가 올바르지 않습니다.");
            return ResponseEntity.badRequest().body(resultMap);
        }
    }

    // 아이디 찾기 결과
    @GetMapping("/user/findIdResult")
    public String UserFindIdResult(){
        return "user/findIdResult";
    }

    // 비밀번호 찾기 
    @GetMapping("/user/findPass")
    public String UserFindPass(){
        return "user/findPass";
    }

    // 비밀번호 찾기 결과 start -----------
    @GetMapping("/user/findPassResult")
    public String UserFindPassResult(HttpSession session, Model model) {
        session.setMaxInactiveInterval(60);
        String uid = (String) session.getAttribute("uid");
        if(uid == null) {
            return "redirect:/user/findPass";
        }
        model.addAttribute("uid", uid);
        return "user/findPassResult";
    }

    @PostMapping("/user/findPassResult")
    public String UserFindPassResult(UserDTO userDTO){

        String uid = userDTO.getUid();

        UserDTO resultUser = userService.selectUserById(uid);

        resultUser.setPass(userDTO.getPass());
        userService.updateUserPass(resultUser);

        return "redirect:/user/login";
    }
    // 비밀번호 찾기 결과 end -----------

}
