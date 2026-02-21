plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-kafka"))
    implementation(project(":core:core-jpa"))

    // database
    runtimeOnly(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterData.jpa)

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)

    // grpc
    implementation(project(":core:core-grpc"))
    implementation(libs.springGrpc)
}
