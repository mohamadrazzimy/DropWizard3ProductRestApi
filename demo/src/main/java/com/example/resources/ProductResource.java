package com.example.resources;

import com.example.db.ProductsDAO;
import com.example.core.Products;
import io.dropwizard.hibernate.UnitOfWork;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/product-app/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

  private final ProductsDAO productsDAO;

  public ProductResource(ProductsDAO productsDAO) {
    this.productsDAO = productsDAO;
  }

  @GET
  @UnitOfWork
  public Response getAllProducts() {
    Response response = Response.ok(productsDAO.getAllProducts()).build();
    return response;
  }

  @GET
  @Path("/{id}")
  @UnitOfWork
  public Optional<Products> getById(@PathParam("id") Long id) {
    Optional<Products> product = productsDAO.getById(id);
    return product;
  }

  @POST
  @UnitOfWork
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createProducts(Products product) {
    Response response = Response
      .accepted(productsDAO.createOrUpdateProduct(product))
      .build();
    return response;
  }

  @PUT
  @Path("/{id}")
  @UnitOfWork
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateProducts(Products products) {
    Response response = Response
      .accepted(productsDAO.createOrUpdateProduct(products))
      .build();
    return response;
  }

  @DELETE
  @Path("/{id}")
  @UnitOfWork
  public Response deleteProduct(@PathParam("id") Long id) {
    try {
      if (productsDAO.deleteProduct(id)) {
        return Response.noContent().build();
      }
    } catch (IllegalArgumentException exception) {
      return Response.serverError().build();
    } finally {
      /* LOG */
    }
    return Response.status(Response.Status.NOT_FOUND).build();
  }

}
