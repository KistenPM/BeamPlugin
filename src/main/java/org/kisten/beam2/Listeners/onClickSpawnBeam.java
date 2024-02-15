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


        Player p = event.getPlayer(); // Получаем игрока
        ItemStack item = p.getEquipment().getItemInMainHand(); // Получаем предмет в руке игрока
        assert  item.getItemMeta() != null; // Есть ли у предмета айтем мета

        //рейтрейс и добавление дамага
//        if (event.getAction() == Action.LEFT_CLICK_AIR) {
//            RayTraceResult res = p.rayTraceEntities(7, false); // проверяем энтити на 7 блоков от куосора игрока, не игнорируя блоки
//            assert res != null;
//            LivingEntity ent = (LivingEntity) res.getHitEntity(); // получаем какой энтити соприкоснулся с лучом, и превращаем его в LivingEntity
//            assert ent != null;
//            ent.damage(20); // накладываем дамаг
//        }

        if (item.getItemMeta().hasLore()) { //Проверяем есть ли лор у предмета
            // проверяем какой клик совершил игрок и по чему он кликнул (в данном случае воздух), проверяем какой предмет в руке у игрока, проверяем есть ли эффект даркнесса на игроке
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) && ((item.getType() == Material.BLAZE_ROD) || (item.getType() == Material.AMETHYST_SHARD)) && !p.getActivePotionEffects().contains(PotionEffectType.DARKNESS)) {
                // Оглавление лоров предметов для дальнейнего их сравнения
                Component palka = text("Веди меня палочка!").color(NamedTextColor.BLUE);
                Component Magnet_palka = text("Приведи меня палочка..").color(NamedTextColor.BLUE);
                // Сравнение лоров
                if (Objects.equals(item.getItemMeta().lore(), palka)) {
                    // Оглавление координат Бима
                    Location start = new Location(p.getLocation().getWorld(), 100.0, 100.0, 100);
                    Location destination = new Location(p.getLocation().getWorld(), 0.0, 100.0, 0);
                    // Спавн бима
                    new Laser().laser(start, destination, p.getLocation(), true);
                    // Наложение эффектов
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 200, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                } else if (Objects.equals(item.getItemMeta().lore(), Magnet_palka)) {
                    Location start = new Location(p.getLocation().getWorld(), -10.0, 100.0, -10);
                    Location destination = new Location(p.getLocation().getWorld(), -100.0, 100.0, -100.0);
                    // Спавн бима
                    new Laser().laser(start, destination, p.getLocation(), true);
                    // Наложение эффектов
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 200, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                }

                //поток для наложения на игрока звука
                new BukkitRunnable() {
                    //кол-во проигрываний звука
                    int amount = 0;
                    @Override
                    public void run() {
                        // Добавляем 1 к кол-ву возпр-ий
                        amount++;
                        // Воспр. звуков
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
