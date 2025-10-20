package quickswap.productservice.application.out

import quickswap.productservice.domain.product.Product

interface OutboxHandler {
  fun execute(product: Product): String
}