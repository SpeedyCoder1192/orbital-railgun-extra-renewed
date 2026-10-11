pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		mavenCentral()
		gradlePluginPortal()
	}
	plugins {
		id("me.modmuss50.mod-publish-plugin") version "2.2.1"
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "orbital-railgun"
