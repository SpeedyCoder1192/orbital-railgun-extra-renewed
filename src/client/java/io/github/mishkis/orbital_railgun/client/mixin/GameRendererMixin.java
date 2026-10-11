package io.github.mishkis.orbital_railgun.client.mixin;

import io.github.mishkis.orbital_railgun.client.OrbitalRailgunClient;
import io.github.mishkis.orbital_railgun.client.rendering.AbstractOrbitalRailgunShader;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private List<Identifier> requestedPostEffects;
    @Shadow @Final private GameRenderState gameRenderState;

    @Unique
    private final Matrix4f orbital_railgun$projection = new Matrix4f();

    @Inject(method = "update", at = @At("TAIL"))
    private void orbital_railgun$requestPostEffects(DeltaTracker deltaTracker, CallbackInfo ci) {
        for (AbstractOrbitalRailgunShader shader : OrbitalRailgunClient.SHADERS) {
            if (shader.shouldRender()) {
                requestedPostEffects.add(shader.getIdentifier());
            }
        }
    }

    // the projection with view bobbing applied only exists as a local, grab it on its way to the GPU
    @ModifyArg(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;"))
    private Matrix4f orbital_railgun$captureProjection(Matrix4f projection) {
        orbital_railgun$projection.set(projection);
        return projection;
    }

    @Inject(method = "applyPostEffects", at = @At("HEAD"))
    private void orbital_railgun$updateUniforms(CallbackInfo ci) {
        LevelRenderState levelState = gameRenderState.levelRenderState;
        for (AbstractOrbitalRailgunShader shader : OrbitalRailgunClient.SHADERS) {
            if (!shader.shouldRender()) {
                continue;
            }

            PostChain chain = minecraft.getShaderManager().getPostChain(shader.getIdentifier(), LevelTargetBundle.MAIN_TARGETS);
            if (chain != null) {
                shader.updateUniforms(chain, orbital_railgun$projection, levelState.cameraRenderState, levelState.worldPartialTicks);
            }
        }
    }
}
