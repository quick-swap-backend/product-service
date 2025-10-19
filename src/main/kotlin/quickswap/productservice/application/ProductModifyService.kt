package quickswap.productservice.application

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import quickswap.commons.application.shared.ports.out.AuthenticationContext
import quickswap.commons.domain.shared.IdProvider
import quickswap.productservice.application.`in`.ProductCreator
import quickswap.productservice.application.out.ProductRepository
import quickswap.productservice.domain.product.Product
import quickswap.productservice.domain.product.ProductCreateRequest

@Transactional
@Service
class ProductModifyService(
  private val repository: ProductRepository,
  private val authContext: AuthenticationContext,
  private val idProvider: IdProvider,
): ProductCreator {

  override fun createProduct(request: ProductCreateRequest): Product {
    val userId = authContext.getCurrentUserId()
    val userEmail = authContext.getCurrentEmail()

    val product = Product.of(idProvider, request, userId, userEmail)

    return repository.save(product)
  }

}