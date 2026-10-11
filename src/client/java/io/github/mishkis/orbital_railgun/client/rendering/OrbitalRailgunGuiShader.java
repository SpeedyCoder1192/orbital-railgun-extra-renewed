package io.github.mishkis.orbital_railgun.client.rendering;

import io.github.mishkis.orbital_railgun.OrbitalRailgun;
import io.github.mishkis.orbital_railgun.OrbitalRailgunSounds;
import io.github.mishkis.orbital_railgun.item.OrbitalRailgunItem;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector3f;

public class OrbitalRailgunGuiShader extends AbstractOrbitalRailgunShader {
    public static final Identifier ORBITAL_RAILGUN_GUI_SHADER = Identifier.fromNamespaceAndPath(OrbitalRailgun.MOD_ID, "orbital_railgun_gui");
    public static final OrbitalRailgunGuiShader INSTANCE = new OrbitalRailgunGuiShader();

    public HitResult hitResult;

    private float isBlockHit = 0f;
    private Vector3f hitPosition = new Vector3f();
    private boolean hidHud = false;

    @Override
    public Identifier getIdentifier() {
        return ORBITAL_RAILGUN_GUI_SHADER;
    }

    @Override
    public boolean shouldRender() {
        return client.player != null && client.player.getUseItem().getItem() instanceof OrbitalRailgunItem;
    }

    @Override
    protected void tickExtra() {
        // the scope replaces the hud, put it back afterwards unless F1 was used in the meantime
        if (shouldRender()) {
            if (ticks == 0) {
                client.getSoundManager().play(SimpleSoundInstance.forUI(OrbitalRailgunSounds.SCOPE_ON, 1.0f));
                if (!client.gui.hud.isHidden()) {
                    client.gui.hud.toggle();
                    hidHud = true;
                }
            }
        } else if (hidHud) {
            if (client.gui.hud.isHidden()) {
                client.gui.hud.toggle();
            }
            hidHud = false;
        }
    }

    @Override
    protected void prepareExtraUniforms(float partialTick) {
        hitResult = client.player.pick(300f, partialTick, false);
        switch (hitResult.getType()) {
            case BLOCK:
                isBlockHit = 1f;
                hitPosition = Vec3.atCenterOf(((BlockHitResult) hitResult).getBlockPos()).toVector3f();
                break;
            case ENTITY:
                isBlockHit = 1f;
                hitPosition = Vec3.atCenterOf(((EntityHitResult) hitResult).getEntity().blockPosition()).toVector3f();
                break;
            case MISS:
                isBlockHit = 0f;
                break;
        }
    }

    @Override
    protected Vector3f getBlockPositionUniform() {
        return hitPosition;
    }

    @Override
    protected float getIsBlockHitUniform() {
        return isBlockHit;
    }
}
