package org.kisten.beam2;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Beam2 extends JavaPlugin {

        @Override
        public void onEnable() {
            // Плагин включен
            getLogger().info("Плагин Laser включен!");
        }

        @Override
        public void onDisable() {
            // Плагин выключен
            getLogger().info("Плагин Laser выключен!");
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
            if (command.getName().equalsIgnoreCase("laser")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;

                    // Получаем текущую локацию игрока
                    Location playerLocation = player.getLocation();

                    // Получаем направление взгляда игрока
                    Vector direction = playerLocation.getDirection();

                    // Создаем лазер в направлении координаты
                    createLaser(playerLocation, direction);

                    return true;
                }
            }

            return false;
        }

        private void createLaser(Location startLocation, Vector direction) {
            World world = startLocation.getWorld();
            Location laserLocation = startLocation.clone();

            // Создаем лазер из блоков
            new BukkitRunnable() {
                int distance = 0;

                @Override
                public void run() {
                    // Увеличиваем дистанцию на 1
                    distance++;

                    // Получаем следующую локацию лазера
                    laserLocation.add(direction);

                    // Получаем блок в текущей локации лазера
                    Block block = world.getBlockAt(laserLocation);

                    /* Замена с блоков на партиклы
                    Particle particle = world.spawnParticle(Particle.REDSTONE, laserLocation);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            particle.wait(60);
                        }
                    }.runTaskLater(getPlugin(), 60);
                     */

                    // Устанавливаем блоку материал лазера
                    block.setType(Material.REDSTONE_BLOCK);

                    // Удаляем блок спустя 60 тиков (3 секунды)
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            block.setType(Material.AIR);
                        }
                    }.runTaskLater(getPlugin(), 60);

                    // Если достигнута максимальная дистанция лазера, останавливаем задачу
                    if (distance >= 15) {
                        cancel();
                    }
                }
            }.runTaskTimer(getPlugin(), 0, 3);
        }

        private Beam2 getPlugin() {
            return this;
        }
    }
/* Старый код по Matyu
    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    private static final Beam2 instance = new Beam2(); //Task
    private Beam2() {
    }

    @Override
    public void run() {
        int length = 3;
        double particleDistance = 0.5;

        for (Player online : Bukkit.getOnlinePlayers()) {
            ItemStack hand = online.getItemInHand();

            if (hand.hasItemMeta() && hand.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Laser Pointer")) {
                Location location = online.getLocation().add(0, 1,0);

                for (double waypoint = 1; waypoint < length; waypoint += particleDistance) {
                    Vector vector = location.getDirection().multiply(waypoint);
                    location.add(vector);

                    if (location.getBlock().getType() != Material.AIR)
                        break;

                    location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(Color.YELLOW, 0.75F));
                }
            }
        }
    }

    public static Beam2 getInstance() {
        return instance;
    }

    public class stick implements Listener { //TaskListener
        @EventHandler
        public void stick2(PlayerInteractEvent event) {
            if (event.getHand() != EquipmentSlot.HAND || event.getAction() == Action.RIGHT_CLICK_AIR)
                return;

            Player player = event.getPlayer();
            ItemStack hand = player.getItemInHand();
            int distance = 30;

            if (hand.hasItemMeta() && hand.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Laser Pointer"))
                RayTraceResult result = player.rayTraceBlocks(distance);

            if (result != null && result.getHitBlock() != null && result.getHitBlock(),isSolid())
                player.getWorld().createExplosion(result.getHitBlock().getlocation(), 2F, true);
            else
                player.sendMessage(ChatColor.LIGHT_PURPLE + "[Laser]" + ChatColor.WHITE + "Target is too far or not solid!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
 */
