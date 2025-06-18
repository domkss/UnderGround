package dev.domkss.networking;

import dev.domkss.networking.payloads.RequestSkillsDataPayload;
import dev.domkss.networking.payloads.SkillsDataPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ServerNetworkHandler {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(SkillsDataPayload.ID, SkillsDataPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestSkillsDataPayload.ID, RequestSkillsDataPayload.CODEC);

        PacketHandler.registerGlobalServerReceiver(RequestSkillsDataPayload.ID, ((player, payload) ->
        {
            //Todo: Get Skills data
            PacketHandler.sendToClient(player, new SkillsDataPayload(new SkillsDataPayload.SkillsData(1, 2, 3, 4)));
        }
        ));
    }

}
