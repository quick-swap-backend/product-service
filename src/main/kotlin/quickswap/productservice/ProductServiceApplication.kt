package quickswap.productservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.kafka.annotation.EnableKafka
import quickswap.productservice.adapter.kafka.ProductKafkaConfig

@EnableKafka
@SpringBootApplication
class ProductServiceApplication

fun main(args: Array<String>) {
  runApplication<ProductServiceApplication>(*args)
}
