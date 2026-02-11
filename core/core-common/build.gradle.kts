plugins {
    id("buildsrc.convention.spring-boot-library")
}

dependencies {
    api(libs.bundles.kotlinxEcosystem)
    api("org.springframework.boot:spring-boot-starter-web")
}

