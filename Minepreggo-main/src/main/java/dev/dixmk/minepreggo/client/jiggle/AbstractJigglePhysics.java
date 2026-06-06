package dev.dixmk.minepreggo.client.jiggle;

import dev.dixmk.minepreggo.client.animation.player.PlayerAnimationManager;
import dev.dixmk.minepreggo.common.animation.CommonPlayerAnimationRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractJigglePhysics<E extends AbstractJigglePhysics.AbstractJigglePhysicsConfig> {
    protected float xVelocity = 0.0f;
    protected float yVelocity = 0.0f;
    protected float position = 0.0f;
    protected float previousPlayerY = 0.0f;
    
    protected final E config;
	
	protected AbstractJigglePhysics(E config) {
		this.config = config;
	}
	
	public void update(LivingEntity player, float playerY, float deltaTime) {
        // Calculate player velocity on Y-axis
        float verticalMovement = (playerY - previousPlayerY) / deltaTime;
        previousPlayerY = playerY;
        
        // Apply forces
        float springForce = -position * config.springStrength;
        float gravityForce = config.gravity * Math.signum(xVelocity);
        
        // Update velocity with player movement influence
        xVelocity += springForce - (verticalMovement * 0.1f) - gravityForce;
        xVelocity *= config.damping;
        
        // Update position
        position += xVelocity * deltaTime;
        
        position = Mth.clamp(position, -config.maxDisplacement, config.maxDisplacement);   
    }
    
    public float getOffset() {
        return position;
    }
    
    public void reset() {
        xVelocity = 0.0f;
        position = 0.0f;
    }
    
    @OnlyIn(Dist.CLIENT)
    public abstract static class AbstractJigglePhysicsConfig {
		public final float springStrength;
		public final float damping;
		public final float gravity;
		public final float maxDisplacement;
		public final float originalYPos;
		
		protected AbstractJigglePhysicsConfig(float springStrength, float damping, float gravity, float maxDisplacement, float originalYPos) {
			this.springStrength = springStrength;
			this.damping = damping;
			this.gravity = gravity;
			this.maxDisplacement = maxDisplacement;
			this.originalYPos = originalYPos;

		}
	}
}
