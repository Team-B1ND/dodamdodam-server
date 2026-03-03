plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-jpa"))

    // database
    runtimeOnly(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jpa)
    implementation(libs.springdoc.openapi.webmvc.ui)

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)

    // external api
    implementation("org.jsoup:jsoup:1.18.3")
}
