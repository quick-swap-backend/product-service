package quickswap.productservice.application.`in`

import quickswap.productservice.application.dto.ProductsFindResponse
import quickswap.productservice.domain.product.ProductCategory

interface ProductFinder {
  fun findProducts(cursorTime: Long?, size: Int, category: ProductCategory?): ProductsFindResponse
}