package quickswap.productservice.adapter.kafka

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import quickswap.commons.adapter.shared.kafka.KafkaGroupIds.PRODUCT_UPDATER
import quickswap.commons.adapter.shared.kafka.KafkaTopics.TRADE_CANCELED
import quickswap.commons.domain.shared.id.ProductId
import quickswap.productservice.application.`in`.ProductUpdater

@Component
class TradeCancelEventListener(
  private val productUpdater: ProductUpdater
) {

  @KafkaListener(
    topics = [TRADE_CANCELED],
    groupId = PRODUCT_UPDATER,
    containerFactory = "kafkaListenerContainerFactory"
  )
  fun tradeCanceled(event: TradeCanceledEvent) {
    productUpdater.cancelByTrade(event.productId)
  }
}

data class TradeCanceledEvent(
  val productId: ProductId,
  val tradeId: String,
  val canceledAt: String? = null
)