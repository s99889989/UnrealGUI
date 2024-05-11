package com.daxton.unrealgui.application;

import com.daxton.unrealgui.controller.GUIController;
import org.bukkit.entity.Player;

public class UnrealGUIAPI {

    public static void openGUI(Player player, String guiName){
        GUIController.open(player, guiName);
    }

}
