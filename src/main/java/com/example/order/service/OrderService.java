package com.example.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.model.order.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

	private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    
    @Transactional
    public void saveOrder(Order order) {
    	// 전체 주문 가격 계산
        order.calcOrderPrice(order.getProduct().getPrice());
        // 주문내역 저장
        orderRepository.saveOrder(order);
        // 상품 재고 수정
        order.getProduct().ajustStock(-order.getCount());
        productRepository.updateProduct(order.getProduct());
    }
    
 // 주문 목록 조회
    public List<Order> findOrdersByMemberId(String member_id){
       return orderRepository.findOrdersByMemberId(member_id);
    }
    
    // 주문 상세정보 조회
    public Order findOrderById(Long order_id) {
       return orderRepository.findOrderById(order_id);
    }
    
    // 주문 취소
    @Transactional
    public void removeOrder(Order order) {

             // 재고 수량 조정
             order.getProduct().ajustStock(order.getCount());
             // 주문 내역 삭제
             orderRepository.removeOrderById(order.getOrder_id());
             // 상품 재고 정보 수정
             productRepository.updateProduct(order.getProduct());

       
    }
}
