plugins {
	// 👇 Deprecated "StartParameter.isConfigurationCacheRequested" property comes from Kotlin's Gradle plugin
	//    https://youtrack.jetbrains.com/issue/KT-61457/
	//    Seems it's fixed in v2.0.0: https://github.com/JetBrains/kotlin/blob/v2.0.0/libraries/tools/kotlin-gradle-plugin/src/common/kotlin/org/jetbrains/kotlin/gradle/plugin/internal/ConfigurationCacheStartParameterAccessor.kt
	//    Spring Boot support for Kotlin 2 will come in v4 after Spring v7 release
	//    https://github.com/spring-projects/spring-framework/issues/33629
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
}

group = "com.davidlj95"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	// 👇 Solve warning about explicitly attaching a Java agent.
	// https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
	// https://github.com/spring-io/initializr/issues/1590
	jvmArgs("-javaagent:${mockitoAgent.asPath}")
}