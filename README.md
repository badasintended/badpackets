# bad packets [![3][3]][5] <img src="src/main/resources/badpackets.png" align="right"/>
Bad packets allows packet messaging between multiple modding platforms.

This means a Fabric client version of a mod that uses Bad Packets can send a packet to a server that uses the Forge version and vice versa.

This mod **DOES NOT** magically make all mods that have versions for different platforms to be compatible with each other, the developer needs to use BP's API to send its packet. Even then there are more complicated things like game object id sync that this mod doesn't handle which makes it unrealistic to support in the first place.

## Project Setup
```gradle
repositories {
    maven {
        url "https://maven2.bai.lol"
        content {
            includeGroup "lol.bai"
        }
    }
}
```

### Fabric
```gradle
dependencies {
    modImplementation "lol.bai:badpackets:fabric-${badpackets_version}"
}
```

### Forge
```gradle
buildscript {
    dependencies {
        classpath "org.spongepowered:mixingradle:0.7.+"
    }
}

apply plugin: "org.spongepowered.mixin"

dependencies {
    implementation fg.deobf("lol.bai:badpackets:forge-${badpackets_version}")
}
```

### Architectury
#### Common
```gradle
dependencies {
    modCompileOnly "lol.bai:badpackets:fabric-${badpackets_version}"
}
```

#### Fabric
```gradle
dependencies {
    modRuntimeOnly "lol.bai:badpackets:fabric-${badpackets_version}"
}
```

#### Forge
```gradle
dependencies {
    modRuntimeOnly "lol.bai:badpackets:forge-${badpackets_version}"
}
```

### Sponge VanillaGradle based multiplatform
#### Common
```gradle
dependencies {
    compileOnly "lol.bai:badpackets:mojmap-${badpackets_version}"
}
```

#### Fabric
```gradle
dependencies {
    modRuntimeOnly "lol.bai:badpackets:fabric-${badpackets_version}"
}
```

#### Forge
```gradle
buildscript {
    dependencies {
        classpath "org.spongepowered:mixingradle:0.7.+"
    }
}

apply plugin: "org.spongepowered.mixin"

dependencies {
    runtimeOnly fg.deobf("lol.bai:badpackets:forge-${badpackets_version}")
}
```

[3]: https://img.shields.io/badge/code_quality-F-red
[5]: https://git.io/code-quality
