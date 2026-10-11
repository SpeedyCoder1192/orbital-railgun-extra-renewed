package io.github.mishkis.orbital_railgun.gametest;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.zombie.Husk;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class OrbitalRailgunClientTest implements FabricClientGameTest {
    private static final int MOUSE_LEFT = InputConstants.MOUSE_BUTTON_LEFT;
    private static final int MOUSE_RIGHT = InputConstants.MOUSE_BUTTON_RIGHT;
    private static final BlockPos TARGET = new BlockPos(0, -61, 40);

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            world.getServer().runCommand("gamerule advance_time false");
            world.getServer().runCommand("time set noon");
            world.getServer().runCommand("tp @a 0 -60 0");
            world.getServer().runCommand("fill -2 -60 38 2 -50 42 minecraft:stone");
            world.getServer().runCommand("summon minecraft:husk 5 -60 45 {NoAI:1b,PersistenceRequired:1b}");
            world.getServer().runCommand("give @a orbital_railgun:orbital_railgun");
            world.getConnection().waitForChunksRender();
            context.waitTicks(20);
            context.takeScreenshot("orbital-railgun-held");

            context.getInput().lookAt(TARGET.above(11));
            context.getInput().holdMouse(MOUSE_RIGHT);
            context.waitTicks(40);
            assertTrue(context.computeOnClient(client -> client.gui.hud.isHidden()), "hud should be hidden while scoped");
            context.takeScreenshot("orbital-railgun-scope");

            context.getInput().holdMouse(MOUSE_LEFT);
            context.waitTicks(2);
            context.getInput().releaseMouse(MOUSE_LEFT);
            context.getInput().releaseMouse(MOUSE_RIGHT);
            context.waitTicks(5);
            assertTrue(!context.computeOnClient(client -> client.gui.hud.isHidden()), "hud should come back after shooting");
            assertTrue(context.computeOnClient(client -> client.player.getCooldowns().isOnCooldown(client.player.getMainHandItem())), "railgun should be on cooldown");

            context.waitTicks(55);
            context.takeScreenshot("orbital-railgun-charge");
            context.waitTicks(340);
            context.takeScreenshot("orbital-railgun-beam");

            world.getServer().waitFor(server -> server.overworld().getBlockState(new BlockPos(0, -50, 40)).isAir(), 400);
            context.waitTicks(20);
            context.takeScreenshot("orbital-railgun-explosion");

            world.getServer().runOnServer(server -> {
                ServerLevel level = server.overworld();
                assertTrue(level.getBlockState(new BlockPos(0, level.getMinY(), 40)).isAir(), "strike should dig down to the bottom of the world");
                assertTrue(level.getBlockState(new BlockPos(0, -61, 70)).is(Blocks.GRASS_BLOCK), "blocks outside the radius should be untouched");
                assertTrue(level.getEntitiesOfClass(Husk.class, new AABB(TARGET).inflate(30)).isEmpty(), "husk in the strike should be dead");
                assertTrue(server.getPlayerList().getPlayers().getFirst().isAlive(), "player outside the radius should survive");
            });

            context.waitTicks(200);
            context.takeScreenshot("orbital-railgun-aftermath");
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
