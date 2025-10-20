package quickswap.productservice.domain.product

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import quickswap.commons.domain.shared.id.UserId
import quickswap.commons.domain.shared.vo.Email
import quickswap.productservice.fixture.ProductFixture
import java.util.UUID

class ProductTest {

  @Test
  fun `상품이 생성되면 판매중 상태여야 한다`() {
    val request = ProductFixture.getProductCreateRequest()
    val idProvider = ProductFixture.getProductIdProvider()
    val product = Product.of(
      idProvider,
      request,
      UserId(UUID.randomUUID().toString()),
      Email("test@test.com")
    )

    assert(product.status == ProductStatus.ON_SALE)
  }

  @Test
  fun `판매중 상품은 예약할 수 있다`() {
    val product = createProduct()

    product.reserved()

    assert(product.status == ProductStatus.RESERVED)
  }

  @Test
  fun `판매중이 아닌 상품은 예약할 수 없다`() {
    val product = createProduct()
    product.reserved()

    assertThrows<IllegalArgumentException> {
      product.reserved()
    }
  }

  @Test
  fun `예약중인 상품은 결제할 수 있다`() {
    val product = createProduct()
    product.reserved()

    product.pay()

    assert(product.status == ProductStatus.PAID)
  }

  @Test
  fun `예약중이 아닌 상품은 결제할 수 없다`() {
    val product = createProduct()

    assertThrows<IllegalArgumentException> {
      product.pay()
    }
  }

  @Test
  fun `결제 완료 상품은 환불 요청할 수 있다`() {
    val product = createProduct()
    product.reserved()
    product.pay()

    product.refundRequested()

    assert(product.status == ProductStatus.REFUND_REQUESTED)
  }

  @Test
  fun `환불 요청 상태에서 환불을 거절하면 결제 완료 상태로 돌아간다`() {
    val product = createProduct()
    product.reserved()
    product.pay()
    product.refundRequested()

    product.rejectRefund()

    assert(product.status == ProductStatus.PAID)
  }

  @Test
  fun `환불 요청 상태에서 환불 완료할 수 있다`() {
    val product = createProduct()
    product.reserved()
    product.pay()
    product.refundRequested()

    product.refunded()

    assert(product.status == ProductStatus.REFUNDED)
  }

  @Test
  fun `결제 완료 상품은 거래 완료할 수 있다`() {
    val product = createProduct()
    product.reserved()
    product.pay()

    product.completed()

    assert(product.status == ProductStatus.COMPLETED)
  }

  @Test
  fun `판매자는 판매중인 상품만 취소할 수 있다`() {
    val product = createProduct()

    product.deleteBySeller()

    assert(product.status == ProductStatus.DELETED)
  }

  @Test
  fun `판매자는 예약중인 상품을 취소할 수 없다`() {
    val product = createProduct()
    product.reserved()

    assertThrows<IllegalArgumentException> {
      product.deleteBySeller()
    }
  }

  @Test
  fun `거래 취소는 예약중인 상품만 가능하다`() {
    val product = createProduct()
    product.reserved()

    product.cancelByTrade()

    assert(product.status == ProductStatus.DELETED)
  }

  @Test
  fun `거래 취소는 판매중인 상품에 대해 불가능하다`() {
    val product = createProduct()

    assertThrows<IllegalArgumentException> {
      product.cancelByTrade()
    }
  }

  @Test
  fun `결제 완료 상품은 판매자가 취소할 수 없다`() {
    val product = createProduct()
    product.reserved()
    product.pay()

    assertThrows<IllegalArgumentException> {
      product.deleteBySeller()
    }
  }

  @Test
  fun `결제 완료 상품은 거래 취소할 수 없다`() {
    val product = createProduct()
    product.reserved()
    product.pay()

    assertThrows<IllegalArgumentException> {
      product.cancelByTrade()
    }
  }

  private fun createProduct(): Product {
    val request = ProductFixture.getProductCreateRequest()
    val idProvider = ProductFixture.getProductIdProvider()
    return Product.of(
      idProvider,
      request,
      UserId(UUID.randomUUID().toString()),
      Email("test@test.com")
    )
  }
}