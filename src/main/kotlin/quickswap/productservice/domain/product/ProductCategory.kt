package quickswap.productservice.domain.product

enum class ProductCategory(
  val displayName: String,
  val description: String
) {
  DIGITAL_DEVICE("디지털/가전", "휴대폰, 태블릿, 노트북, PC, 카메라 등"),
  FURNITURE("가구/인테리어", "침대, 소파, 책상, 의자, 조명 등"),
  KIDS("유아동/유아용품", "유아동 의류, 장난감, 유모차, 카시트 등"),
  FASHION_WOMEN("여성의류/잡화", "의류, 신발, 가방, 액세서리 등"),
  FASHION_MEN("남성패션/잡화", "의류, 신발, 가방, 시계 등"),
  BEAUTY("뷰티/미용", "화장품, 향수, 미용기기 등"),
  SPORTS("스포츠/레저", "운동기구, 자전거, 캠핑용품, 골프용품 등"),
  HOBBY("취미/게임/음반", "게임기, 게임타이틀, 악기, 음반, 피규어 등"),
  BOOK("도서", "소설, 자기계발, 전공서적, 만화책 등"),
  PET("반려동물용품", "사료, 간식, 장난감, 용품 등"),
  FOOD("생활/가공식품", "건강식품, 가공식품 등"),
  PLANT("식물", "화분, 다육이, 분재 등"),
  TICKET("티켓/교환권/쿠폰", "공연티켓, 상품권, 쿠폰 등"),
  HOMEWARE("생활가전", "청소기, 공기청정기, 세탁기, 냉장고 등"),
  OTHER("기타", "기타 중고물품");

  companion object {
    fun fromDisplayName(name: String): ProductCategory? {
      return entries.find { it.displayName == name }
    }
  }
}