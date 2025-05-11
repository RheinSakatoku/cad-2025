package ru.bsu.cad.lab.app;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Создание заказа</title></head><body>");
        out.println("<h1>Создать заказ</h1>");
        out.println("<form method='post' action='/create-order'>");
        out.println("Название заказа: <input type='text' name='orderName' required><br><br>");
        out.println("<button type='submit'>Сохранить</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String orderName = req.getParameter("orderName");
        System.out.println("Создан заказ: " + orderName);
        resp.sendRedirect("/orders");
    }
}