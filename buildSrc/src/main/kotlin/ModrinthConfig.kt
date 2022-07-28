import com.modrinth.minotaur.ModrinthExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

fun <T : Jar> UploadConfig.modrinth(task: T) = project.run {
    apply(plugin = "com.modrinth.minotaur")

    env["MODRINTH_TOKEN"]?.let { MODRINTH_TOKEN ->
        configure<ModrinthExtension> {
            token.set(MODRINTH_TOKEN)
            projectId.set(rootProp["mr.projectId"])

            versionNumber.set("${project.name}-${project.version}")
            versionType.set(prop["mr.releaseType"])
            changelog.set("https://github.com/badasintended/badpackets/releases/tag/${project.version}")

            uploadFile.set(task)

            loaders.addAll(prop["mr.loader"].split(", "))
            gameVersions.addAll(prop["mr.gameVersion"].split(", "))
        }
    }
}
