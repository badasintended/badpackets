import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseExtension
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure

fun <T : Jar> UploadConfig.curseforge(task: T) = project.run {
    apply(plugin = "com.matthewprenger.cursegradle")

    env["CURSEFORGE_API"]?.let { CURSEFORGE_API ->
        configure<CurseExtension> {
            apiKey = CURSEFORGE_API
            project(closureOf<CurseProject> {
                id = rootProp["cf.projectId"]
                releaseType = prop["cf.releaseType"]

                changelogType = "markdown"
                changelog = "https://github.com/badasintended/badpackets/releases/tag/${project.version}"

                prop["cf.loader"].split(", ").forEach(this::addGameVersion)
                prop["cf.gameVersion"].split(", ").forEach(this::addGameVersion)

                mainArtifact(task, closureOf<CurseArtifact> {
                    displayName = "[${prop["cf.loader"].replace(", ", "/")}] ${project.version}"
                })

                if(listOf("cf.require", "cf.optional", "cf.break").any { prop.has(it) }) relations(closureOf<CurseRelation> {
                    prop.ifPresent("cf.require") {
                        it.split(", ").forEach(this::requiredDependency)
                    }
                    prop.ifPresent("cf.optional") {
                        it.split(", ").forEach(this::optionalDependency)
                    }
                    prop.ifPresent("cf.break") {
                        it.split(", ").forEach(this::incompatible)
                    }
                })

                afterEvaluate {
                    uploadTask.dependsOn("build")
                }
            })
        }
    }
}
