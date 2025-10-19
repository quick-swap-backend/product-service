package quickswap.productservice.domain.product

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import quickswap.commons.adapter.shared.persistence.converter.MoneyConverter
import quickswap.commons.domain.shared.IdProvider
import quickswap.commons.domain.shared.id.ProductId
import quickswap.commons.domain.shared.id.UserId
import quickswap.commons.domain.shared.vo.Email
import quickswap.commons.domain.shared.vo.Money
import java.time.LocalDateTime

@Table(name = "products")
@Entity
class Product private constructor(

  @EmbeddedId
  val id: ProductId,

  @Column(name = "title", nullable = false, length = 255)
  var title: String,

  @Column(name = "price", nullable = false)
  @Convert(converter = MoneyConverter::class)
  var price: Money,

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  var productStatus: ProductStatus = ProductStatus.ON_SALE,

  @Embedded
  val seller: Seller,

  @OneToOne(
    mappedBy = "product",
    cascade = [CascadeType.ALL],
    orphanRemoval = true,
    fetch = FetchType.LAZY
  )
  var detail: ProductDetail? = null,

  var createdAt: LocalDateTime = LocalDateTime.now()

) {

  fun createDetail(detail: ProductCreateRequest.Detail) {
    val detail = ProductDetail.of(this, detail)
    this.detail = detail
  }

companion object {
  fun of(
    idProvider: IdProvider,
    request: ProductCreateRequest,
    sellerId: UserId,
    sellerEmail: Email
  ): Product {
    val product = Product(
      id = ProductId(idProvider.provide()),
      title = request.title,
      price = request.price,
      seller = Seller(sellerId, sellerEmail)
    )

    product.detail = ProductDetail.of(product, request.detail)

    return product
  }
}

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Product

    return id == other.id
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}
