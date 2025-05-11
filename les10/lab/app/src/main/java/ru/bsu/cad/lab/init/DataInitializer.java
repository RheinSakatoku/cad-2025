package ru.bsu.cad.lab.init;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.bsu.cad.lab.config.JpaUtil;
import ru.bsu.cad.lab.entity.Category;
import ru.bsu.cad.lab.entity.Customer;
import ru.bsu.cad.lab.entity.Product;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@WebListener
public class DataInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            loadCategories(em);
            loadCustomers(em);
            loadProducts(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void loadCategories(EntityManager em) throws Exception {
        try (BufferedReader reader = readCsv("category.csv")) {
            reader.lines().skip(1).forEach(line -> {
                String[] parts = line.split(",");
                Category category = new Category();
                category.setName(parts[1]);
                category.setDescription(parts[2]);
                em.persist(category);
            });
        }
    }

    private void loadCustomers(EntityManager em) throws Exception {
        try (BufferedReader reader = readCsv("customer.csv")) {
            reader.lines().skip(1).forEach(line -> {
                String[] parts = line.split(",");
                Customer customer = new Customer();
                customer.setName(parts[1]);
                customer.setEmail(parts[2]);
                customer.setPhone(parts[3]);
                customer.setAddress(parts[4]);
                em.persist(customer);
            });
        }
    }

    private void loadProducts(EntityManager em) throws Exception {
        try (BufferedReader reader = readCsv("product.csv")) {
            reader.lines().skip(1).forEach(line -> {
                String[] parts = line.split(",");
                Product product = new Product();
                product.setName(parts[1]);
                product.setDescription(parts[2]);
                product.setCategory(em.find(Category.class, Long.parseLong(parts[3])));
                product.setPrice(new BigDecimal(parts[4]));
                product.setStockQuantity(Integer.parseInt(parts[5]));
                product.setImageUrl(parts[6]);
                product.setCreatedAt(LocalDate.parse(parts[7]).atStartOfDay());
                product.setUpdatedAt(LocalDate.parse(parts[8]).atStartOfDay());

                em.persist(product);
            });
        }
    }

    private BufferedReader readCsv(String fileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }
}
