package com.github.mrchcat.intershop.order.controller;

import com.github.mrchcat.intershop.enums.CartAction;
import com.github.mrchcat.intershop.order.domain.Order;
import com.github.mrchcat.intershop.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void testGetOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void testGetOrder() throws Exception {
        long itemId = 1;
        long userId = 1;
        mockMvc.perform(post("/cart/items/" + itemId)
                        .param("action", CartAction.plus.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart/items"));
        mockMvc.perform(post("/buy"))
                .andExpect(status().is3xxRedirection());

        List<Order> orders = orderRepository.findAllByUserId(userId);
        long orderId = orders.getFirst().getId();
        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("newOrder"));
    }


}