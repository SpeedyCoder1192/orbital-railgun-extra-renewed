package io.github.mishkis.orbital_railgun.client.rendering;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class OrbitalRailgunShader extends AbstractOrbitalRailgunShader {
    public static final Identifier ORBITAL_RAILGUN_SHADER = Identifier.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "orbital_railgun");
    public static final OrbitalRailgunShader INSTANCE = new OrbitalRailgunShader();

    public Vector3f BlockPosition = null;
    public ResourceKey<Level> Dimension = null;

    @Override
    public Identifier getIdentifier() {
        return ORBITAL_RAILGUN_SHADER;
    }

    @Override
    public boolean shouldRender() {
        return BlockPosition != null && client.level != null && client.level.dimension() == Dimension;
    }

    @Override
    protected void tickExtra() {
        // the pillar glow is gone ~25s after the explosion (~61s total)
        if (ticks >= 1300 || client.level == null || client.level.dimension() != Dimension) {
            BlockPosition = null;
            Dimension = null;
        }
    }

    @Override
    protected Vector3f getBlockPositionUniform() {
        return BlockPosition;
    }
}
