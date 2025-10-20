package quickswap.productservice.adapter.outbox

import org.springframework.data.jpa.repository.JpaRepository
import quickswap.productservice.domain.outbox.OutboxEvent

interface OutboxRepository: JpaRepository<OutboxEvent, String> {
}