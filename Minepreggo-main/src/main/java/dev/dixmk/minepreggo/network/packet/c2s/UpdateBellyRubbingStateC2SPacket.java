package dev.dixmk.minepreggo.network.packet.c2s;

import java.util.UUID;
import java.util.function.Supplier;

import dev.dixmk.minepreggo.init.MinepreggoCapabilities;
import dev.dixmk.minepreggo.init.MinepreggoModItems;
import dev.dixmk.minepreggo.init.MinepreggoModMobEffects;
import dev.dixmk.minepreggo.server.ServerParticleHelper;
import dev.dixmk.minepreggo.world.pregnancy.PregnancyPhase;
import dev.dixmk.minepreggo.world.pregnancy.PregnancySymptom;
import dev.dixmk.minepreggo.world.pregnancy.PregnancySystemHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;

public record UpdateBellyRubbingStateC2SPacket(UUID target) {

	public static UpdateBellyRubbingStateC2SPacket decode(FriendlyByteBuf buffer) {	
		return new UpdateBellyRubbingStateC2SPacket(buffer.readUUID());
	}
		
	public static void encode(UpdateBellyRubbingStateC2SPacket message, FriendlyByteBuf buffer) {
		buffer.writeUUID(message.target);
	}
	 
	public static void handler(UpdateBellyRubbingStateC2SPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {					
	       if (context.getDirection().getReceptionSide().isServer()) {
	           var source = context.getSender();
	           var level = source.level();         		
	           if (!(level.getPlayerByUUID(message.target) instanceof ServerPlayer target)) {
	        	   return;
	           }
	           
	   	        target.getCapability(MinepreggoCapabilities.PLAYER_DATA).ifPresent(cap -> 
   	        	cap.getFemaleData().ifPresent(femaleData -> {
   	        		if (femaleData.isPregnant() && femaleData.isPregnancyDataInitialized()) {
   	        			var system = femaleData.getPregnancyData();
   	        			var bellyRubs = system.getBellyRubs();

						var mainHand = target.getMainHandItem().getItem();
						var offHand = target.getOffhandItem().getItem();

						if ((mainHand == MinepreggoModItems.BELLY_OIL.get() || offHand == MinepreggoModItems.BELLY_OIL.get())
						&& !target.hasEffect(MinepreggoModMobEffects.BELLY_LUBRICATION.get())) {
							target.addEffect(new MobEffectInstance(MinepreggoModMobEffects.BELLY_LUBRICATION.get(), 2400, 0, false, false, true));

							if (mainHand == MinepreggoModItems.BELLY_OIL.get()) {
								mainHand.finishUsingItem(target.getMainHandItem(), level, target);
							} else {
								offHand.finishUsingItem(target.getOffhandItem(), level, target);
							}
						}

   	        			if (system.getCurrentPregnancyPhase().compareTo(PregnancyPhase.P2) > 0 && bellyRubs > 0) {
						   bellyRubs -= PregnancySystemHelper.BELLY_RUBBING_VALUE;
						   system.setBellyRubs(bellyRubs);

						   ServerParticleHelper.spawnRandomlyFromServer(target, ParticleTypes.HEART);

						   if (system.getPregnancySymptoms().containsPregnancySymptom((PregnancySymptom.BELLY_RUBS)) && bellyRubs <= PregnancySystemHelper.DESACTIVATEL_BELLY_RUBS_SYMPTOM) {
							   system.getPregnancySymptoms().removePregnancySymptom(PregnancySymptom.BELLY_RUBS);
							   target.removeEffect(MinepreggoModMobEffects.BELLY_RUBS.get());
							   system.syncState(target);
						   }
						   system.syncEffect(target);
   	        			}    			
   	        		}
   	        	})	
   	        ); 
	       }			
		});
		context.setPacketHandled(true);
	}
}
