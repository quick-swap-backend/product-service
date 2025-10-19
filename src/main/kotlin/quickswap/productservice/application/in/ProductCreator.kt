package quickswap.productservice.application.`in`

import quickswap.productservice.domain.product.Product
import quickswap.productservice.domain.product.ProductCreateRequest

interface ProductCreator {
  fun createProduct(request: ProductCreateRequest): Product
}