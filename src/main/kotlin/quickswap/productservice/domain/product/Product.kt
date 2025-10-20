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
  var status: ProductStatus = ProductStatus.ON_SALE,

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  var category: ProductCategory = ProductCategory.OTHER,

  @Embedded
  val seller: Seller,

  @OneToOne(
    mappedBy = "product",
    cascade = [CascadeType.ALL],
    orphanRemoval = true,
    fetch = FetchType.LAZY
  )
  var detail: ProductDetail? = null,

  val createdAt: LocalDateTime = LocalDateTime.now()

) {

  fun reserved() {
    require(status == ProductStatus.ON_SALE)
    { "거래 신청은 판매중인 상품만 가능합니다. id: ${id.value}" }
    status = ProductStatus.RESERVED
  }

  fun pay() {
    require(status == ProductStatus.RESERVED)
    { "결제는 예약 중인 상품만 가능합니다. id: ${id.value}" }
    status = ProductStatus.PAID
  }

  fun rejectRefund() {
    require(status == ProductStatus.REFUND_REQUESTED)
    { "환불 거절은 환불 요청 상태의 상품만 가능합니다. id: ${id.value}" }
    status = ProductStatus.PAID
  }

  fun completed() {
    require(status == ProductStatus.PAID)
    { "거래 완료는 지불이 완료 된 상품만 가능합니다. id: ${id.value}" }
    status = ProductStatus.COMPLETED
  }

  fun refundRequested() {
    require(status == ProductStatus.PAID)
    { "환불 요청은 지불 완료 상태에서만 가능합니다. id: ${id.value}" }
    status = ProductStatus.REFUND_REQUESTED
  }

  fun refunded() {
    require(status == ProductStatus.REFUND_REQUESTED)
    { "환불 완료는 환불 요청상태의 상품만 가능합니다. id: ${id.value}" }
    status = ProductStatus.REFUNDED
  }

  fun deleteBySeller() {
    require(status == ProductStatus.ON_SALE)
    { "판매자는 판매중인 상품만 취소할 수 있습니다. id: ${id.value}" }
    status = ProductStatus.DELETED
  }

  fun cancelByTrade() {
    require(status == ProductStatus.RESERVED)
    { "거래 취소는 예약 중인 상품만 가능합니다. id: ${id.value}" }
    status = ProductStatus.DELETED
  }

  private fun createDetail(detail: ProductCreateRequest.Detail) {
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
        category = request.category,
        seller = Seller(sellerId, sellerEmail),
      )

      product.createDetail(detail = request.detail)

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
