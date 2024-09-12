package com.example;

import com.example.db.ProductsDAO;
import com.example.core.Products;
import com.example.resources.ProductResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import java.sql.SQLException;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoApplication extends Application<DemoConfiguration> {

  public static void main(String[] args) throws Exception {
    new DemoApplication().run(args);
  }

  @Override
  public void run(DemoConfiguration demoConfiguration, Environment environment)
    throws Exception {
    try {
      // Start H2 TCP server
      Server
        .createWebServer("-web", "-webAllowOthers", "-tcp", "-tcpAllowOthers")
        .start();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // Registering REST resources
    final ProductsDAO productsDAO = new ProductsDAO(
      hibernateBundle.getSessionFactory()
    );
    environment.jersey().register(new ProductResource(productsDAO));

    // Debugging helper
    environment.jersey().register(new JsonProcessingExceptionMapper(true));

    //--- Start of the CORS Configuration --//
    final FilterRegistration.Dynamic cors = environment
      .servlets()
      .addFilter("CORS", CrossOriginFilter.class);

    cors.setInitParameter(
      CrossOriginFilter.ALLOWED_ORIGINS_PARAM,
      "http://localhost:3000"
    );
    cors.setInitParameter(
      CrossOriginFilter.ALLOWED_HEADERS_PARAM,
      "X-Requested-With,Content-Type,Accept,Origin,Authorization"
    );
    cors.setInitParameter(
      CrossOriginFilter.ALLOWED_METHODS_PARAM,
      "OPTIONS,GET,PUT,POST,DELETE,HEAD"
    );
    cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

    // Add URL mapping
    cors.addMappingForUrlPatterns(
      EnumSet.allOf(DispatcherType.class),
      true,
      "/*"
    );
    //--- End of the CORS Configuration --//
  }

  private final HibernateBundle<DemoConfiguration> hibernateBundle = new HibernateBundle<DemoConfiguration>(
    Products.class
  ) {
    @Override
    public DataSourceFactory getDataSourceFactory(
      DemoConfiguration configuration
    ) {
      return configuration.getDataSourceFactory();
    }
  };

  @Override
  public void initialize(Bootstrap<DemoConfiguration> bootstrap) {
    bootstrap.addBundle(hibernateBundle);
  }
}
