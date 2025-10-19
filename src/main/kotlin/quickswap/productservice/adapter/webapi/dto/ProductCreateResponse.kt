package quickswap.productservice.adapter.webapi.dto

import quickswap.commons.domain.shared.id.ProductId

data class ProductCreateResponse(
  val id: ProductId,
  val title: String,
)