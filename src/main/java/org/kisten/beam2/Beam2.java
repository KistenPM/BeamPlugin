package org.kisten.beam2;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.kisten.beam2.Listeners.onClickSpawnBeam;
import org.kisten.beam2.Listeners.onJoin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.kyori.adventure.text.Component.text;

public final class Beam2 extends JavaPlugin {
    private static Beam2 instance;

    @Override
    public void onEnable() {
        // Плагин включен
        instance = this;
        // Регистр зачар
        registerGlow();
        // Регистр крафты
        crafts();
        // Регистр листенеры
        getServer().getPluginManager().registerEvents(new onClickSpawnBeam(), this);
        getServer().getPluginManager().registerEvents(new onJoin(), this);

        getLogger().info("Плагин Laser включен!");
    }

    @Override
    public void onDisable() {
        // Плагин выключен
        getLogger().info("Плагин Laser выключен!");
        // Отменяем все циклы
        Bukkit.getServer().getScheduler().cancelTasks(this);
    }

    // Получаем инстанс плагина чтобы использовать в циклах баккита в других классах
    public static Beam2 getInstance() {
        return instance;
    }

    // Регистриурем  энчант, хз как но да
    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Glow glow = new Glow();
            Enchantment.registerEnchantment(glow);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Создание крафтов
    public void crafts() {
        // Создание посоха пути
        Component palka = text("Веди меня палочка!").color(NamedTextColor.BLUE);
        Component Name = text("Посох пути").color(NamedTextColor.AQUA);
        ItemStack Wand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta Wand_meta = Wand.getItemMeta();
        Objects.requireNonNull(Wand_meta).displayName(Name);
        List Suka = new ArrayList<>();
        Suka.add(palka);
        Objects.requireNonNull(Wand_meta).lore(Suka);
        Wand.setItemMeta(Wand_meta);
        NamespacedKey Wand_key = new NamespacedKey(this, "WandToHeaven");
        ShapedRecipe Wand_recipe = new ShapedRecipe(Wand_key, Wand);
        Wand_recipe.shape("  B", " S ", "BE ");
        Wand_recipe.setIngredient('E', Material.END_STONE);
        Wand_recipe.setIngredient('S', Material.STICK);
        Wand_recipe.setIngredient('B', Material.BLAZE_ROD);
        Bukkit.addRecipe(Wand_recipe);

        // Создание бещенного магнита
        Component FCL = text("Куда же ты ведёшь..").color(NamedTextColor.GOLD);
        Component FCName = text("Бешенный компас").color(NamedTextColor.DARK_RED);
        ItemStack FC = new ItemStack(Material.COMPASS);
        ItemMeta FCmeta = FC.getItemMeta();
        CompassMeta Cmeta = (CompassMeta) FC.getItemMeta();
        Cmeta.setLodestone(new Location(Bukkit.getWorld("world_the_end"), 0, 0, 0));
        Objects.requireNonNull(Cmeta).displayName(FCName);
        List SukaFCL = new ArrayList<>();
        SukaFCL.add(FCL);
        Objects.requireNonNull(Cmeta).lore(SukaFCL);
        FC.setItemMeta(Cmeta);
        NamespacedKey FCKey = new NamespacedKey(this, "CompassToHell");
        SmithingTransformRecipe FCRecipe = new SmithingTransformRecipe(FCKey, FC, new RecipeChoice.MaterialChoice(Material.AIR), new RecipeChoice.MaterialChoice(Material.COMPASS), new RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT));
        Bukkit.addRecipe(FCRecipe);

        // Создание намагн. посоха пути
        Component Magnet_palka = text("Приведи меня палочка..").color(NamedTextColor.BLUE);
        Component Magnet_Name = text("Намагниченный посох пути").color(NamedTextColor.DARK_PURPLE);
        ItemStack Magnet_Wand = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta Magnet_Wand_meta = Magnet_Wand.getItemMeta();
        Glow glow = new Glow();
        Magnet_Wand_meta.addEnchant(glow, 1, true);
        Objects.requireNonNull(Magnet_Wand_meta).displayName(Magnet_Name);
        List Magnet_Suka = new ArrayList<>();
        Magnet_Suka.add(Magnet_palka);
        Objects.requireNonNull(Magnet_Wand_meta).lore(Magnet_Suka);
        Magnet_Wand.setItemMeta(Magnet_Wand_meta);
        NamespacedKey Magnet_Wand_key = new NamespacedKey(this, "MagnetWandToHeaven");
        SmithingTransformRecipe Magnet_Wand_recipe = new SmithingTransformRecipe(Magnet_Wand_key, Magnet_Wand, new RecipeChoice.ExactChoice(Wand), new RecipeChoice.MaterialChoice(Material.GLOWSTONE_DUST), new RecipeChoice.ExactChoice(FC));
        Bukkit.addRecipe(Magnet_Wand_recipe);

    }
}
