package com.daxton.unrealgui.command;

import com.daxton.unrealgui.controller.GUIController;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUITab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commandList = new ArrayList<>();
        if (args.length == 1){
            commandList = getCommandList(sender, new String[]{"reload", "open", "test"}, new String[]{"open"});
        }
        if (args.length == 2){
            if(args[0].equalsIgnoreCase("open")){
                commandList = new ArrayList<>(GUIController.unrealCoreGUIMap.keySet());
            }
        }

        if (args.length == 3){
            if(args[0].equalsIgnoreCase("open")){
                if(GUIController.unrealCoreGUIMap.containsKey(args[1])){
                    commandList = Bukkit.getOnlinePlayers().stream()
                            .map(org.bukkit.entity.Player::getName)
                            .collect(Collectors.toList());
                }
            }
        }

        return commandList;
    }
    //判斷管理和玩家來回傳使用指令
    public static List<String> getCommandList(CommandSender sender, String[] opCommand, String[] playerCommand){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!player.isOp()){
                return Arrays.stream(playerCommand).collect(Collectors.toList());
            }
        }
        return Arrays.stream(opCommand).collect(Collectors.toList());
    }

}
