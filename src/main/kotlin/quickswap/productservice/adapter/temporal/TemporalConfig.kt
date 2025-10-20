//package quickswap.productservice.adapter.temporal
//
//import io.temporal.client.WorkflowClient
//import io.temporal.serviceclient.WorkflowServiceStubs
//import io.temporal.worker.Worker
//import io.temporal.worker.WorkerFactory
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//import quickswap.productservice.adapter.temporal.activity.ProductActivityImpl
//
//@Configuration
//class TemporalConfig {
//
//  @Bean
//  @Primary
//  fun workflowServiceStubs(): WorkflowServiceStubs {
//    return WorkflowServiceStubs.newLocalServiceStubs()
//  }
//
//  @Bean
//  fun workflowClient(serviceStubs: WorkflowServiceStubs): WorkflowClient {
//    return WorkflowClient.newInstance(serviceStubs)
//  }
//
//  @Bean
//  fun workerFactory(
//    workflowClient: WorkflowClient,
//    productActivity: ProductActivityImpl
//  ): WorkerFactory {
//    val factory = WorkerFactory.newInstance(workflowClient)
//
//    val worker: Worker = factory.newWorker("trade-task-queue")
//    worker.registerActivitiesImplementations(productActivity)
//
//    factory.start()
//    return factory
//  }
//}