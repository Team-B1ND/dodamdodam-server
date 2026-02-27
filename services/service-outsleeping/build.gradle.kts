plugins {
    id("buildsrc.convention.spring-boot-application")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))
    implementation(project(":core:core-jpa"))

    // database
    runtimeOnly(libs.mysql.jdbcDriver)
    implementation(libs.springBootStarterData.jdbc)
    implementation(libs.springBootStarterData.jpa)

    // flyway
    implementation(libs.flywayCore)
    implementation(libs.flywayMysql)

    // grpc client
    implementation(project(":core:core-grpc"))
    implementation(libs.springGrpc)

    // security
    implementation(libs.springBootStarterSecurity)

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:${libs.versions.querydsl.get()}:jakarta")
    kapt("com.querydsl:querydsl-apt:${libs.versions.querydsl.get()}:jakarta")
}
