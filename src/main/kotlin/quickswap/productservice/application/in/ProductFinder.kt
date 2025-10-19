package quickswap.productservice.application.`in`

import quickswap.productservice.application.dto.ProductFindOnScrollResponse

interface ProductFinder {
  fun getProductsOnScroll(cursorTime: Long?, size: Int): ProductFindOnScrollResponse
}