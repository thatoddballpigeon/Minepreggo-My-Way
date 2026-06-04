package dev.dixmk.minepreggo.network.packet.s2c;

import dev.dixmk.minepreggo.client.resources.sounds.*;
import dev.dixmk.minepreggo.init.MinepreggoModMobEffects;
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

    public PlayMovingStomachSoundPacketS2C(int entityId) {
        this.entityId = entityId;
    }

    public PlayMovingStomachSoundPacketS2C(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    // Encoder
    public static void encode(PlayMovingStomachSoundPacketS2C packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
    }

    public static PlayMovingStomachSoundPacketS2C decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        return new PlayMovingStomachSoundPacketS2C(entityId);
    }

    public static void handler(PlayMovingStomachSoundPacketS2C packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                // Must be executed on the CLIENT side to prevent crashes!
                Minecraft mc = Minecraft.getInstance();
                Entity entity = mc.level.getEntity(packet.entityId);

                if (entity != null) {
                    // Create your moving sound instance (you will need a custom MovingSound/TickableSound class)
                    // Example: MovingSoundInstance extends TickableSoundInstance
                    AbstractTickableSoundInstance sound = checkSounds((LivingEntity) entity);

                    mc.getSoundManager().play(sound);
                }
            }
        });
        context.setPacketHandled(true);
    }

    protected static AbstractTickableSoundInstance checkSounds(LivingEntity entity) {
        int foodLevel;
        int id = -1;
        if (entity instanceof Player player) {
                            /*
                player.getCapability(MinepreggoCapabilities.PLAYER_DATA).ifPresent(cap -> {
                    cap.getFemaleData().ifPresent(femaleData -> {
                        var pregnancySystem = femaleData.getPregnancyData();
                        final var pregnancyHealth = pregnancySystem.getPregnancyHealth();
                        if (pregnancyHealth <= 40
                                || PregnancyPain.isLaborPain(pregnancySystem.getPregnancyPain())) {
                            id = 0;
                        }
                    });
                });
                */

            foodLevel = player.getFoodData().getFoodLevel();
            if (player.hasEffect(MinepreggoModMobEffects.MORNING_SICKNESS.get())) {
                id = 5;
            } else if (id != 0) {
                if (player.getFoodData().getSaturationLevel() > 0) {
                    id = 1;
                } else if (foodLevel <= 6) {
                    id = 2;
                } else if (foodLevel >= 14) {
                    id = 3;
                } else {
                    id = 4;
                }
            }
        } else if (entity instanceof ITamablePregnantPreggoMob handler) {
            foodLevel = handler.getTamableData().getFullness();
            var pregnancySystem = handler.getPregnancyData();
            var pregnancyHealth = pregnancySystem.getPregnancyHealth();
            if (pregnancyHealth <= 40
                    || PregnancyPain.isLaborPain(pregnancySystem.getPregnancyPain())) {
                id = 0;
            }

            if (entity.hasEffect(MinepreggoModMobEffects.MORNING_SICKNESS.get())) {
                id = 5;
            } else if (id != 0) {
                if (foodLevel >= 20) {
                    id = 1;
                } else if (foodLevel <= 6) {
                    id = 2;
                } else if (foodLevel >= 14) {
                    id = 3;
                } else {
                    id = 4;
                }
            }
        }
        return getAlternativeSounds(entity, id);
    }

    protected static AbstractTickableSoundInstance getAlternativeSounds(LivingEntity entity, int id) {
        return switch (id) {
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
            default ->
                    new StomachUpsetSoundInstance(entity);
        };
    }
}
