package dev.domkss;

import dev.domkss.screen.SkillScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
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
    }
}