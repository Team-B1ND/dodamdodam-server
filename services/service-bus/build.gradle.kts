plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-jpa"))
    implementation(project(":core:core-grpc"))

    // database
    runtimeOnly(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterData.jpa)
    implementation(libs.springBootStarterSecurity)

    // grpc
    implementation(libs.springGrpc)

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)
}
