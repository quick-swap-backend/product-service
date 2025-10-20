package quickswap.productservice.fixture

import quickswap.commons.domain.shared.IdProvider
import quickswap.commons.domain.shared.vo.Money
import quickswap.productservice.domain.product.ProductCategory
import quickswap.productservice.domain.product.ProductCreateRequest
import java.time.LocalDate
import java.util.UUID

class ProductFixture {
  companion object {

    fun getProductCreateRequest(): ProductCreateRequest {
      val detail = ProductCreateRequest.Detail(
        description = "튼튼하고 깨끗한 킹 사이즈 에이스 침대",
        purchaseDate = LocalDate.now().minusYears(1),
      )

      return ProductCreateRequest(
        title = "침대 팔아요",
        price = Money.of(10_000L),
        category = ProductCategory.FURNITURE,
        detail = detail,
      )
    }

    fun getProductIdProvider(): IdProvider {
      return object : IdProvider {
        override fun provide(): String {
          return UUID.randomUUID().toString()
        }
      }
    }

  }
}