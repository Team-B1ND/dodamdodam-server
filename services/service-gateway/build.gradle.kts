plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-kafka"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // jwt
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)
    implementation(libs.mysql.jdbcDriver)

    // r2dbc
    implementation(libs.mysql.r2dbcDriver)
    implementation(libs.springBootStarterData.r2dbc)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}
