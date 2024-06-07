package com.daxton.unrealgui.controller;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.been.display.type.SystemGUIType;
import com.daxton.unrealcore.display.been.module.ModuleData;
import com.daxton.unrealgui.UnrealGUI;
import com.daxton.unrealgui.gui.UnrealGUIContainer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class GUIController {
    //GUI列表
    public static Map<String, FileConfiguration> unrealCoreGUIMap = new HashMap<>();
    //置頂模塊
    public static Map<String, List<ModuleData>> topModuleCache = new HashMap<>();
    //設定檔
    public static FileConfiguration config;

    //設置置頂模塊緩存
    public static void setTopModuleCache(Player player){
        topModuleCache.forEach((key, moduleDataList) -> {
            UnrealCoreAPI.inst(player).getGUIHelper().addTopModuleCache(key, moduleDataList);
        });
    }

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
            topModuleCache.put(name, moduleDataList);
        });


    }

    public static void reload(){
        topModuleCache.clear();
        unrealCoreGUIMap.clear();

        load();

        topModuleCache.forEach((key, moduleDataList) -> {
            UnrealCoreAPI.inst().getGUIHelper().addTopModuleCache(key, moduleDataList);
        });

    }



    //把模塊列表轉成ID列表
    public static List<String> moduleIDList(List<ModuleData> moduleDataList){
        List<String> stringList = new ArrayList<>();

        moduleDataList.forEach(moduleData -> stringList.add(moduleData.getModuleID()));

        return stringList;
    }



    //是否包含此GUI
    public static boolean contain(String guiName){
        return unrealCoreGUIMap.containsKey(guiName);
    }

    //打開GUI
    public static void open(Player player, String guiName){
        SystemGUIType systemGUIType = SystemGUIType.convert(guiName);
        if(systemGUIType != SystemGUIType.None){

            UnrealCoreAPI.inst(player).getGUIHelper().openSystemGUI(systemGUIType);
            return;
        }

        if(UnrealCoreAPI.inst(player).getGUIHelper().getUnrealCoreGUI() != null){
            String nowGUIName = UnrealCoreAPI.inst(player).getGUIHelper().getUnrealCoreGUI().getGUIName();
            if(nowGUIName.equals(guiName)){
                UnrealCoreAPI.inst(player).getGUIHelper().closeCoreGUI();
                return;
            }

        }

        if(unrealCoreGUIMap.containsKey(guiName)){
            UnrealGUIContainer unrealGUIContainer = new UnrealGUIContainer(guiName, unrealCoreGUIMap.get(guiName));
            UnrealCoreAPI.inst(player).getGUIHelper().openCoreGUI(unrealGUIContainer);
        }

    }



}
