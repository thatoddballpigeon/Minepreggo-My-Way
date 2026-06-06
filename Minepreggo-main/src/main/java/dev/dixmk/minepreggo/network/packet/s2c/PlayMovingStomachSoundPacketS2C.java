package dev.dixmk.minepreggo.network.packet.s2c;

import dev.dixmk.minepreggo.client.resources.sounds.*;
import dev.dixmk.minepreggo.init.MinepreggoCapabilities;
import dev.dixmk.minepreggo.init.MinepreggoModMobEffects;
import dev.dixmk.minepreggo.network.capability.IFemalePlayer;
import dev.dixmk.minepreggo.world.entity.preggo.ITamablePregnantPreggoMob;
import dev.dixmk.minepreggo.world.pregnancy.PregnancyPain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayMovingStomachSoundPacketS2C {
    private final int entityId;
    private final int soundId;

    public PlayMovingStomachSoundPacketS2C(int entityId, int soundId) {
        this.entityId = entityId;
        this.soundId = soundId;
    }

    public PlayMovingStomachSoundPacketS2C(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.soundId = buf.readInt();
    }

    // Encoder
    public static void encode(PlayMovingStomachSoundPacketS2C packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeInt(packet.soundId);
    }

    public static PlayMovingStomachSoundPacketS2C decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        int soundId = buf.readInt();
        return new PlayMovingStomachSoundPacketS2C(entityId, soundId);
    }

    public static void handler(PlayMovingStomachSoundPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                // Must be executed on the CLIENT side to prevent crashes!
                Minecraft mc = Minecraft.getInstance();
                Entity entity = mc.level.getEntity(packet.entityId);

                if (entity != null) {
                    mc.getSoundManager().play(getStomachGrowlSound((LivingEntity) entity, packet.soundId));
                }
            }
        });
        context.setPacketHandled(true);
    }

    protected static AbstractTickableSoundInstance getStomachGrowlSound(LivingEntity entity, int soundId) {
        return switch (soundId) {
            case 0 ->
                    new StomachAchingSoundInstance(entity);
            case 1 ->
                    new StomachSatedSoundInstance(entity);
            case 2 ->
                    new StomachEmptySoundInstance(entity);
            case 3 ->
                    new StomachFullSoundInstance(entity);
            case 4 ->
                    new StomachDigestingSoundInstance(entity);
            case 5 ->
                    new StomachUpsetSoundInstance(entity);
            default -> throw new IllegalStateException("Unexpected value: " + soundId);
        };
    }

}
