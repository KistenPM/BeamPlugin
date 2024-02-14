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
        Vector toEnd, toStart;
        Vector fullVec = new Vector(Destination.getX() - laserLocation.getX(), 0, Destination.getZ() - laserLocation.getZ());

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
                toEnd = fullVec.clone().normalize().multiply(leg);
                toStart = fullVec.clone().normalize().multiply(leg * -1);
                perp.setY(laserLocation.getY());
                Edge = perp.clone().add(toStart);
                Edge2 = perp.clone().add(toEnd);
            } else if (PerpLength > 15) {
                Location PerpPoint = PlayerLoc.add(new Vector(x - PlayerLoc.getX(), 0, z - PlayerLoc.getZ()));
                PerpPoint.setY(startLocation.getY());
                new ParticleBuilder(Particle.REDSTONE).allPlayers().location(PerpPoint).offset(.1, .15, .1).extra(0.1).color(Color.YELLOW).count(20).spawn();
                return;
            } else {
                return;
            }
        } else {
            perp = laserLocation;
            Edge = laserLocation;
            Edge2 = Destination;

            toEnd = fullVec.clone().normalize();
            toStart = fullVec.clone().normalize();
        }
        perp.setY(laserLocation.getY());
        Edge.setY(laserLocation.getY());
        Edge2.setY(laserLocation.getY());
        Vector travel = new Vector(Edge2.getX() - Edge.getX(), 0, Edge2.getZ() - Edge.getZ());
        double numTrips = travel.length() / 0.5;
        travel = travel.divide(new Vector(numTrips, numTrips, numTrips));
        Vector finalTravel = travel;
        new BukkitRunnable() {
            int distance = 0;
            @Override
            public void run() {
                double numTrips1 = 0.0;
                Vector direction = new Vector(Destination.getX() - perp.getX(), 0, Destination.getZ() - perp.getZ()).normalize();
                // Увеличиваем дистанцию на 1
                distance++;
                // Получаем следующую локацию лазера
                if (!checkRadius) {
                    Vector travel1 = new Vector(destination.getX() - startLocation.getX(), 0, destination.getZ() - startLocation.getZ());
                    numTrips1 = travel1.length() / 0.5;
                    travel1.divide(new Vector(numTrips1, numTrips1, numTrips1));
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(laserLocation).offset(.25, .25, .25).extra(0.1).color(Color.RED).count(4).spawn();
                    if (1 >= laserLocation.distance(Destination)) { // при точке назначения
                        new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Destination).offset(.5, .5, .5).extra(0.1).color(Color.ORANGE).count(20).spawn();
                        new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Destination).offset(.5, .5, .5).extra(0.1).color(Color.YELLOW).count(40).spawn();
                        cancel();
                    }
                    laserLocation.add(travel1);
                }
                if (startLocation.distance(perp) > startLocation.distance(Destination) && direction.angle(toStart) == toStart.angle(direction) && checkRadius) { // после точки назначения
                    if (distance == 1) {
                        finalTravel.multiply(-1);
                    }
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge2).offset(.25, .25, .25).extra(0.1).color(Color.RED).count(4).spawn();
                    Edge2.add(finalTravel);
                }
                else { // между точкой начала и концом
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge).offset(.25, .25, .25).extra(0.1).color(Color.RED).count(4).spawn();
                    Edge.add(finalTravel);
                }
                if (1 >= Edge.distance(Destination)) { // при точке назначения
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge).offset(.5, .5, .5).extra(0.1).color(Color.ORANGE).count(20).spawn();
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge).offset(.5, .5, .5).extra(0.1).color(Color.YELLOW).count(40).spawn();
                    cancel();
                } else if (1 >= Edge2.distance(Destination)) {
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge2).offset(.5, .5, .5).extra(0.1).color(Color.ORANGE).count(20).spawn();
                    new ParticleBuilder(Particle.REDSTONE).allPlayers().location(Edge2).offset(.5, .5, .5).extra(0.1).color(Color.YELLOW).count(40).spawn();
                    cancel();
                }

                // Если достигнута максимальная дистанция лазера, останавливаем задачу
                if (distance >= numTrips && numTrips1 == 0.0) {
                    cancel();
                }
                else if (distance >= numTrips1) {
                    cancel();
                }
            }
        }.runTaskTimer(Beam2.getInstance(), 0, 1);
    }
}
