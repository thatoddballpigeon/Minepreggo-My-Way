package dev.dixmk.minepreggo.client.jiggle;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoobJigglePhysics extends AbstractJigglePhysics<BoobJigglePhysics.JigglePhysicsConfig> {	
	private float rotationVelocityY = 0.0f;
    private float rotationY = 0.0f;
    private float rotationVelocityX = 0.0f;
    private float rotationX = 0.0f;
    private float rotationVelocityZ = 0.0f;
    private float rotationZ = 0.0f;
	
    private double previousPlayerX = 0.0;
    private double previousPlayerZ = 0.0;
    private float movementIntensity = 0.0f;
    
    private float oscillationTime = 0.0f;
    
    private boolean wasInAir = false;
    private float jumpTargetRotationY = 0.0f;
    private float jumpTargetRotationZ = 0.0f;
    private boolean jumpTargetsSet = false;
    private float jumpElevationTarget = 0.0f;
    private float jumpElevationCurrent = 0.0f;
    
    private boolean axisY = true;
    private boolean axisZ = true;
            
    public BoobJigglePhysics(JigglePhysicsConfig config) {
		super(config);
	}
    
    public BoobJigglePhysics(Builder builder) {
    	this(new JigglePhysicsConfig(builder));
	}

	public static BoobJigglePhysics.Builder builder(float phaseOffset, boolean isLeft) {
		return new BoobJigglePhysics.Builder(phaseOffset, isLeft);
	}
    
    public void update(float playerY, double playerX, double playerZ, float deltaTime, boolean isMoving, boolean isOnGround) {
        this.calculateBoobsHorizontalMovement(playerX, playerZ, deltaTime, isMoving);
        this.calculateBoobsVerticalMovement(playerY, deltaTime, isMoving, isOnGround);
    }

    private void calculateBoobsVerticalMovement(float playerY, float deltaTime, boolean isMoving, boolean isOnGround) {
        float verticalMovement = (playerY - previousPlayerY) / deltaTime;
        previousPlayerY = playerY;

        boolean hasVerticalMovement = Math.abs(verticalMovement) > 0.05f;
        boolean isInAir = !isOnGround || hasVerticalMovement;

        if (isInAir && !wasInAir) {
            jumpTargetRotationY = (float) ((Math.random() * 2.0 - 1.0) * config.maxRotationX);
            jumpTargetRotationZ = (float) ((Math.random() * 2.0 - 1.0) * config.maxRotationZ);
            jumpTargetsSet = true;

            jumpElevationTarget = config.jumpElevationMin + (float) (Math.random() * (config.jumpElevationMax - config.jumpElevationMin));
        }

        if (!isInAir && wasInAir) {
            jumpTargetsSet = false;
            jumpElevationTarget = 0.0f; // Return to zero on landing
        }

        wasInAir = isInAir;

        if (isMoving && verticalMovement > 0.001f) {
            movementIntensity = Math.min(1.0f, movementIntensity + verticalMovement * config.movementMultiplier);
        } else {
            movementIntensity *= config.movementDecay;
        }

        if (hasVerticalMovement) {
            oscillationTime += config.oscillationSpeed * Math.max(movementIntensity, Math.abs(verticalMovement) * 2.0f);
        }

        float elevationSpringForce = (jumpElevationTarget - jumpElevationCurrent) * config.jumpElevationSpring;
        jumpElevationCurrent += elevationSpringForce;
        jumpElevationCurrent *= config.jumpElevationDamping;

        float phaseTime = oscillationTime * config.asymmetricFrequency + config.phaseOffset;
        boolean hasAnyMovement = movementIntensity > 0.05f || hasVerticalMovement;
        float springForce = -position * config.springStrength;
        float gravityForce = config.gravity * Math.signum(xVelocity);
        float movementForce = movementIntensity * 0.08f * (float) Math.sin(phaseTime);
        float asymmetricBounce = config.phaseInfluence * (float) Math.cos(phaseTime * 1.3f + config.sideMultiplier * 0.5f) * movementIntensity;

        asymmetricBounce += config.sideMultiplier * config.asymetricDelay * (float) Math.sin(phaseTime * 1.5f) * movementIntensity;

        xVelocity += springForce - gravityForce + movementForce + asymmetricBounce;
        xVelocity *= config.damping;

        position += xVelocity * deltaTime;
        position = Math.max(-config.maxDisplacement, Math.min(config.maxDisplacement, position));

        float targetRotationX;
        if (hasAnyMovement) {
            targetRotationX = movementIntensity * 0.25f * (float) Math.cos(phaseTime * 0.8);
            targetRotationX += xVelocity * 0.4f;
            targetRotationX += verticalMovement * 0.2f;
            targetRotationX += config.sideMultiplier * config.sideInfluence * 1.5f * movementIntensity * (float) Math.sin(phaseTime * 1.1);
            targetRotationX += config.sideMultiplier * 0.05f * movementIntensity;
        } else {
            targetRotationX = 0.0f;
        }

        float rotationSpringForceX = (targetRotationX - rotationX) * (hasAnyMovement ? config.rotationSpringX : config.rotationReturnSpring);
        rotationVelocityX += rotationSpringForceX;
        rotationVelocityX *= config.rotationDamping;

        float maxRotationX = config.maxRotationX * 2;
        rotationX += rotationVelocityX * deltaTime;
        rotationX = Math.max(-maxRotationX, Math.min(maxRotationX, rotationX));
    }

    private void calculateBoobsHorizontalMovement(double playerX, double playerZ, float deltaTime, boolean isMoving) {
        double deltaX = playerX - previousPlayerX;
        double deltaZ = playerZ - previousPlayerZ;
        float horizontalMovement = (float) Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        previousPlayerX = playerX;
        previousPlayerZ = playerZ;

        if (isMoving && horizontalMovement > 0.001f) {
            movementIntensity = Math.min(1.0f, movementIntensity + horizontalMovement * config.movementMultiplier);
        } else {
            movementIntensity *= config.movementDecay;
        }

        if (movementIntensity > 0.01f) {
            oscillationTime += config.oscillationSpeed * Math.max(movementIntensity, Math.abs(horizontalMovement) * 2.0f);
        }

        float elevationSpringForce = (jumpElevationTarget - jumpElevationCurrent) * config.jumpElevationSpring;
        jumpElevationCurrent += elevationSpringForce;
        jumpElevationCurrent *= config.jumpElevationDamping;

        float phaseTime = oscillationTime * config.asymmetricFrequency + config.phaseOffset;
        boolean hasAnyMovement = movementIntensity > 0.05f;
        float springForce = -position * config.springStrength;
        float gravityForce = config.gravity * Math.signum(yVelocity);
        float movementForce = movementIntensity * 0.08f * (float) Math.sin(phaseTime);
        float asymmetricBounce = config.phaseInfluence * (float) Math.cos(phaseTime * 1.3f + config.sideMultiplier * 0.5f) * movementIntensity;

        asymmetricBounce += config.sideMultiplier * config.asymetricDelay * (float) Math.sin(phaseTime * 1.5f) * movementIntensity;

        yVelocity += springForce - gravityForce + movementForce + asymmetricBounce;
        yVelocity *= config.damping;

        position += yVelocity * deltaTime;
        position = Math.max(-config.maxDisplacement, Math.min(config.maxDisplacement, position));

        float targetRotationY;
        if (hasAnyMovement) {
            targetRotationY = movementIntensity * 0.25f * (float) Math.cos(phaseTime * 0.8);
            targetRotationY += yVelocity * 0.4f;
            targetRotationY += horizontalMovement * 0.3f;
            targetRotationY += config.sideMultiplier * config.sideInfluence * 1.5f * movementIntensity * (float) Math.sin(phaseTime * 1.1);
            targetRotationY += config.sideMultiplier * 0.05f * movementIntensity;
        } else {
            targetRotationY = 0.0f;
        }

        float rotationSpringForceY = (targetRotationY - rotationY) * (hasAnyMovement ? config.rotationSpringY : config.rotationReturnSpring);
        rotationVelocityY += rotationSpringForceY;
        rotationVelocityY *= config.rotationDamping;

        rotationY += rotationVelocityY * deltaTime;
        rotationY = Math.max(-config.maxRotationY, Math.min(config.maxRotationY, rotationY));

        if (axisY) {
            updateAxisY(deltaTime, phaseTime, horizontalMovement, hasAnyMovement);
        }
        if (axisZ) {
            updateAxisZ(deltaTime, phaseTime, horizontalMovement, hasAnyMovement);
        }
    }

    private void updateAxisY(float deltaTime, float phaseTime, float horizontalMovement, boolean hasAnyMovement) {
        float targetRotationY;
        if (hasAnyMovement) {
            if (jumpTargetsSet) {
                targetRotationY = jumpTargetRotationY;
                targetRotationY += (float) Math.sin(phaseTime * 2.5) * Math.abs(horizontalMovement) * 0.15f;
                targetRotationY += config.sideMultiplier * config.asymetricDelay * (float) Math.cos(phaseTime * 2.0) * Math.abs(horizontalMovement) * 0.2f;
            } else {
                targetRotationY = movementIntensity * 0.2f * (float) Math.sin(phaseTime * 1.2);
                targetRotationY += horizontalMovement * 0.25f;
                targetRotationY += config.sideMultiplier * config.sideInfluence * 0.8f * (float) Math.cos(phaseTime * 0.95);
            }
        } else {
            targetRotationY = 0.0f;
        }

        float rotationSpringForceY = (targetRotationY - rotationY) *
                (hasAnyMovement ? config.rotationSpringY : config.rotationReturnSpring);
        rotationVelocityY += rotationSpringForceY;
        rotationVelocityY *= config.rotationDamping;

        rotationY += rotationVelocityY * deltaTime;
        rotationY = Math.max(-config.maxRotationY, Math.min(config.maxRotationY, rotationY));
    }

    private void updateAxisZ(float deltaTime, float phaseTime, float horizontalMovement, boolean hasAnyMovement) {
        float targetRotationZ;
        if (hasAnyMovement) {
            if (jumpTargetsSet) {
                targetRotationZ = jumpTargetRotationZ;
                targetRotationZ += config.sideMultiplier * (float) Math.cos(phaseTime * 2.3) * Math.abs(horizontalMovement) * 0.18f;
                targetRotationZ += config.sideMultiplier * config.asymetricDelay * (float) Math.sin(phaseTime * 2.2) * Math.abs(horizontalMovement) * 0.25f;
            } else {
                targetRotationZ = movementIntensity * 0.22f * (float) Math.cos(phaseTime * 1.05);
                targetRotationZ += config.sideMultiplier * yVelocity * 0.6f;
                targetRotationZ += config.sideMultiplier * config.sideInfluence * 1.5f * (float) Math.sin(phaseTime * 1.15);
            }
        } else {
            targetRotationZ = 0.0f;
        }

        float rotationSpringForceZ = (targetRotationZ - rotationZ) *
                (hasAnyMovement ? config.rotationSpringZ : config.rotationReturnSpring);
        rotationVelocityZ += rotationSpringForceZ;
        rotationVelocityZ *= config.rotationDamping;

        rotationZ += rotationVelocityZ * deltaTime;
        rotationZ = Math.max(-config.maxRotationZ, Math.min(config.maxRotationZ, rotationZ));
    }
    
    @Override
    public float getOffset() {
    	return position - jumpElevationCurrent;
    }
    
    public float getRotationY() {
        return rotationY;
    }
    
    public float getRotationX() {
        return rotationX;
    }
    
    public float getRotationZ() {
        return rotationZ;
    }
    
    public float getMovementIntensity() {
        return movementIntensity;
    }

    @Override
    public void reset() {
    	super.reset();
        rotationVelocityY = 0.0f;
        rotationY = 0.0f;
        rotationVelocityX = 0.0f;
        rotationX = 0.0f;
        rotationVelocityZ = 0.0f;
        rotationZ = 0.0f;
        movementIntensity = 0.0f;
        oscillationTime = 0f;
        wasInAir = false;
        jumpTargetRotationY = 0.0f;
        jumpTargetRotationZ = 0.0f;
        jumpTargetsSet = false;
    }
	
    public void useAxisY(boolean axisY) {
    	this.axisY = axisY;
    }
    
    public void useAxisZ(boolean axisZ) {
    	this.axisZ = axisZ;
    }
    
    public static void setupAnim(LivingEntity entity, BoobJigglePhysics leftJiggle, BoobJigglePhysics rightJiggle,
    		ModelPart boobParent, ModelPart leftBoob, ModelPart rightBoob) {
        
        float deltaTime = 0.05f;
        boolean axisY = leftJiggle.axisY && rightJiggle.axisY;
        boolean axisZ = leftJiggle.axisZ && rightJiggle.axisZ;
        
        leftJiggle.update(
            (float) entity.getY(), 
            entity.getX(), 
            entity.getZ(), 
            deltaTime, 
            entity.walkAnimation.isMoving(),
            entity.onGround()
        );
        
        rightJiggle.update(
            (float) entity.getY(), 
            entity.getX(), 
            entity.getZ(),
            deltaTime, 
            entity.walkAnimation.isMoving(),
            entity.onGround()
        );
        
        // Calculate average offset for parent bone
        float avgOffset = (leftJiggle.getOffset() + rightJiggle.getOffset()) * 0.5f;
        boobParent.y = leftJiggle.config.originalYPos + avgOffset;
        
        // Apply averaged rotations to parent bone

        boobParent.xRot = (leftJiggle.getRotationX() + rightJiggle.getRotationX()) * 0.5f;
        leftBoob.y = leftJiggle.getOffset() * 0.4f;
        leftBoob.xRot = leftJiggle.getRotationX() * 0.5f;
        rightBoob.y = rightJiggle.getOffset() * 0.4f;
        rightBoob.xRot = rightJiggle.getRotationX() * 0.5f;
               
        if (axisY) {
            boobParent.yRot = (leftJiggle.getRotationY() + rightJiggle.getRotationY()) * 0.5f;
            leftBoob.yRot = leftJiggle.getRotationY() * 0.6f;
            rightBoob.yRot = rightJiggle.getRotationY() * 0.6f;
        }      
        if (axisZ) {
            boobParent.zRot = (leftJiggle.getRotationZ() + rightJiggle.getRotationZ()) * 0.5f;
            leftBoob.zRot = leftJiggle.getRotationZ() * 0.7f;
            rightBoob.zRot = rightJiggle.getRotationZ() * 0.7f;
        }
    }
      
    @OnlyIn(Dist.CLIENT)
    public static class Builder {
    	private float springStrength = 1.5f;
    	private float damping = 0.5f;
    	private float gravity = 0.02f;
    	private float maxDisplacement = 0.3f;
    	private float originalYPos = 2.0f;
    	
        private float rotationSpringY = 0.25f;
        private float rotationSpringX = 0.23f;
        private float rotationSpringZ = 0.27f;
        private float rotationDamping = 0.88f; 
        private final float rotationReturnSpring = 0.35f; 
        private float maxRotationY = 0.15f;	
        private float maxRotationX = 0.12f;
        private float maxRotationZ = 0.18f;
        
        private float movementMultiplier = 0.5f;
        private float movementDecay = 0.9f;
        private float oscillationSpeed = 0.15f;
        
        private float phaseInfluence = 0.20f;
        private float sideInfluence = 0.15f;
        private float asymetricDelay = 0.08f;
        
        private float jumpElevationMin = 0.4f;
        private float jumpElevationMax = 0.8f;
        private float jumpElevationSpring = 0.15f;
        private float jumpElevationDamping = 0.85f;
            
        private final float phaseOffset;
        private final float sideMultiplier;
        private final float asymmetricFrequency;
            
        public Builder(float phaseOffset, boolean isLeft) {
        	this.phaseOffset = phaseOffset;
            this.sideMultiplier = isLeft ? -1.0f : 1.0f;
            this.asymmetricFrequency = isLeft ? 1.0f : 1.25f;
        }
        
    	public BoobJigglePhysics build() {
    		return new BoobJigglePhysics(this);
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
    	
    	public Builder oscillationSpeed(float oscillationSpeed) {
    		this.oscillationSpeed = oscillationSpeed;
    		return this;
    	}

    	public Builder maxDisplacement(float maxDisplacement) {
    		this.maxDisplacement = maxDisplacement;
    		return this;
    	}
    	
        public Builder rotationSpringY(float rotationSpringY) {
        	this.rotationSpringY = rotationSpringY;
        	return this;
        }
        
        public Builder rotationSpringX(float rotationSpringX) {
        	this.rotationSpringX = rotationSpringX;
        	return this;
        }
        
        public Builder rotationSpringZ(float rotationSpringZ) {
        	this.rotationSpringZ = rotationSpringZ;
        	return this;
        }
        
        public Builder rotationDamping(float rotationDamping) {
        	this.rotationDamping = rotationDamping;
        	return this;
        }
        
        public Builder maxRotationY(float maxRotationY) {
        	this.maxRotationY = maxRotationY;
        	return this;
        }
        
        public Builder maxRotationX(float maxRotationX) {
        	this.maxRotationX = maxRotationX;
        	return this;
        }
        
        public Builder maxRotationZ(float maxRotationZ) {
        	this.maxRotationZ = maxRotationZ;
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
        
        public Builder phaseInfluence(float phaseInfluence) {
        	this.phaseInfluence = phaseInfluence;
        	return this;
        } 
        
        public Builder sideInfluence(float sideInfluence) {
        	this.sideInfluence = sideInfluence;
        	return this;
        } 
        
        public Builder asymetricDelay(float asymetricDelay) {
        	this.asymetricDelay = asymetricDelay;
        	return this;
        } 
        
        public Builder jumpElevationMin(float jumpElevationMin) {
			this.jumpElevationMin = jumpElevationMin;
			return this;
		}
        
        public Builder jumpElevationMax(float jumpElevationMax) {			
        	this.jumpElevationMax = jumpElevationMax;
        	return this;
        }
        
        public Builder jumpElevationSpring(float jumpElevationSpring) {
        	this.jumpElevationSpring = jumpElevationSpring;
        	return this;
        }
        
        public Builder jumpElevationDamping(float jumpElevationDamping) {
			this.jumpElevationDamping = jumpElevationDamping;
			return this;
		}   
    }
    
    @OnlyIn(Dist.CLIENT)
    public static class JigglePhysicsConfig extends AbstractJigglePhysics.AbstractJigglePhysicsConfig {
        public final float rotationSpringY;
        public final float rotationSpringX;
        public final float rotationSpringZ;
        public final float rotationDamping;
        public final float rotationReturnSpring;
        public final float maxRotationY;
        public final float maxRotationX;
        public final float maxRotationZ;
        public final float movementMultiplier;
        public final float movementDecay;
        public final float oscillationSpeed;
        public final float phaseInfluence;
        public final float sideInfluence;
        public final float asymetricDelay;
        public final float phaseOffset;
        public final float sideMultiplier;
        public final float asymmetricFrequency;
        public final float jumpElevationMin;
        public final float jumpElevationMax;
        public final float jumpElevationSpring;
        public final float jumpElevationDamping;

        public JigglePhysicsConfig(Builder builder) {
            super(builder.springStrength, builder.damping, builder.gravity, builder.maxDisplacement, builder.originalYPos);
            this.rotationSpringY = builder.rotationSpringY;
            this.rotationSpringX = builder.rotationSpringX;
            this.rotationSpringZ = builder.rotationSpringZ;
            this.rotationDamping = builder.rotationDamping;
            this.rotationReturnSpring = builder.rotationReturnSpring;
            this.maxRotationY = builder.maxRotationY;
            this.maxRotationX = builder.maxRotationX;
            this.maxRotationZ = builder.maxRotationZ;
            this.movementMultiplier = builder.movementMultiplier;
            this.movementDecay = builder.movementDecay;
            this.oscillationSpeed = builder.oscillationSpeed;
            this.phaseInfluence = builder.phaseInfluence;
            this.sideInfluence = builder.sideInfluence;
            this.asymetricDelay = builder.asymetricDelay;
            this.phaseOffset = builder.phaseOffset;
            this.sideMultiplier = builder.sideMultiplier;
            this.asymmetricFrequency = builder.asymmetricFrequency;
            this.jumpElevationMin = builder.jumpElevationMin;
            this.jumpElevationMax = builder.jumpElevationMax;
            this.jumpElevationSpring = builder.jumpElevationSpring;
            this.jumpElevationDamping = builder.jumpElevationDamping;
        }
    }
}