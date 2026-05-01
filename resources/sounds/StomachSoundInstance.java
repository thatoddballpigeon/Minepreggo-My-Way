package dev.dixmk.minepreggo.client.resources.sounds;

import dev.dixmk.minepreggo.init.MinepreggoModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;

public class StomachSoundInstance extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0F;
    private static final float VOLUME_MAX = 1.2F;
    private static final float PITCH_MIN = 0.0F;
    protected final LivingEntity entity;
    private boolean hasSwitched;
    private int time;

    public StomachSoundInstance(LivingEntity p_119621_, SoundEvent p_119622_, SoundSource p_119623_) {
        super(p_119622_, p_119623_, SoundInstance.createUnseededRandom());
        this.entity = p_119621_;
        this.x = (float) p_119621_.getX();
        this.y = (float) p_119621_.getY();
        this.z = (float) p_119621_.getZ();
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
        this.pitch = this.random.nextFloat() * 0.2F + 0.9F;
    }

    public void tick() {
        ++this.time;
        if (!this.entity.isRemoved() && this.time <= 160) {
            this.x = (float) this.entity.getX();
            this.y = (float) this.entity.getY();
            this.z = (float) this.entity.getZ();
            this.volume = 1.0F;
        } else {
            this.stop();
        }
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean canPlaySound() {
        return !this.entity.isSilent();
    }
}
