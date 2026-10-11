package io.github.mishkis.orbital_railgun.client;

import io.github.mishkis.orbital_railgun.OrbitalRailgunSounds;
import io.github.mishkis.orbital_railgun.client.rendering.AbstractOrbitalRailgunShader;
import io.github.mishkis.orbital_railgun.client.rendering.OrbitalRailgunGuiShader;
import io.github.mishkis.orbital_railgun.client.rendering.OrbitalRailgunShader;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItem;
import io.github.mishkis.orbital_railgun.network.ClientSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;

import java.util.List;

public class OrbitalRailgunClient implements ClientModInitializer {
    public static final List<AbstractOrbitalRailgunShader> SHADERS = List.of(OrbitalRailgunShader.INSTANCE, OrbitalRailgunGuiShader.INSTANCE);

    private Item lastMainHandItem = null;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ClientSyncPayload.TYPE, (payload, context) -> {
            OrbitalRailgunShader.INSTANCE.BlockPosition = Vec3.atCenterOf(payload.pos()).toVector3f();
            OrbitalRailgunShader.INSTANCE.Dimension = context.player().level().dimension();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            SHADERS.forEach(AbstractOrbitalRailgunShader::tick);
            playEquipSound(client);
        });
    }

    private void playEquipSound(Minecraft client) {
        if (client.player == null) {
            lastMainHandItem = null;
            return;
        }

        Item current = client.player.getMainHandItem().getItem();
        if (current != lastMainHandItem) {
            if (current instanceof OrbitalRailgunItem) {
                client.player.playSound(OrbitalRailgunSounds.EQUIP, 1.0f, 1.0f);
            }
            lastMainHandItem = current;
        }
    }
}
