plugins {
    id("buildsrc.convention.spring-boot-library")
}

dependencies {
    api(project(":core:core-common"))
    api(libs.springKafka)
    api(libs.kafkaClients)
}
