plugins {
    id("buildsrc.convention.spring-boot-library")
}

dependencies {
    api(project(":core:core-common"))
    api("org.springframework.boot:spring-boot-starter-webflux")
}
