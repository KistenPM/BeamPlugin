package org.kisten.beam2.Listeners;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
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
    public void onClick(PlayerInteractEvent event) {

        //рейтрейс и добавление дамага
        Player p = event.getPlayer();
        ItemStack item = p.getEquipment().getItemInMainHand();
        assert  item.getItemMeta() != null;
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            RayTraceResult res = p.rayTraceEntities(7, false);
            LivingEntity ent = (LivingEntity) res.getHitEntity();
            assert ent != null;
            ent.damage(20);
        }


        if (item.getItemMeta().hasLore()) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) && ((item.getType() == Material.BLAZE_ROD) || (item.getType() == Material.AMETHYST_SHARD)) && !p.getActivePotionEffects().contains(PotionEffectType.DARKNESS)) {
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
                p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 200, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                new BukkitRunnable() {
                    int amount = 0;
                    @Override
                    public void run() {
                        amount++;
                        p.playSound(p.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 3.0F, 0.5F);
                        if (amount >= 8) {
                            cancel();
                        }
                    }
                }.runTaskTimer(Beam2.getInstance(), 0, 20);
            }
        }
    }
}
