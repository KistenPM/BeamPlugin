package org.kisten.beam2.Listeners;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.kisten.beam2.Beam2;
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
        if (item.getItemMeta().hasLore()) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) && ((item.getType() == Material.BLAZE_ROD) || (item.getType() == Material.AMETHYST_SHARD)) && !p.getActivePotionEffects().contains(PotionEffectType.BLINDNESS)) {
                Component Name = text("Посох пути").color(NamedTextColor.AQUA);
                Component Magnet_Name = text("Намагниченный посох пути").color(NamedTextColor.DARK_PURPLE);
                if (Objects.equals(item.getItemMeta().displayName(), Name)) {
                    Location start = new Location(p.getLocation().getWorld(), 100.0, 100.0, 100);
                    Location destination = new Location(p.getLocation().getWorld(), 0.0, 100.0, 0);
                    new Laser().laser(start, destination, p.getLocation(), true);
                } else if (Objects.equals(item.getItemMeta().displayName(), Magnet_Name)) {
                    Location start = new Location(p.getLocation().getWorld(), -10.0, 100.0, -10);
                    Location destination = new Location(p.getLocation().getWorld(), -100.0, 100.0, -100.0);
                    new Laser().laser(start, destination, p.getLocation(), true);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
            }
        }
    }
}
