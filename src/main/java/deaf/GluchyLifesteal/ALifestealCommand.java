package deaf.GluchyLifesteal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class ALifestealCommand implements CommandExecutor {
    
    private final GluchyLifesteal plugin;
    
    public ALifestealCommand(GluchyLifesteal plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Poprawne uzycie: /alifesteal [check|get|give|remove|reload] [args]");
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "check":
                return handleCheckCommand(sender, args);
            case "get":
                return handleGetCommand(sender, args);
            case "give":
                return handleGiveCommand(sender, args);
            case "remove":
                return handleRemoveCommand(sender, args);
            case "reload":
                return handleReloadCommand(sender, args);
            default:
                sender.sendMessage(ChatColor.RED + "Nieznana subkomenda. Uzycie: /alifesteal [check|get|give|remove|reload] [args]");
                return true;
        }
    }
    
    private boolean handleCheckCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Uzycie: /alifesteal check <gracz>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Gracz " + args[1] + " nie jest online.");
            return true;
        }
        
        int hearts = plugin.getHeartManager().getPlayerHearts(target);
        sender.sendMessage(ChatColor.GREEN + "Gracz " + target.getName() + " ma " + hearts + " serc.");
        return true;
    }
    
    private boolean handleGetCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("alifesteal.admin")) {
            sender.sendMessage(ChatColor.RED + "Nie masz uprawnien do uzycia tej komendy.");
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Uzycie: /alifesteal get <gracz> <ilosc>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Gracz " + args[1] + " nie jest online.");
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Ilosc musi byc wieksza od 0.");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Nieprawidlowa ilosc. Podaj liczbe calkowita.");
            return true;
        }
        
        for (int i = 0; i < amount; i++) {
            target.getInventory().addItem(plugin.getConfigManager().getHeartItem());
        }
        
        sender.sendMessage(ChatColor.GREEN + "Dodano " + amount + " serc do ekwipunku gracza " + target.getName() + ".");
        target.sendMessage(ChatColor.GREEN + "Otrzymales " + amount + " serc!");
        return true;
    }
    
    private boolean handleGiveCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("alifesteal.admin")) {
            sender.sendMessage(ChatColor.RED + "Nie masz uprawnien do uzycia tej komendy.");
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Uzycie: /alifesteal give <gracz> <ilosc>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Gracz " + args[1] + " nie jest online.");
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Ilosc musi byc wieksza od 0.");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Nieprawidlowa ilosc. Podaj liczbe calkowita.");
            return true;
        }
        
        int playerHearts = plugin.getHeartManager().getPlayerHearts(target);
        int maxHearts = plugin.getConfigManager().getMaxHearts();
        
        if (playerHearts >= maxHearts) {
            sender.sendMessage(ChatColor.RED + "Gracz " + target.getName() + " osiagnal juz maksymalna liczbe serc (" + maxHearts + ").");
            return true;
        }
        
        if (plugin.getHeartManager().addPlayerHearts(target, amount)) {
            sender.sendMessage(ChatColor.GREEN + "Dodano " + amount + " serc bezposrednio do gracza " + target.getName() + ".");
            target.sendMessage(ChatColor.GREEN + "Otrzymales " + amount + " serc bezposrednio!");
        } else {
            sender.sendMessage(ChatColor.RED + "Nie udalo sie dodac serc do gracza " + target.getName() + ".");
        }
        return true;
    }
    
    private boolean handleRemoveCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("alifesteal.admin")) {
            sender.sendMessage(ChatColor.RED + "Nie masz uprawnien do uzycia tej komendy.");
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Uzycie: /alifesteal remove <gracz> <ilosc>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Gracz " + args[1] + " nie jest online.");
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Ilosc musi byc wieksza od 0.");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Nieprawidlowa ilosc. Podaj liczbe calkowita.");
            return true;
        }
        
        if (plugin.getHeartManager().removePlayerHearts(target, amount)) {
            sender.sendMessage(ChatColor.GREEN + "Odebrano " + amount + " serc od gracza " + target.getName() + ".");
            target.sendMessage(ChatColor.RED + "Straciles " + amount + " serc!");
        } else {
            sender.sendMessage(ChatColor.RED + "Nie udalo sie odjac serc od gracza " + target.getName() + ".");
        }
        return true;
    }
    
    private boolean handleReloadCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("alifesteal.admin")) {
            sender.sendMessage(ChatColor.RED + "Nie masz uprawnien do uzycia tej komendy.");
            return true;
        }
        
        plugin.reloadConfig();
        plugin.getConfigManager().saveDefaultConfig();
        plugin.getHeartManager().reloadPlayerData();
        sender.sendMessage(ChatColor.GREEN + "Konfiguracja pluginu zostala przeladowana.");
        return true;
    }
}
