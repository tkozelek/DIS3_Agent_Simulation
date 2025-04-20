package config;

import entity.product.Product;

import java.util.Comparator;

public class ProductActivityComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        return Integer.compare(o1.getId(), o2.getId());
    }
}
