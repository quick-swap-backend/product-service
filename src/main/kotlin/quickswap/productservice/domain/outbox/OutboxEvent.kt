package quickswap.productservice.domain.outbox

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "outbox_events")
class OutboxEvent(

  @Id
  val id: String,

  @Column(name = "aggregatetype")
  val aggregateType: String,

  @Column(name = "aggregateid")
  val aggregateId: String,

  @Column(name = "eventtype")
  val eventType: String,

  @Column(name = "payload", columnDefinition = "JSON")
  val payload: String,

  @Column(name = "createdat")
  val createdAt: LocalDateTime = LocalDateTime.now()
) {
  companion object {
    fun of(
      aggregateType: String,
      aggregateId: String,
      eventType: String,
      payload: String
    ): OutboxEvent {
      return OutboxEvent(
        id = UUID.randomUUID().toString(),
        aggregateType = aggregateType,
        aggregateId = aggregateId,
        eventType = eventType,
        payload = payload
      )
    }
  }
}