package quickswap.productservice.adapter.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import quickswap.commons.adapter.shared.kafka.AbstractKafkaConfig

@Primary
@Configuration
open class ProductKafkaConfig : AbstractKafkaConfig() {

  @Value("\${kafka.bootstrap-servers}")
  private lateinit var bootstrapServers: String

  @Value("\${spring.application.name}")
  private lateinit var applicationName: String

  override fun getBootstrapServers(): String = bootstrapServers

  override fun getGroupId(): String = applicationName
}