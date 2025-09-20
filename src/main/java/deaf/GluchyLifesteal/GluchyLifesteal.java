package deaf.GluchyLifesteal;

import org.bukkit.plugin.java.JavaPlugin;

public final class GluchyLifesteal extends JavaPlugin {

    private static GluchyLifesteal instance;
    private HeartManager heartManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        heartManager = new HeartManager(this);
        saveDefaultConfig();
        getCommand("alifesteal").setExecutor(new ALifestealCommand(this));
        getCommand("wyplacserce").setExecutor(new WithdrawHeartCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getLogger().info("GluchyLifesteal has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("GluchyLifesteal has been disabled!");
    }
    
    public static GluchyLifesteal getInstance() {
        return instance;
    }
    
    public HeartManager getHeartManager() {
        return heartManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
