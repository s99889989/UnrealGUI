package com.daxton.unrealgui.listener;

import com.daxton.unrealcore.application.UnrealCoreAPI;

import com.daxton.unrealcore.common.event.PlayerKeyBoardEvent;
import com.daxton.unrealcore.communication.event.PlayerConnectionSuccessfulEvent;

import com.daxton.unrealcore.display.content.gui.UnrealCoreGUI;
import com.daxton.unrealcore.display.event.gui.PlayerGUICloseEvent;
import com.daxton.unrealcore.display.event.gui.PlayerGUIOpenEvent;


import com.daxton.unrealcore.display.event.gui.module.PlayerHoverEvent;
import com.daxton.unrealgui.UnrealGUI;
import com.daxton.unrealgui.controller.GUIController;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GUIListener implements Listener {

    @EventHandler//當玩家連線成功
    public void onPlayerJoin(PlayerConnectionSuccessfulEvent event) {
        Player player = event.getPlayer();
        GUIController.setTopModuleCache(player);
    }

    @EventHandler//當玩家打開GUI
    public void onGUIOpen(PlayerGUIOpenEvent event) {
        Player player = event.getPlayer();
        String uuidString = player.getUniqueId().toString();
        String name = event.getGuiName();

        boolean guiSwitchMessage = GUIController.config.getBoolean("DeBug.GUISwitchMessage");
        if(guiSwitchMessage){
            UnrealGUI.unrealCorePlugin.sendSystemLogger(player.getDisplayName()+" Open: "+name);
        }

    }

    @EventHandler//當玩家關閉GUI
    public void onGUIClose(PlayerGUICloseEvent event) {
        Player player = event.getPlayer();
        String uuidString = player.getUniqueId().toString();
        String name = event.getGuiName();
        boolean guiSwitchMessage = GUIController.config.getBoolean("DeBug.GUISwitchMessage");
        if(guiSwitchMessage){
            UnrealGUI.unrealCorePlugin.sendSystemLogger(player.getDisplayName()+" Close: "+name);
        }


    }

    //按下按鍵
    @EventHandler
    public void onPlayerKeyBoard(PlayerKeyBoardEvent event){
        Player player = event.getPlayer();
        String uuidString = player.getUniqueId().toString();
        boolean inputNow = event.isInputNow();
        int keyAction = event.getKeyAction();
        String keyName = event.getKeyName();

    }

}
