package dev.domkss;

import dev.domkss.blocks.fluids.ModFluids;
import dev.domkss.screen.SkillScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class UnderGroundClient implements ClientModInitializer {
    private static KeyBinding skillKey;

    @Override
    public void onInitializeClient() {
        skillKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.underground.openskill",
                GLFW.GLFW_KEY_K,
                "category.underground"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (skillKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new SkillScreen());
            }
        });

        FluidRenderHandlerRegistry.INSTANCE.register(
                ModFluids.STILL_RADIOACTIVE_WATER,
                ModFluids.FLOWING_RADIOACTIVE_WATER,
                new SimpleFluidRenderHandler(
                        Identifier.of("minecraft", "block/water_still"),
                        Identifier.of("minecraft", "block/water_flow"),
                        0xff597314
                ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ModFluids.STILL_RADIOACTIVE_WATER, ModFluids.FLOWING_RADIOACTIVE_WATER);
    }
}