package dev.domkss.mixin.worldgen;

import dev.domkss.UnderGround;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        ServerWorld world = player.getServerWorld();
        BlockPos spawn = UnderGround.config.getSpawnPos();

        if(player.getY()>world.getTopYInclusive()) {
            player.refreshPositionAndAngles(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, 0.0F, 0.0F);
            player.setSpawnPoint(world.getRegistryKey(), spawn, 0.0f, true, false);
            player.teleport(spawn.getX(), spawn.getY(), spawn.getX(), false);
            UnderGround.LOGGER.info("[UnderGroundMod] Player forcibly positioned at spawn: {}", spawn);
        }

    }
}