rootProject.name = "badpackets"

fun platform(name: String) {
    include(name)
    project(":${name}").projectDir = file("platform/${name}")
}

platform("fabric")
platform("forge")
platform("mojmap")
