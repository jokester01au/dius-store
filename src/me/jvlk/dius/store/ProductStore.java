package me.jvlk.dius.store;


import me.jvlk.dius.store.models.Product;
import me.jvlk.dius.store.persistence.CsvLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FUTURE - Singletons are not a great pattern. Dependency Injection would be highly preferrable
 */
public class ProductStore {
    private static final String PRODUCTS_FILE = "products.csv";

    private static ProductStore _INSTANCE = new ProductStore();

    private ProductStore() {
        try {
            List<Product> productList = new CsvLoader<>(Product.class, new BufferedReader(new FileReader(PRODUCTS_FILE))).loadAll();
            productList.forEach(p -> this.products.put(p.getSku(), p));
        } catch (Exception e) {
            // FIXME - fix exception handling
            throw new RuntimeException(e);
        }
    }

    public static ProductStore getInstance() {
        return _INSTANCE;
    }

    private Map<String, Product> products = new ConcurrentHashMap<>();


    public Product findProduct(String sku) {
        Product product = products.get(sku);
        if (product == null) throw new IllegalArgumentException(String.format("No product found with sku %s", sku));
        return product;
    }


    public void addProduct(Product product) {
        this.products.put(product.getSku(), product);
    }
}
