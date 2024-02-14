package org.kisten.beam2;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.TextColor.color;

public class TimeGetter {
    //Функция для таймера, она обновляет его и когда таймер равен 0 - запускает функцию
    public TimeGetter() {
        World world = Bukkit.getWorld("world");
        System.out.println(world);
        assert world != null;
        @NotNull Collection<Entity> displays = world.getEntitiesByClasses(TextDisplay.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                LocalDateTime targetDateTime = LocalDateTime.of(2024, 2, 14, 16, 30, 0);
                long remainingSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(ZoneOffset.UTC), targetDateTime);
                System.out.println(remainingSeconds);
                while (true) {
                    remainingSeconds = remainingSeconds - 1;
                    if (remainingSeconds > 0) {
                        long remainingMinutes = (remainingSeconds / 60);
                        int remainingHours = (int) remainingMinutes / 60;
                        int remainingDays = remainingHours / 24;
                        String remainingHoursCleared = String.valueOf(remainingHours - remainingDays * 24);
                        String remainingMinutesCleared = String.valueOf(remainingMinutes - remainingHours * 60);
                        String remainingSecondsCleared = String.valueOf(remainingSeconds - remainingMinutes * 60L);
                        final Component component = text()
                                .content(String.valueOf(remainingDays)).color(color(GOLD))
                                .append(text(":", GRAY))
                                .append(text(remainingHoursCleared, GOLD))
                                .append(text(":", GRAY))
                                .append(text(remainingMinutesCleared, GOLD))
                                .append(text(":", GRAY))
                                .append(text(remainingSecondsCleared, GOLD))
                                .build();
                        // System.out.println("Время до " + targetDateTime.toString() + ": " + remainingDays + ":" + remainingHoursCleared + ":" + remainingMinutesCleared + ":" + remainingSecondsCleared);
                        for (Entity entity : displays) {
                            if (entity instanceof TextDisplay) {
                                ((TextDisplay) entity).text(component);
                            }
                        }
                    } else {

                        //Запуск функции когда время равно 0 и заменяет текст на 0:0:0:0
                        final Component component0 = text()
                                .content("0").color(color(GOLD))
                                .append(text(":", GRAY))
                                .append(text("0", GOLD))
                                .append(text(":", GRAY))
                                .append(text("0", GOLD))
                                .append(text(":", GRAY))
                                .append(text("0", GOLD))
                                .build();
                        for (Entity entity : displays) {
                            if (entity instanceof TextDisplay) {
                                ((TextDisplay) entity).text(component0);
                            }
                        }
                        Location start = new Location(world, 223.3, 100.0, 139.2);
                        Location destination = new Location(world, 100.0, 100.0, 100.0);
                        Location p = new Location(world, 0, 100000, 0);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new Laser().laser(start, destination, p, false);
                            }
                        }.runTaskTimer(Beam2.getInstance(), 0, 60);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(Beam2.getInstance());
    }
}
