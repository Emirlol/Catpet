@file:Suppress("UnstableApiUsage")

plugins {
	alias(libs.plugins.loom)
}

repositories {
	mavenCentral()
}

val modName = property("mod_name") as String
val modId = property("mod_id") as String
val modVersion = property("mod_version") as String
val minecraftVersion = libs.versions.minecraft.get()
group = property("maven_group") as String
version = "$modVersion+$minecraftVersion"

dependencies {
	minecraft(libs.minecraft)
	mappings(variantOf(libs.yarn) { classifier("v2") })
	modImplementation(libs.fabricLoader)
}

tasks {
	processResources {
		val props = mapOf(
			"version" to version,
			"minecraft_version" to minecraftVersion,
			"loader_version" to libs.versions.fabricLoader.get(),
		)
		inputs.properties(props)
		filesMatching("fabric.mod.json") {
			expand(props)
		}
	}
	jar {
		from("LICENSE") {
			rename { "${it}_${base.archivesName.get()}" }
		}
	}
}
