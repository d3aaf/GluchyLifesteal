package deaf.GluchyLifesteal;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

public class PlayerListener implements Listener {
    
    private final GluchyLifesteal plugin;
    
    public PlayerListener(GluchyLifesteal plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        
        ItemStack heartItem = plugin.getConfigManager().getHeartItem();
        if (!isSimilarItem(item, heartItem)) {
            return;
        }
        
        event.setCancelled(true);
        event.setCancelled(true);
        
        int playerHearts = plugin.getHeartManager().getPlayerHearts(event.getPlayer());
        int maxHearts = plugin.getConfigManager().getMaxHearts();
        
        if (playerHearts >= maxHearts) {
            event.getPlayer().sendMessage(ChatColor.RED + "Nie mozesz dodac wiecej serc! Osiagnales limit " + maxHearts + " serc.");
            return;
        }
        
        if (plugin.getHeartManager().addPlayerHearts(event.getPlayer(), 1)) {
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                event.getPlayer().getInventory().setItemInMainHand(null);
            }
            
            event.getPlayer().sendMessage(ChatColor.GREEN + "Dodales sobie 1 serce!");
        } else {
            event.getPlayer().sendMessage(ChatColor.RED + "Nie udalo sie dodac serca!");
        }
    }
    
    private boolean isSimilarItem(ItemStack item1, ItemStack item2) {
        if (item1.getType() != item2.getType()) {
            return false;
        }
        
        if (item1.hasItemMeta() != item2.hasItemMeta()) {
            return false;
        }
        
        if (!item1.hasItemMeta()) {
            return true;
        }
        
        var meta1 = item1.getItemMeta();
        var meta2 = item2.getItemMeta();
        
        if (meta1.hasDisplayName() != meta2.hasDisplayName()) {
            return false;
        }
        
        if (meta1.hasDisplayName() && !meta1.getDisplayName().equals(meta2.getDisplayName())) {
            return false;
        }
        
        if (meta1.hasCustomModelData() != meta2.hasCustomModelData()) {
            return false;
        }
        
        if (meta1.hasCustomModelData() && meta1.getCustomModelData() != meta2.getCustomModelData()) {
            return false;
        }
        
        return true;
    }
}
