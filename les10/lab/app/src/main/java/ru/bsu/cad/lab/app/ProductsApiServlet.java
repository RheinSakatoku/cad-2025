package ru.bsu.cad.lab.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.bsu.cad.lab.config.JpaUtil;
import ru.bsu.cad.lab.entity.Product;
import ru.bsu.cad.lab.service.ProductService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/products")
public class ProductsApiServlet extends HttpServlet {

    private EntityManager entityManager;
    private ProductService productService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        entityManager = JpaUtil.getEntityManagerFactory().createEntityManager();
        productService = new ProductService(entityManager);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // поддержка LocalDateTime
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Product> products = productService.getAllProducts();
            String json = objectMapper.writeValueAsString(products);

            resp.setContentType("application/json; charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Ошибка при получении продуктов", e);
        }
    }

    @Override
    public void destroy() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
