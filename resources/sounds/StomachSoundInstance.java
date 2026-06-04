package dev.dixmk.minepreggo.client.resources.sounds;

import dev.dixmk.minepreggo.init.MinepreggoCapabilities;
import dev.dixmk.minepreggo.init.MinepreggoModMobEffects;
import dev.dixmk.minepreggo.world.entity.preggo.ITamablePregnantPreggoMob;
import dev.dixmk.minepreggo.world.pregnancy.PregnancyPain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class StomachSoundInstance extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0F;
    private static final float VOLUME_MAX = 1.2F;
    private static final float PITCH_MIN = 0.0F;
    protected final LivingEntity entity;

    public StomachSoundInstance(LivingEntity p_119621_, SoundEvent p_119622_, SoundSource p_119623_) {
        super(p_119622_, p_119623_, SoundInstance.createUnseededRandom());
        this.entity = p_119621_;
        this.positionToEntity();
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
        this.pitch = 0.9F + (this.random.nextFloat() * 0.2F);
    }

    public void tick() {
        boolean isPlaying = Minecraft.getInstance().getSoundManager().isActive(this);

        if (!this.entity.isRemoved() && isPlaying) {
            this.positionToEntity();
            this.volume = 1.0F;
        } else {
            this.stop();
        }
    }

    private void positionToEntity() {
        this.x = (float) this.entity.getX();
        this.y = (float) this.entity.getY();
        this.z = (float) this.entity.getZ();
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean canPlaySound() {
        return !this.entity.isSilent();
    }
}
