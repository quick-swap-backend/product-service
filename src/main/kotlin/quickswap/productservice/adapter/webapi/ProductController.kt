package quickswap.productservice.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import quickswap.productservice.adapter.webapi.dto.ProductCreateResponse
import quickswap.productservice.application.`in`.ProductCreator
import quickswap.productservice.domain.product.ProductCreateRequest

@RequestMapping("/api/v1")
@RestController
class ProductController(
  private val productCreator: ProductCreator
) {

  @PostMapping("/product")
  fun create(@RequestBody request: ProductCreateRequest): ResponseEntity<ProductCreateResponse> {
    val product = productCreator.createProduct(request)
    return ResponseEntity.ok(ProductCreateResponse(product.id, product.title))
  }

}