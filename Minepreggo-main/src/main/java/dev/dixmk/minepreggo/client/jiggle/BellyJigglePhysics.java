package dev.dixmk.minepreggo.client.jiggle;

import dev.dixmk.minepreggo.client.animation.player.PlayerAnimationManager;
import dev.dixmk.minepreggo.common.animation.CommonPlayerAnimationRegistry;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BellyJigglePhysics extends AbstractJigglePhysics<BellyJigglePhysics.JigglePhysicsConfig> {
    private float xRotationVelocity = 0.0f;
    private float yRotationVelocity = 0.0f;
    private float xRotation = 0.0f;
    private float yRotation = 0.0f;
	
    private double previousPlayerX = 0.0;
    private double previousPlayerZ = 0.0;
    private float movementIntensity = 0.0f;

    private boolean wasInAir = false;
	
	protected BellyJigglePhysics(JigglePhysicsConfig config) {
		super(config);
	}
	
	public BellyJigglePhysics(Builder builder) {
		this(new JigglePhysicsConfig(builder));
	}

	public static BellyJigglePhysics.Builder builder() {
		return new BellyJigglePhysics.Builder();
	}
	
    public void update(LivingEntity player, float playerY, double playerX, double playerZ, float deltaTime, boolean isMoving) {
        if (player instanceof Player playerEntity) {
            final var cache = PlayerAnimationManager.getInstance().get(playerEntity);
            if (CommonPlayerAnimationRegistry.getInstance().isBellyRubbingAnimation(cache.getCurrentAnimationName())) {
                previousPlayerX = playerX;
                previousPlayerY = playerY;
                previousPlayerZ = playerZ;
                this.reset();
                return;
            }
        }
        this.calculateBellyHorizontalMovement(playerX, playerZ, deltaTime, isMoving);
        this.calculateBellyVerticalMovement(playerY, deltaTime, isMoving, player.onGround());
    }

    private void calculateBellyVerticalMovement(float playerY, float deltaTime, boolean isMoving, boolean isOnGround) {
        // Calculate player velocity on Y-axis
        float verticalMovement = (playerY - previousPlayerY) / deltaTime;
        previousPlayerY = playerY;

        boolean hasVerticalMovement = Math.abs(verticalMovement) > 0.05f;
        boolean isInAir = !isOnGround || hasVerticalMovement;

        if (isMoving && verticalMovement > 0.001f) {
            movementIntensity = Math.min(1.0f, movementIntensity + verticalMovement * config.movementMultiplier);
        } else {
            movementIntensity *= config.movementDecay;
        }

        xVelocity += verticalMovement * 0.15f;
        xVelocity *= config.damping;

        if (!isInAir && wasInAir) {
            xVelocity = -xVelocity;
            previousPlayerY = playerY;
        }

        wasInAir = isInAir;

        float targetRotationX = xVelocity * 0.5f + (movementIntensity * 0.2f * (float) Math.cos(System.currentTimeMillis() * 0.008));
        float rotationSpringForce = (targetRotationX - xRotation) * config.rotationSpring;

        xRotationVelocity += rotationSpringForce;
        xRotationVelocity *= config.rotationDamping;

        float maxXRotation = config.maxRotation / 2;

        xRotation += xRotationVelocity * deltaTime;
        xRotation = Math.max(-maxXRotation, Math.min(maxXRotation, xRotation));
    }

    private void calculateBellyHorizontalMovement(double playerX, double playerZ, float deltaTime, boolean isMoving) {
        // Calculate horizontal movement
        double deltaX = playerX - previousPlayerX;
        double deltaZ = playerZ - previousPlayerZ;
        float horizontalMovement = (float) Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        previousPlayerX = playerX;
        previousPlayerZ = playerZ;

        // Update movement intensity with decay
        if (isMoving && horizontalMovement > 0.001f) {
            movementIntensity = Math.min(1.0f, movementIntensity + horizontalMovement * config.movementMultiplier);
        } else {
            movementIntensity *= config.movementDecay;
        }

        // Apply forces to vertical position
        float springForce = -position * config.springStrength;
        float gravityForce = config.gravity * Math.signum(yVelocity);
        float movementForce = movementIntensity * 0.05f * (float) Math.sin(System.currentTimeMillis() * 0.01);

        // Update velocity with player movement influence
        yVelocity += springForce - gravityForce + movementForce;
        yVelocity *= config.damping;

        // Update position
        position += yVelocity * deltaTime;
        position = Math.max(-config.maxDisplacement, Math.min(config.maxDisplacement, position));

        // Calculate rotation based on velocity and movement
        float targetRotationY = yVelocity * 0.5f + (movementIntensity * 0.2f * (float) Math.cos(System.currentTimeMillis() * 0.008));
        float rotationSpringForce = (targetRotationY - yRotation) * config.rotationSpring;

        yRotationVelocity += rotationSpringForce;
        yRotationVelocity *= config.rotationDamping;

        yRotation += yRotationVelocity * deltaTime;
        yRotation = Math.max(-config.maxRotation, Math.min(config.maxRotation, yRotation));
    }

    public float getXRotation() {
        return xRotation;
    }

    public float getYRotation() {
        return yRotation;
    }
    
    public float getMovementIntensity() {
        return movementIntensity;
    }
    
    @Override
    public void reset() {
    	super.reset();
        xRotation = 0.0f;
        yRotation = 0.0f;
        xRotationVelocity = 0.0f;
        yRotationVelocity = 0.0f;
        movementIntensity = 0.0f;
    }
    
    public void setupAnim(LivingEntity entity, ModelPart bellyModel, boolean simple) {			
		// Update jiggle physics with movement data
		float deltaTime = 0.05f; // Approximate frame time
		if (!simple) {
			this.update(entity, (float) entity.getY(), entity.getX(), entity.getZ(), deltaTime, entity.walkAnimation.isMoving());
            bellyModel.y = config.originalYPos + this.getOffset();
            bellyModel.xRot = this.getXRotation();
            bellyModel.yRot = this.getYRotation();
		}
		else {
			this.update(entity, (float) entity.getY(), deltaTime);
            bellyModel.y = config.originalYPos + this.getOffset();
        }
	}
    
    
    @OnlyIn(Dist.CLIENT)
    public static class Builder {
    	private float springStrength = 0.15f;
    	private float damping = 0.85f;
    	private float gravity = 0.02f;
    	private float maxDisplacement = 0.3f;
        protected float originalYPos = 2.0f;
        
        private float rotationSpring = 0.2f;
        private float rotationDamping = 0.88f;
        private float maxRotation = 0.125f;
        
        private float movementMultiplier = 0.3f;
        private float movementDecay = 0.92f;
    	  
    	
    	public BellyJigglePhysics build() {
    		return new BellyJigglePhysics(this);
    	}
        
    	public Builder springStrength(float springStrength) {
    		this.springStrength = springStrength;
    		return this;
    	}
    	
    	public Builder damping(float damping) {
    		this.damping = damping;
    		return this;
    	}
    	
    	public Builder gravity(float gravity) {
    		this.gravity = gravity;
    		return this;
    	}
    	
    	public Builder originalYPos(float originalYPos) {
    		this.originalYPos = originalYPos;
    		return this;
    	}
	
    	public Builder maxDisplacement(float maxDisplacement) {
    		this.maxDisplacement = maxDisplacement;
    		return this;
    	}

        public Builder rotationSpring(float rotationSpring) {
        	this.rotationSpring = rotationSpring;
        	return this;
        }
        
        public Builder rotationDamping(float rotationDamping) {
        	this.rotationDamping = rotationDamping;
        	return this;
        }
        
        public Builder maxRotation(float maxRotation) {
        	this.maxRotation = maxRotation;
        	return this;
        }
        
        public Builder movementMultiplier(float movementMultiplier) {
        	this.movementMultiplier = movementMultiplier;
        	return this;
        }
        
        public Builder movementDecay(float movementDecay) {
        	this.movementDecay = movementDecay;
        	return this;
        }       
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class JigglePhysicsConfig extends AbstractJigglePhysics.AbstractJigglePhysicsConfig {
        public final float rotationSpring;
        public final float rotationDamping;
        public final float maxRotation;
        public final float movementMultiplier;
        public final float movementDecay;

        public JigglePhysicsConfig(Builder builder) {
            super(builder.springStrength, builder.damping, builder.gravity, builder.maxDisplacement, builder.originalYPos);
            this.rotationSpring = builder.rotationSpring;
            this.rotationDamping = builder.rotationDamping;
            this.maxRotation = builder.maxRotation;
            this.movementMultiplier = builder.movementMultiplier;
            this.movementDecay = builder.movementDecay;
        }
    }
}
