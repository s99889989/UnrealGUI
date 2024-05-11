package com.daxton.unrealgui.application;

import com.daxton.unrealgui.UnrealGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerFunction {

    //執行指令
    public static void onCommand(Player player, String cmd) {
        try {
            String[] commands = cmd.split("]:");

            // 检查指令参数长度
            if (commands.length >= 2) {
                commands[1] = commands[1].replace("&", "§").replace("%player%", player.getName());
                String command = commands[1].trim();

                switch (commands[0]) {
                    case "[console":
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                        break;
                    case "[op":
                        boolean originalOpStatus = player.isOp();
                        player.setOp(true);
                        player.performCommand(command);
                        player.setOp(originalOpStatus);
                        break;
                    case "[tell":
                        player.sendMessage(command);
                        break;
                    case "[bc":
                        Bukkit.broadcastMessage(command);
                        break;
                    case "[player":
                        player.performCommand(command);
                        break;
                    case "[chat":
                        player.chat(command);
                        break;
                    default:
                        break;
                }
            } else {
                // 指令格式不正確的情況
                UnrealGUI.unrealCorePlugin.sendSystemLogger("指令格式錯誤!");
                UnrealGUI.unrealCorePlugin.sendSystemLogger("command format error!");
            }
        } catch (Exception exception) {
            // 記錄異常日誌
            UnrealGUI.unrealCorePlugin.sendSystemLogger("An exception occurred while processing the command: " + exception.getMessage());
        }
    }

}
