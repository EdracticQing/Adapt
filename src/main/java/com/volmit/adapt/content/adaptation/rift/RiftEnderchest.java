package com.volmit.adapt.content.adaptation.rift;

import com.volmit.adapt.api.adaptation.SimpleAdaptation;
import com.volmit.adapt.nms.NMS;
import com.volmit.adapt.util.C;
import com.volmit.adapt.util.Element;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class RiftEnderchest extends SimpleAdaptation<RiftEnderchest.Config> {
    public RiftEnderchest() {
        super("rift-enderchest");
        setDescription("Open an enderchest by clicking");
        setIcon(Material.ENDER_CHEST);
        setBaseCost(0);
        setCostFactor(0);
        setMaxLevel(1);
        setInitialCost(10);
        setInterval(9248);
        registerConfiguration(Config.class);
    }

    @Override
    public void addStats(int level, Element v) {
        v.addLore(C.ITALIC + "*Click an Enderchest in your hand to open (Just dont place)*");
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        if (!hasAdaptation(e.getPlayer())) {
            return;
        }
        Player p = e.getPlayer();
        ItemStack hand = p.getInventory().getItemInMainHand();
        if (p.hasCooldown(hand.getType())) {
            e.setCancelled(true);
            return;
        } else {
            NMS.get().sendCooldown(p, Material.ENDER_PEARL, 100);
            p.setCooldown(Material.ENDER_PEARL, 100);
        }
        if (getLevel(e.getPlayer()) > 0) {
            if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ENDER_CHEST)
                    && (e.getAction().equals(Action.RIGHT_CLICK_AIR)
                    || e.getAction().equals(Action.LEFT_CLICK_AIR)
                    || e.getAction().equals(Action.LEFT_CLICK_BLOCK))) {

                if (getPlayer(p).getData().getSkillLines().get("rift").getAdaptations().get("rift-resist").getLevel() > 0) {
                    RiftResist.riftResistStackAdd(p, 10, 2);
                }
                p.openInventory(e.getPlayer().getEnderChest());


            }
        }
    }

    @Override
    public void onTick() {

    }

    @Override
    public boolean isEnabled() {
        return getConfig().enabled;
    }

    @NoArgsConstructor
    protected static class Config {
        boolean enabled = true;
    }
}