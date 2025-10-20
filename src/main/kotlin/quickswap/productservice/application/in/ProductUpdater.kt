package quickswap.productservice.application.`in`

import quickswap.commons.domain.shared.id.ProductId

interface ProductUpdater {

  fun deleteBySeller(id: ProductId): ProductId

  fun cancelByTrade(id: ProductId): ProductId

  fun restoreToReserved(id: ProductId): ProductId
}