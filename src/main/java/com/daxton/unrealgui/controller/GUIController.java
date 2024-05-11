package com.daxton.unrealgui.controller;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.application.base.PluginUtil;
import com.daxton.unrealcore.application.base.YmlFileUtil;
import com.daxton.unrealcore.application.method.SchedulerRunnable;
import com.daxton.unrealcore.been.display.type.SystemGUIType;
import com.daxton.unrealcore.display.been.module.ModuleData;
import com.daxton.unrealcore.display.content.tooltip.UnrealCoreTooltip;
import com.daxton.unrealgui.UnrealGUI;
import com.daxton.unrealgui.gui.UnrealGUIContainer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class GUIController {
    //GUI列表
    public static Map<String, FileConfiguration> unrealCoreGUIMap = new HashMap<>();
    //Top模塊
    public static Map<String, List<ModuleData>> moduleDataMap = new HashMap<>();
    //設定檔
    public static FileConfiguration config;

    //讀取設定
    public static void load(){
        if(Bukkit.getPluginManager().getPlugin("UnrealCore") == null){
            return;
        }
        //建立設定檔
        UnrealGUI.unrealCorePlugin.createConfig();
        config = UnrealGUI.unrealCorePlugin.getYmlFile("config.yml");

        unrealCoreGUIMap = UnrealGUI.unrealCorePlugin.findYmlMap("gui");

        UnrealGUI.unrealCorePlugin.findYmlMap("tooltip").forEach((name, fileConfiguration) -> {
            List<ModuleData> moduleDataList = UnrealCoreAPI.inst().getGUIHelper().getTopModuleList("", fileConfiguration);
            moduleDataMap.put(name, moduleDataList);
        });


    }

    //把模塊列表轉成ID列表
    public static List<String> moduleIDList(List<ModuleData> moduleDataList){
        List<String> stringList = new ArrayList<>();

        moduleDataList.forEach(moduleData -> stringList.add(moduleData.getModuleID()));

        return stringList;
    }

    public static void reload(){
        unrealCoreGUIMap.clear();

        load();
    }

    //是否包含此GUI
    public static boolean contain(String guiName){
        return unrealCoreGUIMap.containsKey(guiName);
    }

    //打開GUI
    public static void open(Player player, String guiName){
//        SystemGUIType systemGUIType = SystemGUIType.convert(guiName);
//        if(systemGUIType != SystemGUIType.None){
//
//            UnrealCoreAPI.inst(player).getGUIHelper().openSystemGUI(systemGUIType);
//            return;
//        }
//
//        if(UnrealCoreAPI.inst(player).getGUIHelper().getUnrealCoreGUI() != null){
//            String nowGUIName = UnrealCoreAPI.inst(player).getGUIHelper().getUnrealCoreGUI().getGUIName();
//            if(nowGUIName.equals(guiName)){
//                UnrealCoreAPI.inst(player).getGUIHelper().closeCoreGUI();
//                return;
//            }
//
//        }

        if(unrealCoreGUIMap.containsKey(guiName)){
            UnrealGUIContainer unrealGUIContainer = new UnrealGUIContainer(guiName, unrealCoreGUIMap.get(guiName));
            UnrealCoreAPI.inst(player).getGUIHelper().openCoreGUI(unrealGUIContainer);
        }

    }



}
