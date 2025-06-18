package dev.domkss.networking.payloads;

import dev.domkss.UnderGround;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class RequestSkillsDataPayload implements CustomPayload {

    public static final CustomPayload.Id<RequestSkillsDataPayload> ID =
            new CustomPayload.Id<>(Identifier.of(UnderGround.MOD_ID, "request_skills_data"));

    public static final PacketCodec<PacketByteBuf, RequestSkillsDataPayload> CODEC = new PacketCodec<PacketByteBuf, RequestSkillsDataPayload>() {
        @Override
        public void encode(PacketByteBuf buf, RequestSkillsDataPayload payload) {

        }

        @Override
        public RequestSkillsDataPayload decode(PacketByteBuf buf) {
            return new RequestSkillsDataPayload();
        }
    };

    public static final CustomPayload.Type<PacketByteBuf, RequestSkillsDataPayload> TYPE = new CustomPayload.Type<>(ID, CODEC);

    public RequestSkillsDataPayload() {
    }

    @Override
    public CustomPayload.Id<?> getId() {
        return ID;
    }


}
