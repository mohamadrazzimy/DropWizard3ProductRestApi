package com.example.db;

import com.example.core.Products;
import io.dropwizard.hibernate.AbstractDAO;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import org.hibernate.SessionFactory;

public class ProductsDAO extends AbstractDAO<Products> {

  public ProductsDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public List<Products> getAllProducts() {
    List<Products> products = list(
      namedTypedQuery("com.example.Products.findAll")
    );
    return products;
  }

  public Optional<Products> getById(Long productId) {
    Optional<Products> product = Optional.ofNullable(get(productId));
    return product;
  }

  public Products createOrUpdateProduct(Products product) {
    if (product.getCreationDate() == null) {
      product.setCreationDate(LocalDate.now());
    }
    return persist(product);
  }

  public boolean deleteProduct(Long id) {
    boolean isDeleted = false;
    try {
      Optional<Products> deletionProduct = getById(id);
      if (deletionProduct.isPresent()) {
        currentSession().delete(deletionProduct.get());
        isDeleted = true;
      }
    } catch (Exception ex) {
      /* LOG ERROR */
    }
    return isDeleted;
  }
}
