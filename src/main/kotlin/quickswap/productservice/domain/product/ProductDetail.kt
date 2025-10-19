package quickswap.productservice.domain.product

import jakarta.persistence.*
import quickswap.commons.domain.shared.id.ProductId
import java.time.LocalDate

@Table(name = "product_details")
@Entity
class ProductDetail private constructor(

  @EmbeddedId
  val id: ProductId,

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  val product: Product,

  /* 상세 설명 */
  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  val description: String,

  /* 구매일 */
  @Column(name = "purchase_date", nullable = false)
  val purchaseDate: LocalDate,

  /* 하자 정보 */
  @Column(name = "defects", columnDefinition = "TEXT", nullable = true)
  val defects: String?

) {

  companion object {
    fun of(
      product: Product,
      request: ProductCreateRequest.Detail
    ): ProductDetail {
      return ProductDetail(
        id = product.id,
        product = product,
        description = request.description,
        purchaseDate = request.purchaseDate,
        defects = request.defects
      )
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as ProductDetail
    return id == other.id
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}