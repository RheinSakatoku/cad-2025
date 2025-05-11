package ru.bsu.cad.lab.app;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.bsu.cad.lab.config.JpaUtil;
import ru.bsu.cad.lab.entity.Order;
import ru.bsu.cad.lab.service.OrderQueryService;

import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        OrderQueryService orderQueryService = new OrderQueryService(em);
        List<Order> orders = orderQueryService.getAllOrders();

        out.println("<html><head><title>Список заказов</title></head><body>");
        out.println("<h1>Список заказов</h1>");
        out.println("<ul>");

        for (Order order : orders) {
            out.printf("<li>Заказ №%d — %s (%s)</li>%n",
                    order.getOrderId(),
                    order.getCustomer() != null ? order.getCustomer().getName() : "Без клиента",
                    order.getStatus());
        }

        out.println("</ul>");
        out.println("<form action='/create-order' method='get'>");
        out.println("<button type='submit'>Создать заказ</button>");
        out.println("</form>");
        out.println("</body></html>");

        em.close(); // обязательно закрывай
    }
}
