package quickswap.productservice.application.`in`

import quickswap.commons.domain.shared.id.ProductId

interface ProductUpdater {

  fun delete(id: ProductId): ProductId

}