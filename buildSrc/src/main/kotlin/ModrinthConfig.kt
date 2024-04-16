import me.modmuss50.mpp.ModPublishExtension
import me.modmuss50.mpp.ReleaseType
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure

fun <T : Jar> UploadConfig.modrinth(task: T) = project.run {
    apply(plugin = "me.modmuss50.mod-publish-plugin")

    configure<ModPublishExtension> {
        modrinth {
            accessToken = env["MODRINTH_TOKEN"]
            dryRun = env["MODRINTH_TOKEN"] == null

            projectId = rootProp["mr.projectId"]
            file = task.archiveFile
            displayName = "${project.version}"
            version = "${project.name}-${project.version}"
            type = ReleaseType.of(prop["mr.releaseType"])
            changelog = "https://github.com/badasintended/badpackets/releases/tag/${project.version}"

            modLoaders.addAll(prop["mr.loader"].split(", "))
            minecraftVersions.addAll(prop["mr.gameVersion"].split(", "))
        }
    }
}
