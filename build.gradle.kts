plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  id("org.springframework.boot") version "3.5.6"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("plugin.jpa") version "1.9.25"
}

group = "quickswap"
version = "0.0.1-SNAPSHOT"
description = "productservice"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
  maven { url = uri("https://jitpack.io") }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.kafka:spring-kafka")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.temporal:temporal-sdk:1.31.0")
  implementation ("io.temporal:temporal-spring-boot-starter:1.31.0")

  runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
  developmentOnly("org.springframework.boot:spring-boot-docker-compose")

  testImplementation("org.springframework.kafka:spring-kafka-test")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.springframework.security:spring-security-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("com.h2database:h2")
  testImplementation("io.mockk:mockk:1.14.6")

  implementation("com.github.quick-swap-backend:commons:v0.0.13")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
  useJUnitPlatform()
}