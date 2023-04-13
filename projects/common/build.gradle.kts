plugins {
    base
    java
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()

    // You're company repo:

    //insecure protocol:
    //maven {
    //    url = uri("http://my.company.repo.com/nexus/content/groups/releases")
    //    isAllowInsecureProtocol = true;
    //}

    //secure protocol:
    //maven {
    //    url = uri("https://my.company.repo.com/nexus/content/groups/releases")
    //}
}

var springVersion = "6.0.7"
var slf4jVersion = "2.0.7"
var lombokVersion = "1.18.26"
var logbackVersion = "1.4.5"


java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}

dependencies{

    /* Spring */
    implementation("org.springframework:spring-core:${springVersion}")
    implementation("org.springframework:spring-context:${springVersion}")
    implementation("org.springframework:spring-beans:${springVersion}")
    implementation("org.springframework:spring-test:${springVersion}")
    implementation("org.springframework:spring-web:${springVersion}")

    /* SLF4J */
    implementation("org.slf4j:slf4j-api:${slf4jVersion}")

    /* Lombok */
    compileOnly("org.projectlombok:lombok:${lombokVersion}")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.14.2")

    implementation("io.rest-assured:rest-assured:5.3.0")

}

configurations {
    all {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
}

tasks {
    compileJava {
        options.encoding = "UFT-8"
    }
    compileTestJava {
        options.encoding = "UFT-8"
    }

    withType<JavaCompile> {
        sourceCompatibility = "${JavaVersion.VERSION_19}"
        targetCompatibility = "${JavaVersion.VERSION_19}"
    }

    withType<Test> {
        systemProperty("java.library.path", "%Path%;./src/main/resources/")

        isScanForTestClasses = false
        testLogging.showStandardStreams = true
    }

    reporting {
        baseDir = file("$buildDir/reports")
    }
}

