package dev.domkss.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.CustomPayload.Id;
import net.minecraft.server.network.ServerPlayerEntity;


public class PacketHandler {

    public interface ServerPayloadHandler<T extends CustomPayload> {
        void handle(ServerPlayerEntity player, T payload);
    }

    public interface ClientPayloadHandler<T extends CustomPayload> {
        void handle(MinecraftClient client, T payload);
    }

    public static <T extends CustomPayload> void registerGlobalServerReceiver(
            Id<T> id,
            ServerPayloadHandler<T> handler
    ) {
        ServerPlayNetworking.registerGlobalReceiver(id, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            player.getServer().execute(() -> handler.handle(player, payload));
        });
    }

    public static <T extends CustomPayload> void registerGlobalClientReceiver(
            Id<T> id,
            ClientPayloadHandler<T> handler
    ) {
        ClientPlayNetworking.registerGlobalReceiver(id, (payload, context) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> handler.handle(client, payload));
        });
    }

    public static void sendToServer(CustomPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    public static void sendToClient(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }
}
