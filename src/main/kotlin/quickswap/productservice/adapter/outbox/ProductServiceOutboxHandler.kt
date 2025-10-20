package quickswap.productservice.adapter.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import quickswap.commons.adapter.shared.kafka.KafkaTopics
import quickswap.productservice.adapter.outbox.OutboxRepository
import quickswap.productservice.application.out.OutboxHandler
import quickswap.productservice.domain.outbox.OutboxEvent
import quickswap.productservice.domain.product.Product

@Transactional
@Component
class ProductServiceOutboxHandler(
  private val repository: OutboxRepository,
  private val objectMapper: ObjectMapper
) : OutboxHandler {

  override fun execute(product: Product): String {
    val eventPayload = mapOf(
      "id" to product.id.value,
      "title" to product.title,
      "price" to product.price.amount,
      "sellerId" to product.seller.id.value,
      "sellerEmail" to product.seller.email.value,
      "category" to product.category.name,
    )

    val outboxEvent = OutboxEvent.of(
      aggregateType = "Product",
      aggregateId = product.id.value,
      eventType = KafkaTopics.PRODUCT_CREATED,
      payload = objectMapper.writeValueAsString(eventPayload)
    )
    val saved = repository.save(outboxEvent)

    return saved.id
  }

}