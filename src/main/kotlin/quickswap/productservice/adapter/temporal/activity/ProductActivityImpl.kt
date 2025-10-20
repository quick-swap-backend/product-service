package quickswap.productservice.adapter.temporal.activity

import io.temporal.spring.boot.ActivityImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import quickswap.commons.domain.shared.LogActions
import quickswap.commons.domain.shared.LogHelper.format
import quickswap.commons.domain.shared.id.ProductId
import quickswap.productservice.application.`in`.ProductUpdater

@Component
@ActivityImpl(taskQueues = ["trade-task-queue"])
class ProductActivityImpl(
  private val productUpdater: ProductUpdater
) : ProductActivity {

  private val logger = LoggerFactory.getLogger(javaClass)

  override fun cancelProductByTrade(productId: ProductId) {
    logger.info(format(LogActions.TRY, "funCall", "cancelByTrade", "productId" to productId.value))

    try {
      productUpdater.cancelByTrade(productId)
      logger.info(format(LogActions.OK, "funCall", "cancelByTrade", "productId" to productId.value))
    } catch(e: Exception) {
      logger.error(format(LogActions.FAIL, "funCall", "cancelByTrade", "productId" to productId.value), e)
      throw e
    }
  }

  override fun restoreProductToReserved(productId: ProductId) {
    logger.info(format(LogActions.TRY, "funCall", "restoreProductToReserved", "productId" to productId.value))
    try {
      productUpdater.restoreToReserved(productId)
      logger.info(format(LogActions.OK, "funCall", "restoreToReserved", "productId" to productId.value))
    } catch(e: Exception) {
      logger.error(format(LogActions.FAIL, "funCall", "restoreToReserved", "productId" to productId.value), e)
    }
  }

}