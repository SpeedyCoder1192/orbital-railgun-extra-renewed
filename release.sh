#!/usr/bin/env bash
# Releases the current version to Modrinth and/or GitHub.
# Needs: gh logged in, and modrinth.token in ~/.gradle/gradle.properties (or MODRINTH_TOKEN set).
set -euo pipefail
cd "$(dirname "$0")"

repo="SpeedyCoder1192/orbital-railgun-extra-renewed"

# Gradle needs Java 25; use the one in ~/.jdks if nothing else is set up
if [ -z "${JAVA_HOME:-}" ] && ! command -v java >/dev/null; then
	JAVA_HOME=$(ls -d ~/.jdks/jdk-25* 2>/dev/null | tail -1)
	[ -n "$JAVA_HOME" ] || { echo "No Java found. Install JDK 25 or set JAVA_HOME."; exit 1; }
	export JAVA_HOME
fi

command -v gh >/dev/null || { echo "GitHub CLI (gh) isn't installed."; exit 1; }
GITHUB_TOKEN="$(gh auth token 2>/dev/null)" || { echo "gh isn't logged in, run: gh auth login"; exit 1; }
export GITHUB_TOKEN
if ! grep -q '^publish.modrinth_id=.' gradle.properties; then
	echo "Set publish.modrinth_id in gradle.properties first (Project page -> the three dots -> Copy ID)"
	exit 1
fi
if [ -z "${MODRINTH_TOKEN:-}" ] && ! grep -q '^modrinth.token=' ~/.gradle/gradle.properties 2>/dev/null; then
	echo "No Modrinth token. Add modrinth.token=... to ~/.gradle/gradle.properties"
	exit 1
fi

tag="$(grep '^mod_version=' gradle.properties | cut -d= -f2)+$(grep '^minecraft_version=' gradle.properties | cut -d= -f2)"
if gh release view "$tag" --repo "$repo" >/dev/null 2>&1; then
	echo "$tag is already released. To replace it, delete the old one first:"
	echo "  Modrinth: project page -> Versions -> $tag -> Delete"
	echo "  GitHub:   gh release delete '$tag' --repo $repo --cleanup-tag --yes"
	exit 1
fi

echo "Orbital Railgun: Extra Renewed $tag"
echo
echo "Upload to:"
echo "  1) Modrinth + GitHub"
echo "  2) Modrinth only"
echo "  3) GitHub only"
read -rp "> " where
case "$where" in
	1) task=publishMods; target="Modrinth + GitHub" ;;
	2) task=publishModrinth; target="Modrinth" ;;
	3) task=publishGithub; target="GitHub" ;;
	*) echo "Pick 1, 2 or 3."; exit 1 ;;
esac

echo
read -rp "Changelog (leave empty to use CHANGELOG.md): " changelog
echo "Changelog: ${changelog:-<CHANGELOG.md>}"
read -rp "Release $tag to $target? [y/N] " ok
[[ "$ok" =~ ^[yY]$ ]] || { echo "Cancelled."; exit 0; }

args=()
[ -n "$changelog" ] && args+=("-Pchangelog=$changelog")

# a tag left behind by a deleted release would point the new release at old code
if [ "$task" != publishModrinth ] && gh api "repos/$repo/git/refs/tags/$tag" >/dev/null 2>&1; then
	echo "removing leftover tag $tag"
	gh api -X DELETE "repos/$repo/git/refs/tags/$tag" >/dev/null
fi
./gradlew --console=plain -q "$task" "${args[@]}"
echo "done"
