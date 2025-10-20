package quickswap.productservice.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import quickswap.commons.application.shared.ports.out.AuthenticationContext
import quickswap.commons.domain.shared.IdProvider
import quickswap.commons.domain.shared.id.UserId
import quickswap.commons.domain.shared.vo.Email
import quickswap.productservice.application.`in`.ProductCreator
import quickswap.productservice.application.out.ProductRepository
import quickswap.productservice.domain.product.ProductCreateRequest
import quickswap.productservice.fixture.ProductFixture
import java.util.UUID

class ProductModifyServiceTest {
  val repository: ProductRepository = mockk()
  val authContext: AuthenticationContext = mockk()
  val idProvider: IdProvider = mockk()
  val service: ProductCreator = ProductModifyService(repository, authContext, idProvider)

  @Test
  fun `product 생성 성공 케이스`() {
    val request: ProductCreateRequest = ProductFixture.getProductCreateRequest()
    val userId = UserId(UUID.randomUUID().toString())
    val userEmail = Email("test@test.com")
    val productId = UUID.randomUUID().toString()

    every { authContext.getCurrentUserId() } returns userId
    every { authContext.getCurrentEmail() } returns userEmail
    every { idProvider.provide() } returns productId
    every { repository.save(any()) } returnsArgument 0

    val createdProduct = service.createProduct(request)

    assertNotNull(createdProduct)
    assert(request.title == createdProduct.title)
    assert(request.price == createdProduct.price)
    assert(productId == createdProduct.id.value)
    assert(userId == createdProduct.seller.id)

    verify(exactly = 1) { repository.save(any()) }
  }
}