import net.minecraftforge.gradle.common.util.RunConfig

plugins {
    id("net.minecraftforge.gradle") version "[6.0, 6.2)"
    id("org.spongepowered.mixin") version "0.7.+"
}

setupPlatform()

repositories {
    maven("https://maven.minecraftforge.net")
}

dependencies {
    minecraft("net.minecraftforge:forge:${rootProp["minecraft"]}-${rootProp["forge"]}")

    implementation("org.jetbrains:annotations:19.0.0")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

minecraft {
    mappings("official", rootProp["minecraft"])

    runs {
        val runConfig = Action<RunConfig> {
            workingDirectory(rootProject.file("run"))
            ideaModule("${rootProject.name}.${project.name}.testmod")

            mods {
                create("badpackets") {
                    source(sourceSets["main"])
                    source(rootProject.sourceSets["main"])
                }

                create("badpackets_test") {
                    source(sourceSets["testmod"])
                    source(rootProject.sourceSets["testmod"])
                }
            }
        }
        create("client", runConfig)
        create("server", runConfig)
    }
}

mixin {
    add(sourceSets["main"], "badpackets.refmap.json")
    config("badpackets.mixins.json")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }
}

tasks.jar {
    finalizedBy("reobfJar")
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

sourceSets.forEach {
    val dir = layout.buildDirectory.dir("sourcesSets/${it.name}")
    it.output.setResourcesDir(dir)
    it.java.destinationDirectory.set(dir)
}
