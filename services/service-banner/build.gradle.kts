plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-jpa"))

    implementation(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterSecurity)
    implementation(libs.springdoc.openapi.webmvc.ui)

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)
}
