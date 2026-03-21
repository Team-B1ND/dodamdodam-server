plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-kafka"))
    implementation(project(":core:core-jpa"))
    implementation(project(":core:core-redis"))

    // database
    runtimeOnly(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterData.jpa)
    implementation(libs.springdoc.openapi.webmvc.ui)

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)

    // http client
    implementation("org.apache.httpcomponents.client5:httpclient5")

    // grpc
    implementation(project(":core:core-grpc"))
    implementation(libs.springGrpc)
}
