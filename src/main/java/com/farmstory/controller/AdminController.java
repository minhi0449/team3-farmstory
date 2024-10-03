package com.farmstory.controller;

import com.farmstory.dto.PageResponseDTO;
import com.farmstory.dto.order.OrderItemResponseDTO;
import com.farmstory.dto.order.OrderItemWithOrderWithProductResponseDTO;
import com.farmstory.dto.user.UserDTO;
import com.farmstory.security.MyUserDetails;
import com.farmstory.service.OrderService;
import com.farmstory.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class AdminController {
    private final OrderService orderService;

    private final UserService userService;

    @GetMapping(value={"/admin/","/admin/index"})
    public String index() {
        return "/admin/index";
    }


    @GetMapping("/admin/order/list")
    public String orderList(@AuthenticationPrincipal MyUserDetails userDetails, Model model, Pageable pageable) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String uid = userDetails.getUsername();
        PageResponseDTO<OrderItemWithOrderWithProductResponseDTO> orderItemsPage = orderService.getOrderItems(pageable);
        model.addAttribute("orderItemsPage", orderItemsPage);
        return "/admin/order/list";
    }

    // admin 관리자 회원목록
    @GetMapping("/admin/user/list")
    public String userList(Model model) {
        List<UserDTO> userDTO = userService.selectUsers();
        log.info(userDTO);
        model.addAttribute("users", userDTO);
        return "/admin/user/list";

    }







}
