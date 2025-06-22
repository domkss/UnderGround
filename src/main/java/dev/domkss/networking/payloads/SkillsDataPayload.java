package dev.domkss.networking.payloads;

import dev.domkss.UnderGround;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SkillsDataPayload(
        dev.domkss.networking.payloads.SkillsDataPayload.SkillsData skillsData) implements CustomPayload {

    public static final Id<SkillsDataPayload> ID =
            new Id<>(Identifier.of(UnderGround.MOD_ID, "skills_data"));

    public static final PacketCodec<PacketByteBuf, SkillsDataPayload> CODEC = new PacketCodec<PacketByteBuf, SkillsDataPayload>() {
        @Override
        public void encode(PacketByteBuf buf, SkillsDataPayload payload) {
            buf.writeInt(payload.skillsData.health());
            buf.writeInt(payload.skillsData.armor());
            buf.writeInt(payload.skillsData.speed());
            buf.writeInt(payload.skillsData.haste());

        }

        @Override
        public SkillsDataPayload decode(PacketByteBuf buf) {
            int health = buf.readInt();
            int armor = buf.readInt();
            int speed = buf.readInt();
            int haste = buf.readInt();

            return new SkillsDataPayload(new SkillsData(health, armor, speed, haste));
        }
    };

    public static final Type<PacketByteBuf, SkillsDataPayload> TYPE = new Type<>(ID, CODEC);

    @Override
    public Id<?> getId() {
        return ID;
    }

    public record SkillsData(int health, int armor, int speed, int haste) {}
}


