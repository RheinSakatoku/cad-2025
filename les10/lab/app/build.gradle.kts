plugins {
    id("java")
    id("war")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("com.h2database:h2:2.2.224")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")


}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources") // <--- Важно!
        }
    }
}


testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.12.1")
        }
    }
}

tasks.named<Jar>("jar") {
    enabled = true
}

tasks.withType<War> {
    archiveFileName.set("app.war")
}

tasks.processResources {

    from("src/main/resources") {
        include("**/persistence.xml")
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
