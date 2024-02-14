package org.kisten.beam2;


import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static org.apache.logging.log4j.LogManager.getLogger;

public class Laser {
    public void laser(Location startLocation, Location destination, Location PlayerLoc, Boolean checkRadius) {
        Location laserLocation = startLocation.clone();
        Location Destination = destination.clone();
        Location perp, Edge, Edge2;
        Vector ToEdge, ToEdge2;
        if (checkRadius) {
            double x1 = startLocation.getX(), y1 = startLocation.getZ(), x2 = Destination.getX(), y2 = Destination.getZ(), x3 = PlayerLoc.getX(), y3 = PlayerLoc.getZ();
            final double v = ((x1 - x2) *
                    (x1 - x2)) + ((y1 - y2) * (y1 - y2));
            double x = ((((x1 * x1 * x3) - (2 * x1 * x2 * x3)) + (x2 * x2 * x3) + (x2 *
                    (y1 - y2) * (y1 - y3))) - (x1 * (y1 - y2) * (y2 - y3))) / v;
            double z = (x2 * x2 * y1 + x1 * x1 * y2 + x2 * x3 * (y2 - y1) - x1 *
                    (x3 * (y2 - y1) + x2 * (y1 + y2)) + (y1 - y2) * (y1 - y2) * y3) / v;

            perp = new Location(laserLocation.getWorld(), x, 0, z);
            Location PlayerXZ = new Location(laserLocation.getWorld(), PlayerLoc.getX(), 0, PlayerLoc.getZ());
            double PerpLength = PlayerXZ.distance(perp);
            double leg;
            if (PerpLength <= 15) {
                leg = Math.sqrt(Math.pow(15, 2) - Math.pow(PerpLength, 2));
                ToEdge = new Vector(laserLocation.getX() - perp.getX(), 0, laserLocation.getZ() - perp.getZ()).normalize().multiply(leg);
                ToEdge2 = new Vector(Destination.getX() - perp.getX(), 0, Destination.getZ() - perp.getZ()).normalize().multiply(leg);
                perp.setY(laserLocation.getY());
                Edge = perp.clone().add(ToEdge);
            } else if (PerpLength > 15) {
                Location PerpPoint = PlayerLoc.add(new Vector(x - PlayerLoc.getX(), 0, z - PlayerLoc.getZ()));
                PerpPoint.setY(startLocation.getY());
                new ParticleBuilder(Particle.REDSTONE).allPlayers().location(PerpPoint).offset(.1, .15, .1).extra(0.1).color(Color.YELLOW).count(20).spawn();
                return;
            } else {
                return;
            }
        } else {
            perp = Destination;
            Edge = laserLocation;
            perp.setY(laserLocation.getY());
            Edge.setY(laserLocation.getY());
            ToEdge = new Vector(0, 0, 0);
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
                        travel.multiply(-1);
                        perp.subtract(ToEdge);
                    }
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(perp).offset(.25, .25, .25).extra(0.1).color(Color.RED).count(4).spawn();
                    perp.add(travel);
                }
                if (perp.distance(Destination) - startLocation.distance(Destination) > 0) { // перед точкой начала
                    if (distance == 1) {
                        travel.multiply(-1);
                        perp.subtract(ToEdge);
                    }
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(perp).offset(.25, .25, .25).extra(0.1).color(Color.RED).count(4).spawn();
                    perp.add(travel);
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
                if (distance >= numTrips) {
                    cancel();
                }
            }
        }.runTaskTimer(Beam2.getInstance(), 0, 1);
    }
}
