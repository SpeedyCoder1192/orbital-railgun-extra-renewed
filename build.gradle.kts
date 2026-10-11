plugins {
	id("net.fabricmc.fabric-loom") version "1.18.2"
	id("me.modmuss50.mod-publish-plugin")
}

version = "${property("mod_version")}+${property("minecraft_version")}"
group = property("maven_group") as String
base.archivesName = property("archives_base_name") as String

loom {
	splitEnvironmentSourceSets()

	mods {
		register("orbital_railgun") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets["client"])
		}
	}
}

repositories {
	maven("https://api.modrinth.com/maven") {
		name = "Modrinth"
		content { includeGroup("maven.modrinth") }
	}
}

fabricApi {
	configureTests {
		createSourceSet = true
		modId = "orbital_railgun-gametest"
		enableGameTests = false
		enableClientGameTests = true
		eula = true
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
	implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

	// dev only, not a dependency of the mod
	localRuntime("maven.modrinth:modmenu:${property("modmenu_version")}")
	localRuntime("maven.modrinth:placeholder-api:${property("placeholder_api_version")}")
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.processResources {
	inputs.property("version", project.version)
	filesMatching("fabric.mod.json") {
		expand("version" to project.version)
	}
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_orbital_railgun" }
	}
}

publishMods {
	val modrinthId = property("publish.modrinth_id") as String
	val mc = property("minecraft_version") as String
	file = tasks.jar.flatMap { it.archiveFile }
	displayName = "Orbital Railgun: Extra Renewed ${property("mod_version")} ($mc)"
	version = project.version.toString()
	changelog = providers.gradleProperty("changelog").orElse(rootProject.file("CHANGELOG.md").readText())
	type = if (findProperty("publish.type") == "beta") BETA else STABLE
	modLoaders.add("fabric")
	// missing a token or project id -> only prints what it would upload
	dryRun = modrinthId.isEmpty() ||
		(!hasProperty("modrinth.token") && System.getenv("MODRINTH_TOKEN") == null) ||
		System.getenv("GITHUB_TOKEN") == null

	modrinth {
		accessToken = providers.gradleProperty("modrinth.token").orElse(providers.environmentVariable("MODRINTH_TOKEN"))
		projectId = modrinthId.ifEmpty { "dry-run" }
		minecraftVersions.add(mc)
		requires("fabric-api")
	}

	github {
		accessToken = providers.environmentVariable("GITHUB_TOKEN")
		repository = "SpeedyCoder1192/orbital-railgun-extra-renewed"
		commitish = "main"
		tagName = project.version.toString()
	}
}
