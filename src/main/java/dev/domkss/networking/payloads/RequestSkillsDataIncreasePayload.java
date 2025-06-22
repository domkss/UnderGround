package dev.domkss.networking.payloads;

import dev.domkss.UnderGround;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestSkillsDataIncreasePayload(String statKey) implements CustomPayload {

    public static final Id<RequestSkillsDataIncreasePayload> ID =
            new Id<>(Identifier.of(UnderGround.MOD_ID, "skills_data_increase"));

    public static final PacketCodec<PacketByteBuf, RequestSkillsDataIncreasePayload> CODEC = new PacketCodec<PacketByteBuf, RequestSkillsDataIncreasePayload>() {
        @Override
        public void encode(PacketByteBuf buf, RequestSkillsDataIncreasePayload payload) {
            buf.writeString(payload.statKey);
        }

        @Override
        public RequestSkillsDataIncreasePayload decode(PacketByteBuf buf) {
            String statKey = buf.readString();
            return new RequestSkillsDataIncreasePayload(statKey);
        }
    };

    public static final Type<PacketByteBuf, RequestSkillsDataIncreasePayload> TYPE = new Type<>(ID, CODEC);

    @Override
    public Id<?> getId() {
        return ID;
    }

}


