package dev.domkss.networking.payloads;

import com.mojang.datafixers.util.Pair;
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
            buf.writeInt(payload.skillsData.health.getFirst());
            buf.writeInt(payload.skillsData.health.getSecond());
            buf.writeInt(payload.skillsData.armor.getFirst());
            buf.writeInt(payload.skillsData.armor.getSecond());
            buf.writeInt(payload.skillsData.speed.getFirst());
            buf.writeInt(payload.skillsData.speed.getSecond());
            buf.writeInt(payload.skillsData.haste.getFirst());
            buf.writeInt(payload.skillsData.haste.getSecond());
            buf.writeInt(payload.skillsData.radiation_resistance.getFirst());
            buf.writeInt(payload.skillsData.radiation_resistance.getSecond());

        }

        @Override
        public SkillsDataPayload decode(PacketByteBuf buf) {
            int bonus_health = buf.readInt();
            int max_bonus_health = buf.readInt();

            int bonus_armor = buf.readInt();
            int max_bonus_armor = buf.readInt();

            int bonus_speed = buf.readInt();
            int max_bonus_speed = buf.readInt();

            int bonus_haste = buf.readInt();
            int max_bonus_haste = buf.readInt();

            int bonus_radiation_resistance = buf.readInt();
            int max_radiation_resistance = buf.readInt();

            return new SkillsDataPayload(new SkillsData(Pair.of(bonus_health, max_bonus_health), Pair.of(bonus_armor, max_bonus_armor),
                    Pair.of(bonus_speed, max_bonus_speed), Pair.of(bonus_haste, max_bonus_haste), Pair.of(bonus_radiation_resistance, max_radiation_resistance)));
        }
    };

    public static final Type<PacketByteBuf, SkillsDataPayload> TYPE = new Type<>(ID, CODEC);

    @Override
    public Id<?> getId() {
        return ID;
    }

    public record SkillsData(Pair<Integer, Integer> health, Pair<Integer, Integer> armor,
                             Pair<Integer, Integer> speed, Pair<Integer, Integer> haste,
                             Pair<Integer, Integer> radiation_resistance) {
    }
}


