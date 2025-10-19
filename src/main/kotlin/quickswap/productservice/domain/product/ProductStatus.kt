package quickswap.productservice.domain.product

enum class ProductStatus {
  ON_SALE,     // 판매중
  RESERVED,    // 예약중
  SOLD_OUT,    // 판매완료
  DELETED      // 삭제됨
}