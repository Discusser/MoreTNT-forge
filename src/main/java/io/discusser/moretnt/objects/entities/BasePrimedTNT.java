package io.discusser.moretnt.objects.entities;

import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class BasePrimedTNT extends PrimedTnt implements IPrimedTNT {
    public static final float DEFAULT_SIZE = 4.0F;
    public static final boolean DEFAULT_FIRE = true;
    public float size = DEFAULT_SIZE;
    public boolean fire = DEFAULT_FIRE;

    public BasePrimedTNT(EntityType<? extends PrimedTnt> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    // Override with your own EntityType
    public BasePrimedTNT(Level pLevel, double pX, double pY, double pZ, float size, boolean fire) {
        this(EntityType.TNT, pLevel);
        this.setPos(pX, pY, pZ);
        double d0 = pLevel.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
        this.size = size;
        this.fire = fire;
    }

    // Override PrimedTnt constructor for compatibility
    public BasePrimedTNT(Level pLevel, double pX, double pY, double pZ, @Nullable LivingEntity pOwner) {
        this(pLevel, pX, pY, pZ, DEFAULT_SIZE, DEFAULT_FIRE);
    }

    // Override with your own Explosion class
//    public Explosion createExplosion(double x, double y, double z) {
//        return new Explosion(this.level, null, null, null, x, y, z, this.size, this.fire,
//                Explosion.BlockInteraction.BREAK);
//    }

    @Override
    protected void explode() {
        double pX = this.getX();
        double pY = this.getY(0.0625);
        double pZ = this.getZ();

        Explosion explosion = this.createExplosion(pX, pY, pZ);
        explosion.explode();
        explosion.finalizeExplosion(true);

        if (this.level instanceof ServerLevel level) {
            for(ServerPlayer serverplayer : level.players()) {
                if (serverplayer.distanceToSqr(pX, pY, pZ) < 4096.0D) {
                    serverplayer.connection.send(new ClientboundExplodePacket(pX, pY, pZ, this.size, explosion.getToBlow(),
                            explosion.getHitPlayers().get(serverplayer)));
                }
            }
        }
    }
}
