package quickswap.productservice.application.`in`

import quickswap.productservice.application.dto.ProductFindOnScrollResponse
import quickswap.productservice.domain.product.ProductCategory

interface ProductFinder {
  fun getProductsOnScroll(cursorTime: Long?, size: Int, category: ProductCategory?): ProductFindOnScrollResponse
}