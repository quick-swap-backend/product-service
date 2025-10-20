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

  @Query(
    """
    select p from Product p
    where p.status = :status
    and (:category is null or p.category = :category)
    order by p.createdAt desc
  """
  )
  fun findByProductStatusOrderByCreatedAtDesc(
    category: ProductCategory? = null,
    status: ProductStatus,
    pageable: Pageable,
  ): List<Product>

  @EntityGraph(attributePaths = ["detail"])
  @Query(
    """
    select p from Product p
    where p.status = :status
    and (:category is null or p.category = :category)
    and p.createdAt < :cursorTime
    order by p.createdAt desc
    """
  )
  fun findByProductStatusWithCursor(
    category: ProductCategory? = null,
    status: ProductStatus,
    pageable: Pageable,
    cursorTime: LocalDateTime,
  ): List<Product>

}