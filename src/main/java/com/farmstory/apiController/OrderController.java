package com.farmstory.apiController;

import com.farmstory.dto.order.OrderCreateDTO;
import com.farmstory.dto.order.OrderIdResponseDTO;
import com.farmstory.dto.order.OrderWithTotalResponseDTO;
import com.farmstory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderIdResponseDTO> createOrder(@RequestBody OrderCreateDTO orderDTO) {
        log.debug("createOrder start");
        log.debug("orderDTO = {}", orderDTO.getOrderItems());
        int orderId = orderService.createOrder(orderDTO);
        return ResponseEntity.status(201).body(new OrderIdResponseDTO(orderId)) ;
    }

    @GetMapping("/{orderNo}")
    public ResponseEntity<OrderWithTotalResponseDTO> getOrder(@PathVariable("orderNo") int orderNo) {
        OrderWithTotalResponseDTO order = orderService.getOrderByIdWithPrice(orderNo);
        return ResponseEntity.status(200).body(order) ;
    }
}
