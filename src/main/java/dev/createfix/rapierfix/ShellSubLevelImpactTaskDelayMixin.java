package dev.createfix.rapierfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Delays only CBC's Sable physical-projectile detonation task by one server tick.
 *
 * CBC keeps full ownership of the original operation: fuze evaluation, NBT,
 * world-coordinate conversion, block removal, projectile creation and detonation.
 * This mixin changes only the tick number passed to the existing TickTask.
 */
@Mixin(
        targets = "rbasamoyai.createbigcannons.compat.sable.ShellSubLevelImpactCallback",
        priority = 900,
        remap = false
)
public abstract class ShellSubLevelImpactTaskDelayMixin {

    @ModifyArg(
            method = "sable$onCollision(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lorg/joml/Vector3d;D)Ldev/ryanhcode/sable/api/physics/callback/BlockSubLevelCollisionCallback$CollisionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/TickTask;<init>(ILjava/lang/Runnable;)V",
                    remap = false
            ),
            index = 0,
            require = 1,
            expect = 1,
            allow = 1,
            remap = false
    )
    private int createfix$runPhysicalShellDetonationNextTick(int currentTick) {
        return currentTick == Integer.MAX_VALUE ? currentTick : currentTick + 1;
    }
}
