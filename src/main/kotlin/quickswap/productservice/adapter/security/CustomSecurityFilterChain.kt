package quickswap.productservice.adapter.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import quickswap.commons.adapter.shared.security.JwtAuthenticationFilter

@EnableWebSecurity
@Configuration
class CustomSecurityFilterChain(
  private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf { it.disable() }
      .formLogin { it.disable() }
      .httpBasic { it.disable() }
      .authorizeHttpRequests { auth ->
        auth
          .requestMatchers("/api/v1/public/**").permitAll()
          .anyRequest().authenticated()
      }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

    return http.build()
  }
}