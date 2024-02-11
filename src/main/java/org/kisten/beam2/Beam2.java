package org.kisten.beam2;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class Beam2 extends JavaPlugin {

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
                Location playerLocation = player.getEyeLocation();
                // Создаем лазер в направлении координаты
                Location start = new Location(playerLocation.getWorld(), 100.0, 100.0, 100);
                createLaser(start, playerLocation);

                return true;
            }
        }
        return false;
    }

    private void createLaser(Location startLocation, Location PlayerLoc) {
        Location laserLocation = startLocation.clone();
        Location Destination = new Location(laserLocation.getWorld(), -1, 100, -1);

        double x1 = startLocation.getX(), y1 = startLocation.getZ(), x2 = Destination.getX(), y2 = Destination.getZ(), x3 = PlayerLoc.getX(), y3 = PlayerLoc.getZ();
        final double v = ((x1 - x2) *
                (x1 - x2)) + ((y1 - y2) * (y1 - y2));
        double x = ((((x1 * x1 * x3) - (2 * x1 * x2 * x3)) + (x2 * x2 * x3) + (x2 *
                (y1 - y2) * (y1 - y3))) - (x1 * (y1 - y2) * (y2 - y3))) / v;
        double z = (x2 * x2 * y1 + x1 * x1 * y2 + x2 * x3 * (y2 - y1) - x1 *
                (x3 * (y2 - y1) + x2 * (y1 + y2)) + (y1 - y2) * (y1 - y2) * y3) / v;

        Location perp = new Location(laserLocation.getWorld(), x, 0, z);
        Location PlayerXZ = new Location(laserLocation.getWorld(), PlayerLoc.getX(), 0, PlayerLoc.getZ());
        double PerpLength = PlayerXZ.distance(perp);
        double leg;
        Vector ToEdge;
        Location Edge;

        if (PerpLength <= 15) {
            leg = Math.sqrt(Math.pow(15, 2) - Math.pow(PerpLength, 2));
            ToEdge = new Vector(laserLocation.getX() - perp.getX(), 0, laserLocation.getZ() - perp.getZ()).normalize().multiply(leg);
            Edge = perp.clone().add(ToEdge);
            perp.setY(laserLocation.getY());
            Edge.setY(laserLocation.getY());
        }
        else {
            return;

        }
        Vector travel = new Vector(perp.getX() - Edge.getX(), 0, perp.getZ() - Edge.getZ());
        double numTrips = travel.length() / 0.7;
        travel.divide(new Vector(numTrips, numTrips, numTrips));
        new BukkitRunnable() {
            int distance = 0;
            @Override
            public void run() {
                // Увеличиваем дистанцию на 1
                distance++;
                // Получаем следующую локацию лазера
                if (startLocation.distance(perp) > startLocation.distance(Destination)) { // после точки назначения
                    if (distance == 1) {
                        Edge.add(ToEdge.multiply(-2));
                        travel.multiply(-1);
                    }
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge).offset(.25, .25, .25).extra(0.1).color(Color.RED).count(4).spawn();
                    Edge.add(travel);
                }
                if (perp.distance(Destination) - startLocation.distance(Destination) > 0) { // перед точкой начала
                    if (distance == 1) {
                        Edge.add(ToEdge.multiply(-2));
                        travel.multiply(-1);
                    }
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge).offset(.25, .25, .25).extra(0.1).color(Color.RED).count(4).spawn();
                    Edge.add(travel);
                }
                if (1 >= Edge.distance(Destination)) { // при точке назначения
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge).offset(.5, .5, .5).extra(0.1).color(Color.ORANGE).count(20).spawn();
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge).offset(.5, .5, .5).extra(0.1).color(Color.YELLOW).count(40).spawn();
                    cancel();
                }
                else { // между точкой начала и концом
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge).offset(.25, .25, .25).extra(0.1).color(Color.RED).count(4).spawn();
                    Edge.add(travel);
                }
                // Устанавливаем блоку материал лазера
                // block.setType(Material.REDSTONE_BLOCK);
                // Удаляем блок спустя 60 тиков (3 секунды)
                // Если достигнута максимальная дистанция лазера, останавливаем задачу
                if (distance >= numTrips * 2.5) {
                    cancel();
                }
            }
        }.runTaskTimer(getPlugin(), 0, 1);
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
