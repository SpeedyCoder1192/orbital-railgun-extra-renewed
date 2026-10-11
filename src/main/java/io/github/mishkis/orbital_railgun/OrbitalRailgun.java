package io.github.mishkis.orbital_railgun;

import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItems;
import io.github.mishkis.orbital_railgun.network.ClientSyncPayload;
import io.github.mishkis.orbital_railgun.network.ShootPayload;
import io.github.mishkis.orbital_railgun.util.OrbitalRailgunStrikeManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class OrbitalRailgun implements ModInitializer {
    public static final String MOD_ID = "orbital_railgun";

    @Override
    public void onInitialize() {
        OrbitalRailgunItems.initialize();
        OrbitalRailgunSounds.initialize();
        OrbitalRailgunStrikeManager.initialize();

        PayloadTypeRegistry.serverboundPlay().register(ShootPayload.TYPE, ShootPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ClientSyncPayload.TYPE, ClientSyncPayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ShootPayload.TYPE, (payload, context) -> OrbitalRailgunStrikeManager.shoot(context.player(), payload.pos()));

        ServerTickEvents.END_SERVER_TICK.register(OrbitalRailgunStrikeManager::tick);
        // otherwise a strike left over from one singleplayer world goes off in the next one
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> OrbitalRailgunStrikeManager.activeStrikes.clear());
    }
}
