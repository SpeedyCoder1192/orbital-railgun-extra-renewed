package io.github.mishkis.orbital_railgun.client.rendering;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mishkis.orbital_railgun.client.mixin.PostChainAccessor;
import io.github.mishkis.orbital_railgun.client.mixin.PostPassAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.util.Map;

// The post chains are requested through GameRenderer's post effect list (see GameRendererMixin),
// this only keeps the RailgunConfig uniform block up to date.
public abstract class AbstractOrbitalRailgunShader {
    private static final String UNIFORM_BLOCK = "RailgunConfig";
    // Std140Builder pads a vec3 out to 16 bytes while GLSL packs a following scalar into it,
    // so the scalars go before the vectors to keep both layouts the same
    private static final int UNIFORM_BLOCK_SIZE = new Std140SizeCalculator()
            .putMat4f()
            .putFloat()
            .putFloat()
            .putVec3()
            .putVec3()
            .get();
    private static final Vector3f ZERO = new Vector3f();

    protected final Minecraft client = Minecraft.getInstance();

    protected int ticks = 0;

    public abstract Identifier getIdentifier();

    public abstract boolean shouldRender();

    protected void tickExtra() {}

    protected void prepareExtraUniforms(float partialTick) {}

    protected Vector3f getBlockPositionUniform() {
        return ZERO;
    }

    protected float getIsBlockHitUniform() {
        return 0f;
    }

    public final void tick() {
        tickExtra();

        if (shouldRender()) {
            ticks++;
        } else {
            ticks = 0;
        }
    }

    public final void updateUniforms(PostChain chain, Matrix4f projection, CameraRenderState camera, float partialTick) {
        prepareExtraUniforms(partialTick);

        Matrix4f inverseTransformMatrix = new Matrix4f(projection).mul(camera.viewRotationMatrix).invert();
        Vector3f cameraPosition = camera.pos.toVector3f();
        Vector3f blockPosition = getBlockPositionUniform();
        float time = (ticks + partialTick) / 20f;
        float isBlockHit = getIsBlockHitUniform();

        for (PostPass pass : ((PostChainAccessor) chain).orbital_railgun$getPasses()) {
            Map<String, GpuBuffer> customUniforms = ((PostPassAccessor) pass).orbital_railgun$getCustomUniforms();
            GpuBuffer previous = customUniforms.get(UNIFORM_BLOCK);
            if (previous == null) {
                // the final blit pass has no RailgunConfig block
                continue;
            }

            try (MemoryStack memoryStack = MemoryStack.stackPush()) {
                Std140Builder builder = Std140Builder.onStack(memoryStack, UNIFORM_BLOCK_SIZE);
                builder.putMat4f(inverseTransformMatrix);
                builder.putFloat(time);
                builder.putFloat(isBlockHit);
                builder.putVec3(cameraPosition);
                builder.putVec3(blockPosition);

                customUniforms.put(UNIFORM_BLOCK, RenderSystem.getDevice().createBuffer(() -> "orbital_railgun " + UNIFORM_BLOCK, GpuBuffer.USAGE_UNIFORM, builder.get()));
            }

            previous.close();
        }
    }
}
