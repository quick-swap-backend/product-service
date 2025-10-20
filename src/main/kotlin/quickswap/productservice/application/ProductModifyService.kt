package quickswap.productservice.application

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import quickswap.commons.application.shared.ports.out.AuthenticationContext
import quickswap.commons.domain.shared.IdProvider
import quickswap.commons.domain.shared.id.ProductId
import quickswap.productservice.application.`in`.ProductCreator
import quickswap.productservice.application.`in`.ProductUpdater
import quickswap.productservice.application.out.ProductRepository
import quickswap.productservice.domain.product.Product
import quickswap.productservice.domain.product.ProductCreateRequest

@Transactional
@Service
class ProductModifyService(
  private val repository: ProductRepository,
  private val authContext: AuthenticationContext,
  private val idProvider: IdProvider,
): ProductCreator, ProductUpdater {

  override fun createProduct(request: ProductCreateRequest): Product {
    val userId = authContext.getCurrentUserId()
    val userEmail = authContext.getCurrentEmail()

    val product = Product.of(idProvider, request, userId, userEmail)

    return repository.save(product)
  }

  override fun delete(id: ProductId): ProductId {
    val foundProduct = repository.findById(id)
      .orElseThrow { IllegalArgumentException("Product 를 찾을 수 없습니다. id: ${id.value}") }

    require(authContext.getCurrentUserId() == foundProduct.seller.id) { "작성자만 삭제 할 수 있습니다." }

    foundProduct.deleteBySeller()

    return foundProduct.id
  }
}