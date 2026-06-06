package dev.dixmk.minepreggo.client.resources.sounds;

import dev.dixmk.minepreggo.init.MinepreggoCapabilities;
import dev.dixmk.minepreggo.init.MinepreggoModMobEffects;
import dev.dixmk.minepreggo.init.MinepreggoModSounds;
import dev.dixmk.minepreggo.world.entity.preggo.ITamablePregnantPreggoMob;
import dev.dixmk.minepreggo.world.pregnancy.AbstractPregnancySystem;
import dev.dixmk.minepreggo.world.pregnancy.PregnancyPain;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.BeeFlyingSoundInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class StomachFullSoundInstance extends StomachSoundInstance {
    public StomachFullSoundInstance(LivingEntity p_119621_) {
        super(p_119621_, MinepreggoModSounds.PREGNANT_STOMACH_FULL.get(), SoundSource.NEUTRAL);
        this.volume = 1.0F;
    }
}
