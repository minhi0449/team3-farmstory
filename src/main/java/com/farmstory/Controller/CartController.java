package com.farmstory.Controller;


import com.farmstory.dto.CartRequestDTO;
import com.farmstory.dto.CartResponseDTO;
import com.farmstory.entity.User;
import com.farmstory.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 지현님&상훈님 - market
@Log4j2
@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;
//    private final ProductService productService;

    // cart
    @GetMapping("/market/cart")
    public String cart(@RequestParam String uid, Model model) {
        log.info("uid :" + uid);
        List<CartResponseDTO> cartdto = cartService.selectAllCartByUid(uid);
        log.info("cartdto : " + cartdto);
        model.addAttribute("cart", cartdto);

        return "/market/cart";
    }

    @ResponseBody
    @PostMapping("/market/cart")
    public ResponseEntity<CartRequestDTO> cart(@RequestBody CartRequestDTO cartRequestDTO) {
        String uid = cartRequestDTO.getUid();
        log.info("uid :" + uid);
        CartRequestDTO dto = cartService.insertCart(cartRequestDTO);
        dto.setUid(uid);
        log.info("dto : " + dto);

        return ResponseEntity
                .ok()
                .body(dto);
    }
    @GetMapping("/market/order")
    public String order() {
        // TODO: 사용자의 정보 가져와야함(name, hp, point)

        return "/market/order";
    }
    @ResponseBody
    @DeleteMapping("/market/cart/delete")
    public ResponseEntity<?> delete(@RequestBody List<Integer> data) {
        if(data == null || data.isEmpty()){
            return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
        }else {
            cartService.deleteCart(data);
            return ResponseEntity.ok().build();
        }
    }

}
