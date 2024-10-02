package com.farmstory.controller;

import com.farmstory.dto.PageResponseDTO;
import com.farmstory.dto.order.OrderItemWithOrderWithProductResponseDTO;
import com.farmstory.security.MyUserDetails;
import com.farmstory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/my-page")
@RequiredArgsConstructor
public class MyPageController {
    private final OrderService orderService;

    @GetMapping("/order")
    public String order(@PageableDefault(size = 10,page = 0) Pageable pageable, Model model, @AuthenticationPrincipal MyUserDetails userDetails) {
        if(userDetails == null)
        {
            return "redirect:/";
        }

        PageResponseDTO<OrderItemWithOrderWithProductResponseDTO> orderItemsByUid = orderService.getOrderItemsByUid(userDetails.getUsername(), pageable);
        int orderItemCount = orderService.getOrderItemCountByUid(userDetails.getUsername());
        model.addAttribute("orderItemPage", orderItemsByUid );
        model.addAttribute("orderItemCount", orderItemCount );
        return "/my-page/order";
    }
}
