package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Product;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.ArrayList;




@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private List<Order> orderList;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        var order1 = new Order();
        order1.setOrderId(1L);
        order1.setTotalPrice(new BigDecimal(30));
        order1.setStatus("New");

        var order2 = new Order();
        order2.setOrderId(2L);
        order2.setTotalPrice(new BigDecimal(50));
        order2.setStatus("New");

        orderList = List.of(order1, order2);

        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Ivan");

        product = new Product();
        product.setProductId(10L);
        product.setName("Tovar"); 
    }


    @Test
    void getOrders_ShouldGetOrdersList()
    {
        when(orderRepository.findAll()).thenReturn(orderList);
        var listOrders = orderService.getOrders();
        

        assertThat(listOrders.size()).isEqualTo(2);
        assertThat(listOrders.get(1).getOrderId()).isEqualTo(2L);
        assertThat(listOrders.get(1).getStatus()).isEqualTo("New");

        verify(orderRepository).findAll();
    }

    @Test
    void createOrder_ShouldCreateOrder()
    {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        // when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        var newOrder = orderService.createOrder(1L, new ArrayList<>(), "Belgorod");

        assertThat(newOrder.getCustomer().getName()).isEqualTo("Ivan");
        assertThat(newOrder.getShippingAddress()).isEqualTo("Belgorod");

        verify(customerRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }
}
