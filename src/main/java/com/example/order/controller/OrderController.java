package com.example.order.controller;

import com.example.order.model.order.OrderProductForm;
import com.example.order.model.product.Product;
import com.example.order.model.order.Order;
import com.example.order.config.PrincipalDetails;
import com.example.order.model.member.Member;
import com.example.order.repository.ProductRepository;
import com.example.order.service.OrderService;
import com.example.order.service.ProductService;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("order")
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    @PostMapping("orderProduct")
    public String order(@AuthenticationPrincipal PrincipalDetails userInfo,
                        @Validated @ModelAttribute OrderProductForm orderProductForm,
                        BindingResult bindingResult) {
        log.info("order: {}", orderProductForm);
        Product product = productService.findProductById(orderProductForm.getProduct_id());

        // 상품 정보가 없으면 주문 처리를 하지 않는다.
        if (product == null) {
            return "redirect:/product/products";
        }
        orderProductForm.setProduct_id(product.getProduct_id());
        orderProductForm.setName(product.getName());
        orderProductForm.setPrice(product.getPrice());
        orderProductForm.setStock(product.getStock());

        if (bindingResult.hasErrors()) {
            return "/product/productInfo";
        }

        // 재고량 보다 주문수량이 더 많으면 주문처리를 하지 않는다.
        if (product.getStock() < orderProductForm.getCount()) {
            bindingResult.rejectValue("count", "countError", "재고 수량이 부족합니다.");
            return "/product/productInfo";
        }

        // 주문 객체를 생성
        Order order = OrderProductForm.toOrder(orderProductForm, userInfo.getMember(), product);
        orderService.saveOrder(order);

        return "redirect:/product/products";
    }

    @GetMapping("myOrders")
    public String myOrders(@AuthenticationPrincipal PrincipalDetails userInfo,
                           Model model) {
        List<Order> orders = orderService.findOrdersByMemberId(userInfo.getMember().getMember_id());
        log.info("orders: {}", orders);
        model.addAttribute("orders", orders);
        return "order/myOrders";
    }

    @GetMapping("orderInfo")
    public String orderInfo(@AuthenticationPrincipal PrincipalDetails userInfo,
                            @RequestParam Long order_id,
                            Model model) {

        Order order = orderService.findOrderById(order_id);
        log.info("product: {}", order.getProduct());
        model.addAttribute("order", order);
        return "order/orderInfo";
    }
    
    @GetMapping("withdrawOrder")
    public String withdrawOrder(@AuthenticationPrincipal PrincipalDetails userInfo,
                                @RequestParam Long order_id) {
       
        Order order = orderService.findOrderById(order_id);
        if (order.getMember().getMember_id().equals(userInfo.getMember().getMember_id())) {
        orderService.removeOrder(order);
        }

        return "redirect:/order/myOrders";
    }
}
