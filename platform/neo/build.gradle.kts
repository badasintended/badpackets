import net.neoforged.gradle.dsl.common.runs.run.Run

plugins {
    idea
    id("net.neoforged.gradle.userdev") version "7.0.+"
}

setupPlatform()

dependencies {
    implementation("net.neoforged:neoforge:${rootProp["neo"]}")
}

runs {
    val runConfig = Action<Run> {
        modSource(sourceSets["main"])
        modSource(sourceSets["testmod"])
    }

    create("client", runConfig)
    create("server", runConfig)
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }
}

tasks.named<JavaCompile>("compileTestmodJava") {
    source(rootProject.sourceSets["testmod"].allSource)
}

afterEvaluate {
    val jar = tasks.jar.get()
    val sourcesJar = tasks.sourcesJar.get()

    upload {
        curseforge(jar)
        modrinth(jar)
        maven(jar, sourcesJar)
    }
}
