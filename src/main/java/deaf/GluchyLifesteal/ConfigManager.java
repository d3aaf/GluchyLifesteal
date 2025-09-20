package deaf.GluchyLifesteal;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.ArrayList;

public class ConfigManager {
    
    private final GluchyLifesteal plugin;
    
    public ConfigManager(GluchyLifesteal plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }
    
    public void saveDefaultConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        
        config.addDefault("heart-item.material", "NETHER_STAR");
        config.addDefault("heart-item.name", "&cSerce Zycia");
        config.addDefault("heart-item.lore", List.of("&7Kliknij prawym przyciskiem myszy,", "&7aby dodac sobie serce!"));
        config.addDefault("heart-item.custom-model-data", 1);
        config.addDefault("heart-item.glint", true);
        
        config.addDefault("max-hearts", 20);
        config.addDefault("cooldown-seconds", 300);
        
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    public ItemStack getHeartItem() {
        FileConfiguration config = plugin.getConfig();
        
        String materialStr = config.getString("heart-item.material", "NETHER_STAR");
        Material material = Material.getMaterial(materialStr.toUpperCase());
        if (material == null) {
            material = Material.NETHER_STAR;
        }
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            String name = config.getString("heart-item.name", "&cSerce Zycia");
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            
            List<String> loreStrings = config.getStringList("heart-item.lore");
            List<String> lore = new ArrayList<>();
            for (String line : loreStrings) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(lore);
            
            int customModelData = config.getInt("heart-item.custom-model-data", 1);
            if (customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }
            
            item.setItemMeta(meta);
            
            if (config.getBoolean("heart-item.glint", true)) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }
        }
        
        return item;
    }
    
    public int getMaxHearts() {
        return plugin.getConfig().getInt("max-hearts", 20);
    }
    
    public int getCooldownSeconds() {
        return plugin.getConfig().getInt("cooldown-seconds", 300);
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
    }
}
