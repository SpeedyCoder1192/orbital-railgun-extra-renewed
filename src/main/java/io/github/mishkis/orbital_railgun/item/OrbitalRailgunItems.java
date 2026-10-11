package io.github.mishkis.orbital_railgun.item;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class OrbitalRailgunItems {
    private static final ResourceKey<Item> ORBITAL_RAILGUN_KEY = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "orbital_railgun"));

    public static final OrbitalRailgunItem ORBITAL_RAILGUN = Registry.register(BuiltInRegistries.ITEM, ORBITAL_RAILGUN_KEY,
            new OrbitalRailgunItem(new Item.Properties().setId(ORBITAL_RAILGUN_KEY)));

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> output.insertAfter(Items.CROSSBOW, ORBITAL_RAILGUN));
    }
}
