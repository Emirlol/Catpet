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


/*
/execute at @p run summon minecraft:cat ~1 ~ ~ {NoAI:1b,PersistenceRequired:1b,variant:}
/execute at @p run summon minecraft:cat ~2 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:1b}
/execute at @p run summon minecraft:cat ~3 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:2b}
/execute at @p run summon minecraft:cat ~4 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:3b}
/execute at @p run summon minecraft:cat ~5 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:4b}
/execute at @p run summon minecraft:cat ~6 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:5b}
/execute at @p run summon minecraft:cat ~7 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:6b}
/execute at @p run summon minecraft:cat ~8 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:7b}
/execute at @p run summon minecraft:cat ~9 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:8b}
/execute at @p run summon minecraft:cat ~10 ~ ~ {NoAI:1b,PersistenceRequired:1b,CatType:9b}
 */
