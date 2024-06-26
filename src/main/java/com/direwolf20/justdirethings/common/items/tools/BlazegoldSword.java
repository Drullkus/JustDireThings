package com.direwolf20.justdirethings.common.items.tools;

import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.Helpers;
import com.direwolf20.justdirethings.common.items.tools.basetools.BaseSword;
import com.direwolf20.justdirethings.common.items.tools.utils.GooTier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class BlazegoldSword extends BaseSword {
    public BlazegoldSword() {
        super(GooTier.BLAZEGOLD, 3, -2.0F, new Properties().fireResistant());
        registerAbility(Ability.MOBSCANNER);
        registerAbility(Ability.LAVAREPAIR);
        registerAbility(Ability.CAUTERIZEWOUNDS);
        registerAbility(Ability.SMOKER);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (canUseAbility(stack, Ability.LAVAREPAIR))
            return Helpers.doLavaRepair(stack, entity);
        return false;
    }
}
