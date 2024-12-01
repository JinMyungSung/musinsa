import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.github.node-gradle.node") version "7.1.0"
}

node {
    // 프론트엔트 프로젝트의 node, npm 버전과 같게 합니다.
    val frontendNodeVersion = "23.3.0"
    val frontendNpmVersion = "9.7.1"

    val userHome = System.getProperty("user.home")
    // node 바이너리가 저장될 위치를 지정합니다.
    val nodeDir = file("$userHome/.node/$frontendNodeVersion")
    // npm 바이너리 저장될 위치를 지정합니다.
    val npmDir = file("$userHome/.npm/$frontendNpmVersion")
    // 빌드할 프로젝트의 위치를 지정합니다.
    val projectDir = file("${project.projectDir}/src/main/resources/static")

    version.set(frontendNodeVersion)
    npmVersion.set(frontendNpmVersion)
    download.set(true)
    workDir.set(nodeDir)
    npmWorkDir.set(npmDir)
    nodeProjectDir.set(projectDir)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.kotest:kotest-runner-junit5:6.0.0.M1")
    testImplementation("io.kotest:kotest-assertions-core:6.0.0.M1")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // h2
    runtimeOnly("com.h2database:h2:2.3.232")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val frontendTask =
    tasks.register<NpmTask>("frontendTask") {
        args.set(listOf("run", "build"))
    }

tasks.withType<KotlinCompile> {
    dependsOn(frontendTask)
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = java.sourceCompatibility.toString()
    }
}
