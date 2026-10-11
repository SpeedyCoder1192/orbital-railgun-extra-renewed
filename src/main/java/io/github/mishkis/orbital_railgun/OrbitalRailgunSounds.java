package io.github.mishkis.orbital_railgun;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class OrbitalRailgunSounds {
    public static final SoundEvent EQUIP = register(SoundEvent.createVariableRangeEvent(id("equip")));
    public static final SoundEvent SCOPE_ON = register(SoundEvent.createVariableRangeEvent(id("scope_on")));
    // fixed range so everyone near the strike hears it, not just players within 16 blocks
    public static final SoundEvent RAILGUN_SHOOT = register(SoundEvent.createFixedRangeEvent(id("railgun_shoot"), 500.0f));

    private static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, name);
    }

    private static SoundEvent register(SoundEvent sound) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, sound.location(), sound);
    }

    public static void initialize() {}
}
