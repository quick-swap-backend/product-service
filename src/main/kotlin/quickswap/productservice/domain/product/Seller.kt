package quickswap.productservice.domain.product

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable
import quickswap.commons.adapter.shared.persistence.converter.EmailConverter
import quickswap.commons.domain.shared.id.UserId
import quickswap.commons.domain.shared.vo.Email

@Embeddable
class Seller(

  @AttributeOverride(name = "value", column = Column(name = "seller_id", nullable = false))
  val id: UserId,

  @Column(name = "seller_email", nullable = false)
  @Convert(converter = EmailConverter::class)
  val email: Email,
)
