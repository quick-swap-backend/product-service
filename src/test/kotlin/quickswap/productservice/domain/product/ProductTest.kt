package quickswap.productservice.domain.product

import org.junit.jupiter.api.Test
import quickswap.commons.domain.shared.id.UserId
import quickswap.commons.domain.shared.vo.Email
import quickswap.productservice.fixture.ProductFixture
import java.util.UUID

class ProductTest {

  @Test
  fun of() {
    val request = ProductFixture.getProductCreateRequest()
    val idProvider = ProductFixture.getProductIdProvider()
    val product = Product.of(idProvider, request, UserId(UUID.randomUUID().toString()), Email("test@test.com"))

    assert(product.productStatus == ProductStatus.ON_SALE)
  }
}