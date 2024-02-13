package org.kisten.beam2.Listeners;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.kisten.beam2.Glow;
import org.kisten.beam2.Laser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static net.kyori.adventure.text.Component.text;

public class onClickSpawnBeam implements Listener {
    @EventHandler
    public void takingDrugs(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = p.getEquipment().getItemInMainHand();
        if (event.getAction() == Action.RIGHT_CLICK_AIR && item.getType() == Material.IRON_NUGGET) {
            Component palka = text("Веди меня палочка!").color(NamedTextColor.DARK_PURPLE);
            if (item.getItemMeta().lore().equals(palka)){
                Location start = new Location(p.getLocation().getWorld(), 100.0, 100.0, 100);
                Location destination = new Location(p.getLocation().getWorld(), 0.0, 100.0, 0);
                new Laser().laser(start, destination, p.getLocation(), true);
            } else if (item.getItemMeta().hasEnchant(Objects.requireNonNull(Enchantment.getByKey(new NamespacedKey("Glow", "glow"))))){
                Location start = new Location(p.getLocation().getWorld(), -10.0, 100.0, -10);
                Location destination = new Location(p.getLocation().getWorld(), -100.0, 100.0, -100.0);
                new Laser().laser(start, destination, p.getLocation(), true);
            }
        }
    }
}
