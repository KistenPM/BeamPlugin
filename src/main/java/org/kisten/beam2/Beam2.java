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
        registerGlow();
        crafts();
        getServer().getPluginManager().registerEvents(new onClickSpawnBeam(), this);
        getLogger().info("Плагин Laser включен!");
    }

    @Override
    public void onDisable() {
        // Плагин выключен
        getLogger().info("Плагин Laser выключен!");
    }

    public static Beam2 getInstance() {
        return instance;
    }

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
    public void crafts() {

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


// Код для создания команды

//    @Override
//    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
//        if (command.getName().equalsIgnoreCase("laser")) {
//            if (sender instanceof Player) {
//                Player player = (Player) sender;
//                // Получаем текущую локацию игрока
//                Location playerLocation = player.getEyeLocation();
//                // Создаем лазер в направлении координаты
//                Location start = new Location(playerLocation.getWorld(), 100.0, 100.0, 100);
//                createLaser(start, playerLocation);
//
//                return true;
//            }
//        }
//        return false;
//    }

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
