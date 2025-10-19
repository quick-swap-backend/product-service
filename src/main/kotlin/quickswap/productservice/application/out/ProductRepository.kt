package quickswap.productservice.application.out

import org.springframework.data.jpa.repository.JpaRepository
import quickswap.commons.domain.shared.id.ProductId
import quickswap.productservice.domain.product.Product

interface ProductRepository: JpaRepository<Product, ProductId> {
}