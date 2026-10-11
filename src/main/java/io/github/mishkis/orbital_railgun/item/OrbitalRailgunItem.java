package io.github.mishkis.orbital_railgun.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class OrbitalRailgunItem extends Item {
    public OrbitalRailgunItem(Item.Properties properties) {
        super(properties.rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 24000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) {
            return ItemUtils.startUsingInstantly(level, player, hand);
        }

        return InteractionResult.FAIL;
    }

    public void shoot(Player player) {
        player.getCooldowns().addCooldown(this.getDefaultInstance(), 2400);
    }
}
