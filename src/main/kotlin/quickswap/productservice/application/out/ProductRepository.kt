package quickswap.productservice.application.out

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import quickswap.commons.domain.shared.id.ProductId
import quickswap.productservice.domain.product.Product
import quickswap.productservice.domain.product.ProductCategory
import quickswap.productservice.domain.product.ProductStatus
import java.time.LocalDateTime

interface ProductRepository: JpaRepository<Product, ProductId> {

  @EntityGraph(attributePaths = ["detail"])
  @Query("""
    select p from Product p
    where p.productStatus = :status
    and (:category is null or p.category = :category)
    order by p.createdAt desc
  """)
  fun findByProductStatusOrderByCreatedAtDesc(
    status: ProductStatus,
    pageable: Pageable,
    category: ProductCategory? = null,
  ): List<Product>

  @EntityGraph(attributePaths = ["detail"])
  @Query("""
    select p from Product p
    where p.productStatus = :status
    and (:category is null or p.category = :category)
    and p.createdAt < :cursorTime
    order by p.createdAt desc
  """)
  fun findByProductStatusWithCursor(
    status: ProductStatus,
    cursorTime: LocalDateTime,
    pageable: Pageable,
    category: ProductCategory? = null,
  ): List<Product>
}