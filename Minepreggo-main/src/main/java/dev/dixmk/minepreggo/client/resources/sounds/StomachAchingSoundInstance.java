package dev.dixmk.minepreggo.client.resources.sounds;

import dev.dixmk.minepreggo.init.MinepreggoModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class StomachAchingSoundInstance extends StomachSoundInstance {
    public StomachAchingSoundInstance(LivingEntity p_119621_) {
        super(p_119621_, MinepreggoModSounds.PREGNANT_STOMACH_ACHING.get(), SoundSource.NEUTRAL);
    }
}