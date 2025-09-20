package deaf.GluchyLifesteal;

import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartManager {
    
    private final GluchyLifesteal plugin;
    private final File playerDataFile;
    private FileConfiguration playerDataConfig;
    private final Map<UUID, Map<UUID, Long>> cooldowns;
    
    public HeartManager(GluchyLifesteal plugin) {
        this.plugin = plugin;
        this.playerDataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        this.cooldowns = new HashMap<>();
        reloadPlayerData();
    }
    
    public void reloadPlayerData() {
        if (!playerDataFile.exists()) {
            plugin.getDataFolder().mkdirs();
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }
    
    public int getPlayerHearts(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            return (int) (attribute.getValue() / 2);
        }
        return 10;
    }
    
    public boolean setPlayerHearts(Player player, int hearts) {
        int maxHearts = plugin.getConfigManager().getMaxHearts();
        if (hearts < 1) hearts = 1;
        if (hearts > maxHearts) hearts = maxHearts;
        
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            double health = hearts * 2.0;
            attribute.setBaseValue(health);
            
            player.setHealth(Math.min(player.getHealth() + 2.0, health));
            
            playerDataConfig.set(player.getUniqueId().toString() + ".hearts", hearts);
            savePlayerData();
            return true;
        }
        return false;
    }
    
    public boolean addPlayerHearts(Player player, int hearts) {
        int currentHearts = getPlayerHearts(player);
        return setPlayerHearts(player, currentHearts + hearts);
    }
    
    public boolean removePlayerHearts(Player player, int hearts) {
        int currentHearts = getPlayerHearts(player);
        return setPlayerHearts(player, currentHearts - hearts);
    }
    
    public void savePlayerData() {
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setKillerCooldown(UUID killer, UUID victim) {
        cooldowns.computeIfAbsent(killer, k -> new HashMap<>()).put(victim, System.currentTimeMillis());
    }
    
    public boolean isOnCooldown(UUID killer, UUID victim) {
        Map<UUID, Long> killerCooldowns = cooldowns.get(killer);
        if (killerCooldowns == null) return false;
        
        Long timestamp = killerCooldowns.get(victim);
        if (timestamp == null) return false;
        
        long cooldownMillis = plugin.getConfigManager().getCooldownSeconds() * 1000L;
        return (System.currentTimeMillis() - timestamp) < cooldownMillis;
    }
    
    public String getCooldownTimeLeft(UUID killer, UUID victim) {
        Map<UUID, Long> killerCooldowns = cooldowns.get(killer);
        if (killerCooldowns == null) return "0";
        
        Long timestamp = killerCooldowns.get(victim);
        if (timestamp == null) return "0";
        
        long cooldownMillis = plugin.getConfigManager().getCooldownSeconds() * 1000L;
        long timeLeftMillis = cooldownMillis - (System.currentTimeMillis() - timestamp);
        
        if (timeLeftMillis <= 0) return "0";
        
        long seconds = timeLeftMillis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return minutes + "min " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }
}
