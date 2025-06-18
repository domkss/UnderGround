package dev.domkss.networking.payloads;

import dev.domkss.UnderGround;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class SkillsDataPayload implements CustomPayload {

    private final SkillsData skillsData;

    public static final CustomPayload.Id<SkillsDataPayload> ID =
            new CustomPayload.Id<>(Identifier.of(UnderGround.MOD_ID, "skills_data"));

    public static final PacketCodec<PacketByteBuf, SkillsDataPayload> CODEC = new PacketCodec<PacketByteBuf, SkillsDataPayload>() {
        @Override
        public void encode(PacketByteBuf buf, SkillsDataPayload payload) {
            buf.writeInt(payload.skillsData.getHealth());
            buf.writeInt(payload.skillsData.getArmor());
            buf.writeInt(payload.skillsData.getSpeed());
            buf.writeInt(payload.skillsData.getHaste());

        }

        @Override
        public SkillsDataPayload decode(PacketByteBuf buf) {
            int health= buf.readInt();
            int armor = buf.readInt();
            int speed = buf .readInt();
            int haste = buf.readInt();

            return new SkillsDataPayload(new SkillsData(health, armor, speed, haste));
        }
    };

    public static final CustomPayload.Type<PacketByteBuf, SkillsDataPayload> TYPE = new CustomPayload.Type<>(ID, CODEC);

    public SkillsDataPayload(SkillsData skillsData) {
        this.skillsData=skillsData;
    }

    @Override
    public CustomPayload.Id<?> getId() {
        return ID;
    }

    public SkillsData getSkillsData() {
        return skillsData;
    }

    public static class SkillsData {
        int health;
        int armor;
        int speed;
        int haste;

        public SkillsData(int health, int armor, int speed, int haste) {
            this.health = health;
            this.armor = armor;
            this.speed = speed;
            this.haste = haste;
        }

        public int getHealth() {
            return health;
        }

        public void setHealth(int health) {
            this.health = health;
        }

        public int getHaste() {
            return haste;
        }

        public void setHaste(int haste) {
            this.haste = haste;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public int getArmor() {
            return armor;
        }

        public void setArmor(int armor) {
            this.armor = armor;
        }
    }
}


