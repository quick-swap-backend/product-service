package quickswap.productservice.application

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import quickswap.productservice.application.dto.ProductFindOnScrollResponse
import quickswap.productservice.application.`in`.ProductFinder
import quickswap.productservice.application.out.ProductRepository
import quickswap.productservice.domain.product.ProductCategory
import quickswap.productservice.domain.product.ProductStatus
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class ProductQueryService(
  private val repository: ProductRepository,
) : ProductFinder {

  override fun getProductsOnScroll(
    cursorTime: Long?,
    size: Int,
    category: ProductCategory?
  ): ProductFindOnScrollResponse {
    require(size in 1..100) { throw IllegalArgumentException("size 가 너무 큽니다. $size") }

    val pageable = PageRequest.of(0, size + 1)

    val products = if (cursorTime == null) {
      repository.findByProductStatusOrderByCreatedAtDesc(
        status = ProductStatus.ON_SALE,
        pageable = pageable
      )
    } else {
      val cursorDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(cursorTime),
        ZoneId.systemDefault()
      )
      repository.findByProductStatusWithCursor(
        status = ProductStatus.ON_SALE,
        cursorTime = cursorDateTime,
        pageable = pageable
      )
    }

    val hasNext = products.size > size
    val resultProducts = if (hasNext) products.dropLast(1) else products

    val nextCursor = if (hasNext) {
      resultProducts.last().createdAt
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    } else null

    return ProductFindOnScrollResponse.of(
      products = resultProducts,
      nextCursor = nextCursor,
      hasNext = hasNext
    )
  }

}