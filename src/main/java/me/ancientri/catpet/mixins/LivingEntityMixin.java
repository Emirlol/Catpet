package me.ancientri.catpet.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariants;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@WrapOperation(method = "tickCramming", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;pushAway(Lnet/minecraft/entity/Entity;)V"))
	private void catpet$pushAway(LivingEntity instance, Entity entity, Operation<Void> original) {
		if (instance instanceof AbstractHorseEntity horse && entity instanceof CatEntity cat && !cat.getEntityWorld().isClient() && horse.hasPlayerRider()) {
			var pos = cat.getBlockPos();
			var world = cat.getEntityWorld();

			if (!world.isAir(pos)) world.setBlockState(pos, getCarpet(cat), Block.NOTIFY_ALL);

			world.playSound(cat, pos, SoundEvents.UI_HUD_BUBBLE_POP, cat.getSoundCategory(), 1.0F, 1.0F);
			cat.kill((ServerWorld) world);
		} else original.call(instance, entity);
	}

	@Unique
	private BlockState getCarpet(CatEntity cat) {
		var entry = cat.getVariant();
		Block block;
		// @formatter:off
		if (entry.matchesKey(CatVariants.BLACK))                  block = Blocks.BLACK_CARPET;
		else if (entry.matchesKey(CatVariants.BRITISH_SHORTHAIR)) block = Blocks.LIGHT_GRAY_CARPET;
		else if (entry.matchesKey(CatVariants.CALICO))            block = Blocks.YELLOW_CARPET;
		else if (entry.matchesKey(CatVariants.JELLIE))            block = Blocks.GRAY_CARPET;
		else if (entry.matchesKey(CatVariants.PERSIAN))           block = Blocks.YELLOW_CARPET;
		else if (entry.matchesKey(CatVariants.RAGDOLL))           block = Blocks.WHITE_CARPET;
		else if (entry.matchesKey(CatVariants.RED))               block = Blocks.ORANGE_CARPET;
		else if (entry.matchesKey(CatVariants.SIAMESE))           block = Blocks.GRAY_CARPET;
		else if (entry.matchesKey(CatVariants.ALL_BLACK))         block = Blocks.BLACK_CARPET;
		else if (entry.matchesKey(CatVariants.TABBY))             block = Blocks.BROWN_CARPET;
		else if (entry.matchesKey(CatVariants.WHITE))             block = Blocks.WHITE_CARPET;
		else                                                      block = Blocks.WHITE_CARPET;
		// @formatter:on
		return block.getDefaultState();
	}
}
