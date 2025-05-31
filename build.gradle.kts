plugins {
	java
	id("org.springframework.boot") version ("3.4.5")
	id("io.spring.dependency-management") version ("1.1.7")
	id("com.diffplug.spotless") version ("7.0.4")
}

group = "com.songxue77"
version = "0.0.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven("https://repo.spring.io/milestone")
	maven("https://repo.spring.io/snapshot")
	maven("https://packages.confluent.io/maven")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

spotless {
	val excludeFiles = arrayOf("./idea/**/*.*", ".vscode/**/*.*")

	java {
		targetExclude("build/**/*.java")
		removeUnusedImports()
		replaceRegex("Remove wildcard imports", "import\\s+[^\\*\\s]+\\*;(\\r\\n|\\r|\\n)", "$1")
		replaceRegex(
			"Replace lombok.NonNull with spring NonNull",
			"import lombok.NonNull",
			"import org.springframework.lang.NonNull;"
		)
		googleJavaFormat()
		importOrder(
			"java",
			"jakarta",
			"javax",
			"lombok",
			"org.springframework",
			"",
			"org.junit",
			"com.songxue77",
			"\\#",
			"\\#org.junit",
			"\\#com.songxue77"
		)
		trimTrailingWhitespace()
		endWithNewline()
	}
	format("yaml") {
		target("**/*.yaml", "**/*.yml")
		targetExclude(*excludeFiles)
		prettier().configFile("$rootDir/.prettierrc")
	}
	format("xml") {
		target("**/*.xml")
		targetExclude(*excludeFiles)
		prettier().config(mapOf("parser" to "html", "printWidth" to 160)).configFile("$rootDir/.prettierrc")
	}
	format("md") {
		target("**/*.md")
		targetExclude(*excludeFiles)
		prettier().config(mapOf("parser" to "markdown", "printWidth" to 160)).configFile("$rootDir/.prettierrc")
	}
}

tasks.test {
	useJUnitPlatform()
}
