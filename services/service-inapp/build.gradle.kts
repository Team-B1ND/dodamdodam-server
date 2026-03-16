plugins {
    id("buildsrc.convention.spring-boot-application")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-jpa"))
    implementation(project(":core:core-kafka"))
    implementation(project(":core:core-grpc"))
    implementation(libs.springGrpc)

    // database
    runtimeOnly(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterData.jpa)

    // querydsl
    implementation(libs.querydsl.jpa) { artifact { classifier = "jakarta" } }
    kapt(libs.querydsl.apt) { artifact { classifier = "jakarta" } }

    // swagger
    implementation(libs.springdoc.openapi.webmvc.ui)

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)
}
