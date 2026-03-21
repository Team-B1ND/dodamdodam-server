plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-jpa"))
    implementation(project(":core:core-grpc"))
    implementation(libs.springGrpc)

    runtimeOnly(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterData.jpa)
    implementation(libs.springdoc.openapi.webmvc.ui)
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)

    testRuntimeOnly("com.h2database:h2")
}
