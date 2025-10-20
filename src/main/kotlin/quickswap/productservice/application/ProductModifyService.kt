package quickswap.productservice.application

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import quickswap.commons.application.shared.ports.out.AuthenticationContext
import quickswap.commons.domain.shared.IdProvider
import quickswap.commons.domain.shared.id.ProductId
import quickswap.productservice.application.`in`.ProductCreator
import quickswap.productservice.application.`in`.ProductUpdater
import quickswap.productservice.application.out.OutboxHandler
import quickswap.productservice.application.out.ProductRepository
import quickswap.productservice.domain.product.Product
import quickswap.productservice.domain.product.ProductCreateRequest

@Transactional
@Service
class ProductModifyService(
  private val repository: ProductRepository,
  private val authContext: AuthenticationContext,
  private val idProvider: IdProvider,
  private val outboxHandler: OutboxHandler,
): ProductCreator, ProductUpdater {

  override fun createProduct(request: ProductCreateRequest): Product {
    val userId = authContext.getCurrentUserId()
    val userEmail = authContext.getCurrentEmail()

    val product = Product.of(idProvider, request, userId, userEmail)

    val saved = repository.save(product)
    outboxHandler.execute(saved)

    return saved
  }

  override fun deleteBySeller(id: ProductId): ProductId {
    val foundProduct = findByIdOrThrow(id)

    require(authContext.getCurrentUserId() == foundProduct.seller.id) { "작성자만 삭제 할 수 있습니다." }

    foundProduct.deleteBySeller()

    return foundProduct.id
  }

  override fun cancelByTrade(id: ProductId): ProductId {
    val foundProduct = findByIdOrThrow(id)

    foundProduct.cancelByTrade()

    return foundProduct.id
  }

  override fun restoreToReserved(id: ProductId): ProductId {
    TODO("Not yet implemented")
  }

  private fun findByIdOrThrow(id: ProductId): Product {
    return repository.findById(id)
      .orElseThrow { IllegalArgumentException("Product 를 찾을 수 없습니다. id: ${id.value}") }
  }
}