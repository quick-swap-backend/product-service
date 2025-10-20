package quickswap.productservice.adapter.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import quickswap.commons.adapter.shared.kafka.AbstractKafkaConfig

@Configuration
open class ProductKafkaConfig(
  @Value("\${kafka.bootstrap-servers}")
  private val bootstrapServers: String,

  @Value("\${spring.application.name}")
  private val applicationName: String
) : AbstractKafkaConfig() {

  override fun getBootstrapServers(): String = bootstrapServers

  override fun getGroupId(): String = applicationName
}