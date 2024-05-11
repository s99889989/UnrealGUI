package com.daxton.unrealgui.command;

import com.daxton.unrealcore.nms.NMSItem;
import com.daxton.unrealgui.controller.GUIController;
import com.daxton.unrealgui.UnrealGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;


public class GUICommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){

                if(sender instanceof Player){
                    Player player = (Player) sender;
                    if(!player.isOp()){
                        return true;
                    }
                    player.sendMessage("[UnrealGUI] Reload");
                }
                UnrealGUI.unrealCorePlugin.sendSystemLogger("Reload");
                GUIController.reload();
                return true;
            }
//            if(args[0].equalsIgnoreCase("test")){
//                if(sender instanceof Player){
//                    Player player = (Player) sender;
//                    ItemStack itemStack = new ItemStack(Material.ACACIA_BOAT);
//                    ItemMeta itemMeta = itemStack.getItemMeta();
//                   if(itemMeta != null){
//                       itemMeta.setDisplayName("GOGO");
//                       itemStack.setItemMeta(itemMeta);
//                   }
//
//                    String nbtString = NMSItem.itemNBTtoString(itemStack);
//                    UnrealGUI.unrealCorePlugin.sendLogger(nbtString);
////                    World endWorld = Bukkit.getWorld("world_the_end"); // 末地的世界名称，请根据你的服务器设置进行调整
////                    if (endWorld == null) {
////                        sender.sendMessage("The End world is not loaded.");
////                        return true;
////                    }
////
////                    Location endSpawn = endWorld.getSpawnLocation();
////                    player.teleport(endSpawn);
//////                    Inventory chestInventory = Bukkit.createInventory(null, 27, "普通仓库");
//////                    player.openInventory(chestInventory);
//                }
//
//            }
        }

        if (args.length == 2){
            if(args[0].equalsIgnoreCase("open")){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    GUIController.open(player, args[1]);
                }
            }

        }

        if (args.length == 3){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(!player.isOp()){
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("open")){
                Player player = Bukkit.getPlayer(args[2]);
                if(player != null){
                    GUIController.open(player, args[1]);
                }
            }
        }

        return false;
    }
}
