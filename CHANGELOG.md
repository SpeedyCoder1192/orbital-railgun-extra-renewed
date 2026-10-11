First release, for 26.3.

- Ported to Fabric from Orbital Railgun: Renewed (NeoForge 26.1)
- No GeckoLib needed anymore, the gun is a normal item model now
- Shaders updated for the new 26.3 renderer, works on both OpenGL and Vulkan
- The server now checks you're actually holding a railgun that's off cooldown before it starts a strike
- Leaving a world mid strike no longer lets the strike go off in the next world you open
- If you hid the HUD with F1 before scoping in, it stays hidden afterwards
