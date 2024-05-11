package com.daxton.unrealgui;

import com.daxton.unrealcore.UnrealCorePlugin;
import com.daxton.unrealgui.command.GUICommand;
import com.daxton.unrealgui.command.GUITab;
import com.daxton.unrealgui.controller.GUIController;
import com.daxton.unrealgui.listener.GUIListener;
import com.daxton.unrealgui.listener.ResourceListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class UnrealGUI extends JavaPlugin {

    public static UnrealCorePlugin unrealCorePlugin;

    @Override
    public void onEnable() {

        unrealCorePlugin = new UnrealCorePlugin(this);

        Objects.requireNonNull(Bukkit.getPluginCommand("unrealgui")).setExecutor(new GUICommand());

        Objects.requireNonNull(Bukkit.getPluginCommand("unrealgui")).setTabCompleter(new GUITab());

        GUIController.load();

        Bukkit.getPluginManager().registerEvents(new GUIListener(), unrealCorePlugin.getJavaPlugin());
        if(Bukkit.getServer().getPluginManager().getPlugin("UnrealResource") != null){
            Bukkit.getPluginManager().registerEvents(new ResourceListener(), unrealCorePlugin.getJavaPlugin());
        }

    }

    @Override
    public void onDisable() {

    }

}
