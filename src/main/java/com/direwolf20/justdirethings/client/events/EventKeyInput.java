package com.direwolf20.justdirethings.client.events;

import com.direwolf20.justdirethings.JustDireThings;
import com.direwolf20.justdirethings.client.KeyBindings;
import com.direwolf20.justdirethings.common.items.interfaces.Ability;
import com.direwolf20.justdirethings.common.items.interfaces.LeftClickableTool;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableItem;
import com.direwolf20.justdirethings.common.items.interfaces.ToggleableTool;
import com.direwolf20.justdirethings.common.network.data.LeftClickPayload;
import com.direwolf20.justdirethings.common.network.data.ToggleToolPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;

import static com.direwolf20.justdirethings.util.MiscTools.getHitResult;

@Mod.EventBusSubscriber(modid = JustDireThings.MODID, value = Dist.CLIENT)
public class EventKeyInput {

    @SubscribeEvent
    public static void handleEventInput(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.phase == TickEvent.Phase.START)
            return;

        ItemStack toggleableItem = ToggleableItem.getToggleableItem(mc.player);
        if (!toggleableItem.isEmpty()) {
            if (KeyBindings.toggleTool.consumeClick()) {
                PacketDistributor.SERVER.noArg().send(new ToggleToolPayload("enabled"));
            }
        }
    }

    // Handling key presses
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null)
            return;
        Player player = mc.player;
        if (event.getAction() == InputConstants.PRESS) {
            for (int i = 0; i < mc.player.getInventory().items.size(); i++) {
                ItemStack itemStack = mc.player.getInventory().getItem(i);
                if (itemStack.getItem() instanceof ToggleableTool toggleableTool && itemStack.getItem() instanceof LeftClickableTool) {
                    activateAbilities(itemStack, event.getKey(), toggleableTool, player, i, false);
                }
            }
        }
    }

    // Handling mouse clicks
    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null || event.getButton() == 0 || event.getButton() == 1 || event.getAction() != InputConstants.PRESS)
            return;
        Player player = mc.player;
        for (int i = 0; i < mc.player.getInventory().items.size(); i++) {
            ItemStack itemStack = mc.player.getInventory().getItem(i);
            if (itemStack.getItem() instanceof ToggleableTool toggleableTool && itemStack.getItem() instanceof LeftClickableTool) {
                activateAbilities(itemStack, event.getButton(), toggleableTool, player, i, true);
            }
        }
    }

    private static void activateAbilities(ItemStack itemStack, int key, ToggleableTool toggleableTool, Player player, int invSlot, boolean isMouse) {
        Set<Ability> abilities = LeftClickableTool.getCustomBindingList(itemStack, new LeftClickableTool.Binding(key, isMouse));
        if (!abilities.isEmpty()) {
            //Do them client side and Server side, since some abilities (like ore scanner) are client side activated.
            toggleableTool.useAbility(player.level(), player, itemStack, key, isMouse);
            BlockHitResult blockHitResult = getHitResult(player);
            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                UseOnContext useoncontext = new UseOnContext(player.level(), player, InteractionHand.MAIN_HAND, itemStack, blockHitResult);
                toggleableTool.useOnAbility(useoncontext, itemStack, key, isMouse);
            }
            PacketDistributor.SERVER.noArg().send(new LeftClickPayload(0, false, BlockPos.ZERO, -1, invSlot, key, isMouse)); //Type 0 == air
        }
    }
}
