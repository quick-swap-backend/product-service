package quickswap.productservice.application.dto

import quickswap.commons.domain.shared.vo.Email
import quickswap.commons.domain.shared.vo.Money
import quickswap.productservice.domain.product.ProductCategory
import quickswap.productservice.domain.product.ProductStatus
import java.time.LocalDateTime
import java.time.ZoneId

class ProductsFindResponse private constructor(
  val products: List<Product>,
  val nextCursor: Long?,
  val hasNext: Boolean,
) {

  companion object {
    fun of(
      products: List<quickswap.productservice.domain.product.Product>,
      nextCursor: Long?,
      hasNext: Boolean,
    ): ProductsFindResponse {
      return ProductsFindResponse(
        products = Product.of(products),
        nextCursor = nextCursor,
        hasNext = hasNext,
      )
    }
  }

  data class Product(
    val id: String,
    val title: String,
    val price: Money,
    val productStatus: ProductStatus,
    val category: ProductCategory,
    val seller: Seller,
    val createdAt: LocalDateTime,
    val cursorTime: Long,
  ) {
    companion object {
      fun of(product: quickswap.productservice.domain.product.Product): Product{
        return Product(
          id = product.id.value,
          title = product.title,
          price = product.price,
          productStatus = product.status,
          category = product.category,
          seller = Seller(product.seller.id.value, product.seller.email),
          createdAt = product.createdAt,
          cursorTime = product.createdAt
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        )
      }

      fun of(products: List<quickswap.productservice.domain.product.Product>): List<Product>{
        return products.map { of(it) }
      }
    }
  }

  data class Seller(
    val id: String,
    val email: Email,
  )
}