package quickswap.productservice.application

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import quickswap.productservice.application.dto.ProductsFindResponse
import quickswap.productservice.application.`in`.ProductFinder
import quickswap.productservice.application.out.ProductRepository
import quickswap.productservice.domain.product.Product
import quickswap.productservice.domain.product.ProductCategory
import quickswap.productservice.domain.product.ProductStatus
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class ProductQueryService(
  private val repository: ProductRepository,
) : ProductFinder {

  override fun findProducts(
    cursorTime: Long?,
    size: Int,
    category: ProductCategory?
  ): ProductsFindResponse {

    val products = getProductsByCursor(cursorTime, category, size)

    val hasNext = productsHasNext(products, size)

    val nextCursor = if (hasNext) getNextCursor(products) else null

    return ProductsFindResponse.of(
      products = products,
      nextCursor = nextCursor,
      hasNext = hasNext
    )
  }

  private fun getProductsByCursor(cursorTime: Long?, category: ProductCategory?, size: Int): List<Product> {
    require(size in 1..100) { "size 가 너무 큽니다. $size" }

    val pageable = PageRequest.of(0, size + 1)

    val products = if (cursorTime == null) {
      repository.findByProductStatusOrderByCreatedAtDesc(
        category,
        ProductStatus.ON_SALE,
        pageable
      )
    } else {
      val cursorDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(cursorTime),
        ZoneId.systemDefault()
      )
      repository.findByProductStatusWithCursor(
        category,
        ProductStatus.ON_SALE,
        pageable,
        cursorDateTime
      )
    }

    val hasNext = productsHasNext(products, size)
    val resultProducts = if (hasNext) products.dropLast(1) else products
    return resultProducts
  }

  private fun productsHasNext(products: List<Product>, sizeCondition: Int): Boolean {
    return products.size > sizeCondition
  }

  private fun getNextCursor(products:List<Product>): Long {
    require(!products.isEmpty()) { "products 가 비어있습니다." }

    return products.last().createdAt
      .atZone(ZoneId.systemDefault())
      .toInstant()
      .toEpochMilli()
  }

}