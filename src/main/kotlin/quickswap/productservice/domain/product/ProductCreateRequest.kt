package quickswap.productservice.domain.product

import quickswap.commons.domain.shared.vo.Money
import java.time.LocalDate

data class ProductCreateRequest(
  val title: String,
  val price: Money,
  val category: ProductCategory,
  val detail: Detail,
) {
  data class Detail(
    val description: String,
    val purchaseDate: LocalDate,
    val defects: String? = null
  )
}

