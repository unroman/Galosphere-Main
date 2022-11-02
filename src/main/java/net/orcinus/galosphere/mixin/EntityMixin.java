package net.orcinus.galosphere.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.orcinus.galosphere.init.GItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyVariable(at = @At("HEAD"), method = "turn", ordinal = 0, argsOnly = true)
    private double I$XRotate(double value) {
        return this.spectrePerspectiveValue(value);
    }

    @ModifyVariable(at = @At("HEAD"), method = "turn", ordinal = 1, argsOnly = true)
    private double I$YRotate(double value) {
        return this.spectrePerspectiveValue(value);
    }

    @Unique
    private double spectrePerspectiveValue(double value) {
        Entity $this = (Entity) (Object) this;
        boolean flag = $this instanceof Player player && this.isFirstPerspective() && player.getUseItem().is(GItems.SPECTRE_BOUND_SPYGLASS) && player.isUsingItem();
        return flag ? value * 8 : value;
    }

    @Environment(EnvType.CLIENT)
    private boolean isFirstPerspective() {
        return Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

}