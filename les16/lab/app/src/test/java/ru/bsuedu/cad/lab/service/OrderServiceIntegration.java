package ru.bsuedu.cad.lab.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.transaction.Transactional;
import ru.bsuedu.cad.lab.conf.TestConfiguration;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.OrderRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Transactional
//@TestInstance(Lifecycle.PER_CLASS)
public class OrderServiceIntegration {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        var customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Ivan");

        customerRepository.save(customer);


    }

    


    @Test
    void createOrder_ShouldCreateOrder()
    {
        var newOrder = orderService.createOrder(1L, new ArrayList<>(), "Belgorod");

        Iterable<Order> orders = orderRepository.findAll();

        List<Order> orderList = new ArrayList<>();
        for (Order o : orders) {
            orderList.add(o);
        }

        assertEquals(1, orderList.size());
        assertEquals("Belgorod", orderList.get(0).getShippingAddress());
        assertEquals("Ivan1", orderList.get(0).getCustomer().getName());

    }

    @Test
    void createOrder_ShouldntCreateOrder()
    {
        try {
            var newOrder = orderService.createOrder(2L, new ArrayList<>(), "Belgorod");
        } 
        
        catch (Exception e) {
            assertEquals("java.util.NoSuchElementException", e.getClass().getName());
        }
            
        

    }
}
