package quickswap.productservice.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import quickswap.productservice.adapter.webapi.dto.ProductCreateResponse
import quickswap.productservice.application.dto.ProductsFindResponse
import quickswap.productservice.application.`in`.ProductCreator
import quickswap.productservice.application.`in`.ProductFinder
import quickswap.productservice.domain.product.ProductCategory
import quickswap.productservice.domain.product.ProductCreateRequest

@RequestMapping("/api/v1")
@RestController
class ProductController(
  private val productCreator: ProductCreator,
  private val productFinder: ProductFinder,
) {

  @PostMapping("/product")
  fun create(@RequestBody request: ProductCreateRequest): ResponseEntity<ProductCreateResponse> {
    val product = productCreator.createProduct(request)
    return ResponseEntity.ok(ProductCreateResponse(product.id, product.title))
  }

  @GetMapping("/public/product")
  fun getProductOnScroll(
    @RequestParam(required = false) cursorTime: Long?,
    @RequestParam(defaultValue = "20") size: Int,
    @RequestParam(required = false) category: ProductCategory?
  ): ResponseEntity<ProductsFindResponse> {
    return ResponseEntity.ok(productFinder.findProducts(cursorTime, size,category))
  }

}