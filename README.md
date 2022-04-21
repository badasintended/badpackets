# Bad Packets
Bad Packets allows packet messaging between multiple modding platforms.

## Project Setup
```gradle
repositories {
    maven { url "https://maven.bai.lol" }
}

dependencies {
    // fabric, architectury common
    modImplementation "lol.bai:badpackets:fabric-${badpacketsVersion}"
    
    // forge
    implementation fg.deobf("lol.bai:badpackets:forge-${badpacketsVersion}")
    
    // sponge's vanillagradle
    implementation "lol.bai:badpackets:mojmap-${badpacketsVersion}"
}
```
