plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-kafka"))
    implementation(project(":core:core-jpa"))

    implementation(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterSecurity)

    implementation(libs.jwt.nimbusJose)
    implementation(project(":core:core-grpc"))
    implementation(libs.springGrpc)

    implementation("com.github.ua-parser:uap-java:1.6.1")
    implementation(libs.springdoc.openapi.webmvc.ui)

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)
}
