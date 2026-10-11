# Orbital Railgun: Extra Renewed

Unofficial Fabric port of [Orbital Railgun](https://modrinth.com/mod/orbital-railgun) by Mishkis for Minecraft 26.3.

The port that I ported this port off of (sry _WHAT_) is [Orbital Railgun: Renewed](https://github.com/Rayness/orbital-railgun-renewed) by Rayness and the mod that he ported is [Orbital Railgun](https://github.com/Mishkis/orbital-railgun) by Mishkis.

Download it on [Modrinth](https://modrinth.com/mod/orbital-railgun-extra-renewed).

Hold right click to aim through the targeting screen, left click to fire. About 35 seconds later the strike wipes out a 24 block radius from the bottom of the world to the top and kills anything inside it. Two minute cooldown.

Needs Fabric Loader 0.19.5+ and Fabric API. Install it on both the client and the server.

## What's different from Renewed

This is based on the 26.1 branch of Renewed. Changes on top of that:

- Fabric instead of NeoForge
- GeckoLib is gone. The gun has no animations, so the model is now a normal item model (converted from the original Blockbench geo file, same look and same display transforms)
- Shaders updated for 26.3's new renderer (SPIR-V style GLSL and reversed depth). Tested on both the OpenGL and Vulkan backends
- The post effects go through vanilla's own post effect list instead of being drawn from a render event
- The server checks that you're actually holding a railgun that isn't on cooldown, and that the target is in range, before starting a strike. Before this any client could call one in anywhere
- Leaving a singleplayer world mid strike no longer lets the strike go off in the next world you open
- If you had the HUD hidden with F1 before scoping in, it stays hidden afterwards

The crafting recipe, timings, damage and blast radius are unchanged.

## Building

```
./gradlew build
```

The jar ends up in `build/libs/`. Gradle needs JDK 25.

On Linux with Wayland the game may fail to start OpenGL in the dev environment and fall back to Vulkan. Run with `JAVA_TOOL_OPTIONS=-Dorg.lwjgl.opengl.contextAPI=native` to get OpenGL.

`./gradlew runClientGameTest` opens the game, makes a flat world, fires the railgun at a stone pillar and checks that the crater, the kill and the cooldown all happen. Screenshots of each stage go to `build/run/clientGameTest/screenshots/`. See [TESTING.md](TESTING.md) for the manual checklist.

## Credits

- [Mishkis](https://github.com/Mishkis/orbital-railgun): made the original mod, the model, texture, sounds and shaders
- [Julian Speckmann (KingIronMan2011)](https://github.com/KingIronMan2011) and Jakob: fixes and cleanup in the original repo
- [Rayness](https://github.com/Rayness/orbital-railgun-renewed): the NeoForge port this is based on, including moving the shaders off Satin onto vanilla post chains, wiring the original sounds back in, and the shorter post-explosion fade
- [NightEpiphany](https://github.com/NightEpiphany/orbital-railgun-1.21): earlier Fabric 1.21 port
- [SpeedyCoder1192](https://github.com/SpeedyCoder1192): this Fabric port

The translations come from the original repo.

This is an unofficial port. Report bugs with it here, not to Mishkis.

## License

MIT, same as the original. See [LICENSE](LICENSE).
