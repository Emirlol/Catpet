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
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.Registries;
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
			world.setBlockState(pos, getCarpet(cat), Block.NOTIFY_ALL);
			world.playSound(cat, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, cat.getSoundCategory(), 1.0F, 1.0F);
			cat.kill();
		} else original.call(instance, entity);
	}

	@Unique
	private BlockState getCarpet(CatEntity cat) {
		var entry = Registries.CAT_VARIANT.getEntry(cat.getVariant());
		Block block;
		// @formatter:off
		if (entry.matchesKey(CatVariant.BLACK))                  block = Blocks.BLACK_CARPET;
		else if (entry.matchesKey(CatVariant.BRITISH_SHORTHAIR)) block = Blocks.LIGHT_GRAY_CARPET;
		else if (entry.matchesKey(CatVariant.CALICO))            block = Blocks.YELLOW_CARPET;
		else if (entry.matchesKey(CatVariant.JELLIE))            block = Blocks.GRAY_CARPET;
		else if (entry.matchesKey(CatVariant.PERSIAN))           block = Blocks.YELLOW_CARPET;
		else if (entry.matchesKey(CatVariant.RAGDOLL))           block = Blocks.WHITE_CARPET;
		else if (entry.matchesKey(CatVariant.RED))               block = Blocks.ORANGE_CARPET;
		else if (entry.matchesKey(CatVariant.SIAMESE))           block = Blocks.GRAY_CARPET;
		else if (entry.matchesKey(CatVariant.ALL_BLACK))         block = Blocks.BLACK_CARPET;
		else if (entry.matchesKey(CatVariant.TABBY))             block = Blocks.BROWN_CARPET;
		else if (entry.matchesKey(CatVariant.WHITE))             block = Blocks.WHITE_CARPET;
		else                                                     block = Blocks.WHITE_CARPET;
		// @formatter:on
		return block.getDefaultState();
	}
}
