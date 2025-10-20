package quickswap.productservice.domain.product

/*
* 최종 상태 후보값: COMPLETED, REFUNDED, DELETE
* */
enum class ProductStatus {

  DELETED,               // 삭제됨
  ON_SALE,               // 판매중
  RESERVED,              // 예약중
  PAID,                  // 결제 완료
  COMPLETED,             // 판매 완료
  REFUND_REQUESTED,      // 환불 진행 중
  REFUNDED,              //환불 완료
}
