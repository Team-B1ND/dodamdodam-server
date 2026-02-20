plugins {
    id("buildsrc.convention.spring-boot-application")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-jpa"))
    implementation(project(":core:core-grpc"))

    runtimeOnly(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterData.jpa)
    implementation(libs.springBootStarterSecurity)

    implementation(libs.springGrpc)

    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)

    implementation(libs.querydslJpa) { artifact { classifier = "jakarta" } }
    kapt(libs.querydslApt) { artifact { classifier = "jakarta" } }
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")
}
