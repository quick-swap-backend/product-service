package quickswap.productservice.application

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest
import quickswap.commons.domain.shared.id.ProductId
import quickswap.productservice.application.out.ProductRepository
import quickswap.productservice.domain.product.Product
import quickswap.productservice.domain.product.ProductStatus
import java.time.LocalDateTime
import java.time.ZoneId

class ProductQueryServiceTest {

  val repository: ProductRepository = mockk()
  val service = ProductQueryService(repository)

  @Test
  fun `첫 페이지 조회 - 다음 페이지가 있는 경우`() {
    val size = 10
    val pageable = PageRequest.of(0, size + 1)

    val products = (1..size + 1).map { index ->
      mockk<Product>(relaxed = true).apply {
        every { createdAt } returns LocalDateTime.now().minusHours(index.toLong())
        every { id } returns ProductId(index.toString())
      }
    }

    every {
      repository.findByProductStatusOrderByCreatedAtDesc(
        null,
        ProductStatus.ON_SALE,
        pageable
      )
    } returns products

    val result = service.findProducts(cursorTime = null, size = size, category = null)

    assert(result.products.size == 10)
    assertTrue { result.hasNext }
    assertNotNull(result.nextCursor)
  }

  @Test
  fun `첫 페이지 조회 - 다음 페이지가 없는 경우`() {
    val size = 10
    val pageable = PageRequest.of(0, size + 1)

    val products = (1..size).map { index ->
      mockk<Product>(relaxed = true).apply {
        every { createdAt } returns LocalDateTime.now().minusHours(index.toLong())
        every { id } returns ProductId(index.toString())
      }
    }

    every {
      repository.findByProductStatusOrderByCreatedAtDesc(
        null,
        ProductStatus.ON_SALE,
        pageable
      )
    } returns products

    val result = service.findProducts(cursorTime = null, size = size, category = null)

    assert(result.products.size == 10)
    assertFalse { result.hasNext }
    assertNull(result.nextCursor)
  }

  @Test
  fun `nextCursor는 마지막 상품의 createdAt 타임스탬프여야 함`() {
    val size = 10
    val pageable = PageRequest.of(0, size + 1)

    val products = (1..size + 1).map { index ->
      mockk<Product>(relaxed = true).apply {
        every { createdAt } returns LocalDateTime.now().minusHours(index.toLong())
        every { id } returns ProductId(index.toString())
      }
    }

    every {
      repository.findByProductStatusOrderByCreatedAtDesc(
        null,
        ProductStatus.ON_SALE,
        pageable
      )
    } returns products

    val result = service.findProducts(
      cursorTime = null,
      size = size,
      category = null
    )

    val expectedCursor = products[size-1].createdAt
      .atZone(ZoneId.systemDefault())
      .toInstant()
      .toEpochMilli()

    assert(result.nextCursor == expectedCursor)
    assert(result.products.size == size)
  }

  @Test
  fun `size 범위 테스트`() {
    assertThrows<IllegalArgumentException> { service.findProducts(null, 0, null) }
    assertThrows<IllegalArgumentException> { service.findProducts(null, 101, null) }
  }

}