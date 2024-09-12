package com.example.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@NamedQueries(
  {
    @NamedQuery(
      name = "com.example.Products.findAll",
      query = "SELECT e FROM Products e"
    ),
  }
)
public class Products {

  /**
   * Unique Identifier for the Product
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long productId;

  /**
   * Name for the Product
   */
  private String name;

  /**
   * Price of the Product
   */
  @Column(precision = 10, scale = 2)
  private BigDecimal price;

  /**
   * Creation date is System generated
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate creationDate;
}
