package com.daxton.unrealgui.listener;


import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealgui.controller.GUIController;
import com.daxton.unrealresource.event.UnrealResourceLoadFinishEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ResourceListener implements Listener {



    @EventHandler//當玩家資源加載成功
    public void onPlayerJoin(UnrealResourceLoadFinishEvent event) {
        Player player = event.getPlayer();


    }

}
