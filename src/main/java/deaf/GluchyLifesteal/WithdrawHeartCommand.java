package deaf.GluchyLifesteal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class WithdrawHeartCommand implements CommandExecutor {
    
    private final GluchyLifesteal plugin;
    
    public WithdrawHeartCommand(GluchyLifesteal plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Ta komenda moze byc uzyta tylko przez graczy.");
            return true;
        }
        
        Player player = (Player) sender;
        int playerHearts = plugin.getHeartManager().getPlayerHearts(player);
        
        if (playerHearts <= 1) {
            player.sendMessage(ChatColor.RED + "Nie mozesz wyplacic serca, masz tylko 1 serce!");
            return true;
        }
        
        if (player.getInventory().firstEmpty() == -1) {
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "Nie masz miejsca w ekwipunku!");
            return true;
        }
        
        if (plugin.getHeartManager().removePlayerHearts(player, 1)) {
            player.getInventory().addItem(plugin.getConfigManager().getHeartItem());
            player.sendMessage(ChatColor.GREEN + "Wyplaciles 1 serce jako przedmiot!");
        } else {
            player.sendMessage(ChatColor.RED + "Nie udalo sie wyplacic serca.");
        }
        
        return true;
    }
}
