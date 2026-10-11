# Testing checklist

Run through this on 26.3 with only Fabric API and this mod installed unless a step says otherwise. Put an x in the box when it passes, and write down anything weird next to it.

`./gradlew runClientGameTest` already covers the basic shoot, crater, kill and cooldown run (marked *auto* below), but still check the visual parts by eye.

## Setup

- [ ] Game starts with the mod, no errors in the log mentioning `orbital_railgun`
- [ ] Dedicated server starts with the mod, and a client without the mod can't join (expected, it's needed on both sides)
- [ ] Mod shows up in Mod Menu (if installed) with the right name, icon, authors and description

## Item

- [ ] Shows up in the Combat creative tab right after the crossbow
- [ ] Shows up in creative search
- [ ] `/give @s orbital_railgun:orbital_railgun` works *(auto)*
- [ ] Name is purple (epic rarity) and reads "Orbital Railgun"
- [ ] Only stacks to 1
- [ ] Recipe works in a crafting table:
  ```
  glass pane      netherite ingot   emerald
  nether star     netherite block   netherite ingot
  lapis lazuli    redstone          (empty)
  ```
- [ ] Recipe shows up in the recipe book once unlocked

## Model

Compare against screenshots of the original if you can, it should look the same.

- [ ] First person, right hand *(auto, screenshot)*
- [ ] First person, left hand (swap with F)
- [ ] Third person, both hands
- [ ] Inventory and hotbar icon
- [ ] Dropped on the ground
- [ ] In an item frame
- [ ] On an armor stand / player head slot (`/item replace entity @s armor.head with orbital_railgun:orbital_railgun`)
- [ ] Break particles use the gun texture, not the missing texture

## Sounds

- [ ] Equip sound plays when switching to the railgun in the main hand
- [ ] Equip sound doesn't play again while you keep holding it
- [ ] "Scope online" sound plays when you start aiming
- [ ] Shot sound plays at the target, and a second player around 400 blocks away can still hear it
- [ ] Subtitles show for all three sounds with subtitles turned on

## Aiming

- [ ] Holding right click zooms in like a spyglass
- [ ] Mouse movement is smoothed while aiming, same as a spyglass
- [ ] HUD hides while aiming and comes back when you let go *(auto)*
- [ ] If the HUD was already hidden with F1 before aiming, it stays hidden after you let go
- [ ] Green frame, scanlines and the small flicker show up *(auto, screenshot)*
- [ ] The frame grows open when you start aiming instead of popping in
- [ ] The crosshair in the middle slowly rotates
- [ ] The block you're aiming at gets a red glow *(auto, screenshot)*
- [ ] The 24 block blue ring and cross show on the ground around the target and rotate slowly
- [ ] Aiming at a mob targets the block it's standing on
- [ ] Aiming at the sky shows no ring and left click does nothing
- [ ] Targets up to about 300 blocks away work
- [ ] Can't start aiming while the railgun is on cooldown

## Firing

- [ ] Left click while aiming fires and drops you out of the scope *(auto)*
- [ ] Cooldown overlay shows on the item and lasts 2 minutes *(auto for the cooldown starting)*
- [ ] Can't fire a second time while a strike is still going on, even with a second railgun
- [ ] Fire from the offhand works too

## Strike

Times are from the moment you fire.

- [ ] 0 to 4s: a ring sweeps out on the ground around the target and the screen fades to black *(auto, screenshot)*
- [ ] 4 to 36s: big blue sphere with six beams around it, the beams rise and come back in *(auto, screenshot)*
- [ ] From about 20s, mobs and players near the target get pulled toward it
- [ ] Players in spectator mode don't get pulled
- [ ] Items and other non-living entities get pulled too
- [ ] At about 35s everything within 24 blocks of the target dies *(auto for the mob)*
- [ ] Creative players inside the radius survive
- [ ] Death message reads "<name> was obliterated by an Orbital Strike"
- [ ] A round hole 49 blocks wide gets cleared from the bottom of the world to the build limit *(auto)*
- [ ] Blocks outside the radius are untouched *(auto)*
- [ ] White flash and shockwave after the explosion, then a blue pillar of light over the crater *(auto, screenshot)*
- [ ] Screen goes back to normal brightness after about 10s and the pillar fades out by about 60s
- [ ] Turning around during the strike: the effect stays locked to the target, not to the screen
- [ ] Strike works in the Nether and the End
- [ ] Bedrock, end portal frames and other unbreakable blocks inside the radius still get removed (same as the original)

## Multiplayer

Needs two clients on a dedicated server.

- [ ] Second player within 250 blocks of the target sees the whole strike effect
- [ ] Second player further than that doesn't see it and isn't pulled
- [ ] Player in another dimension sees nothing
- [ ] Changing dimension mid strike stops the effect on your screen
- [ ] Logging out mid strike on a server and back in doesn't crash, and the crater still happens
- [ ] Leaving a singleplayer world mid strike and opening a different world doesn't put a crater in the new one
- [ ] Two players firing at different spots at the same time both work
- [ ] Server ignores a shot packet when the player isn't holding a railgun or it's on cooldown (needs a hacked client or a debugger, skip if you can't)

## Graphics

- [ ] Vulkan backend: scope and strike look right *(auto)*
- [ ] OpenGL backend: scope and strike look right *(auto, run with `JAVA_TOOL_OPTIONS=-Dorg.lwjgl.opengl.contextAPI=native` if it falls back to Vulkan)*
- [ ] Resizing the window during a strike doesn't break the effect
- [ ] View bobbing on and off: the ring and red glow stay on the target while walking
- [ ] Different FOV settings: the effects still line up with the world
- [ ] Render distance 2 and 32 both work
- [ ] F5 third person while aiming and during a strike

## Other mods

- [ ] Sodium
- [ ] Iris with shaders off
- [ ] Iris with a shader pack on (the original didn't work with shader packs either, just note what happens)

## Languages

- [ ] Item name and death message are translated in a few of the 15 languages (de_de, ja_jp, ru_ru are easy to spot)
