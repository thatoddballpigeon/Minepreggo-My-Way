package dev.dixmk.minepreggo.client.resources.sounds;

import dev.dixmk.minepreggo.init.MinepreggoModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class StomachFullSoundInstance extends StomachSoundInstance {
    public StomachFullSoundInstance(LivingEntity p_119621_) {
        super(p_119621_, MinepreggoModSounds.PREGNANT_STOMACH_FULL.get(), SoundSource.AMBIENT);
        this.delay = 0;
    }
}
