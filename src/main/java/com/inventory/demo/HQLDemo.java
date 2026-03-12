package com.inventory.demo;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;
import com.inventory.loader.ProductDataLoader;

public class HQLDemo {

    public static void main(String[] args) {

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        try {

            // Run once only (then comment it)
            ProductDataLoader.loadSampleProducts(session);

            sortProductsByPriceAscending(session);
            sortProductsByPriceDescending(session);
            sortProductsByQuantityDescending(session);
            getFirstThreeProducts(session);
            countTotalProducts(session);
            findMinMaxPrice(session);
            filterProductsByPriceRange(session, 20.0, 100.0);
            findProductsStartingWith(session, "D");

        } finally {
            session.close();
            factory.close();
        }
    }

    public static void sortProductsByPriceAscending(Session session) {
        String hql = "FROM Product p ORDER BY p.price ASC";
        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\n=== Price ASC ===");
        products.forEach(System.out::println);
    }

    public static void sortProductsByPriceDescending(Session session) {
        String hql = "FROM Product p ORDER BY p.price DESC";
        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\n=== Price DESC ===");
        products.forEach(System.out::println);
    }

    public static void sortProductsByQuantityDescending(Session session) {
        String hql = "FROM Product p ORDER BY p.quantity DESC";
        Query<Product> query = session.createQuery(hql, Product.class);
        List<Product> products = query.list();

        System.out.println("\n=== Quantity DESC ===");
        products.forEach(System.out::println);
    }

    public static void getFirstThreeProducts(Session session) {
        String hql = "FROM Product";
        Query<Product> query = session.createQuery(hql, Product.class);
        query.setFirstResult(0);
        query.setMaxResults(3);

        System.out.println("\n=== First 3 Products ===");
        query.list().forEach(System.out::println);
    }

    public static void countTotalProducts(Session session) {
        String hql = "SELECT COUNT(p) FROM Product p";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long count = query.uniqueResult();

        System.out.println("\nTotal Products: " + count);
    }

    public static void findMinMaxPrice(Session session) {
        String hql = "SELECT MIN(p.price), MAX(p.price) FROM Product p";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        Object[] result = query.uniqueResult();

        System.out.println("\nMin Price: " + result[0]);
        System.out.println("Max Price: " + result[1]);
    }

    public static void filterProductsByPriceRange(Session session, double min, double max) {
        String hql = "FROM Product p WHERE p.price BETWEEN :min AND :max";
        Query<Product> query = session.createQuery(hql, Product.class);
        query.setParameter("min", min);
        query.setParameter("max", max);

        System.out.println("\n=== Price Between " + min + " and " + max + " ===");
        query.list().forEach(System.out::println);
    }

    public static void findProductsStartingWith(Session session, String prefix) {
        String hql = "FROM Product p WHERE p.name LIKE :pattern";
        Query<Product> query = session.createQuery(hql, Product.class);
        query.setParameter("pattern", prefix + "%");

        System.out.println("\n=== Starts with " + prefix + " ===");
        query.list().forEach(System.out::println);
    }
}