package com.daxton.unrealgui.gui;



import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.application.base.YmlFileUtil;
import com.daxton.unrealcore.application.method.SchedulerFunction;
import com.daxton.unrealcore.application.method.SchedulerRunnable;
import com.daxton.unrealcore.been.display.type.HoverType;
import com.daxton.unrealcore.common.type.MouseActionType;
import com.daxton.unrealcore.common.type.MouseButtonType;

import com.daxton.unrealcore.display.been.module.ModuleData;
import com.daxton.unrealcore.display.content.gui.UnrealCoreGUI;

import com.daxton.unrealcore.display.content.module.ModuleComponents;
import com.daxton.unrealcore.display.content.module.control.*;

import com.daxton.unrealcore.display.content.module.display.ItemModule;

import com.daxton.unrealgui.application.PlayerFunction;
import com.daxton.unrealgui.controller.GUIController;
import com.daxton.unrealgui.UnrealGUI;
import com.daxton.unrealgui.application.CustomValueConvert;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UnrealGUIContainer extends UnrealCoreGUI {

    //佔位符列表
    public Map<String, String> customValue = new HashMap<>();
    //更新佔位符
    SchedulerRunnable schedulerRunnable;

    String name;
    //停止動作
    private boolean stop;
    //指令執行延遲
    private List<String> command_cooldown = new ArrayList<>();

    public UnrealGUIContainer(String guiName, FileConfiguration fileConfiguration) {

        super(guiName, fileConfiguration);
        customValue.clear();
        applyFunctionToFields(this::placeholder);

    }

    @Override
    public void opening() {

        SchedulerFunction.runLater(UnrealGUI.unrealCorePlugin.getJavaPlugin(), ()->{
            stop = false;
        }, 5);
        if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            placeholderChange();
        }
    }

    //更新佔位符
    public void placeholderChange(){

        //定時更新佔位符
        schedulerRunnable = new SchedulerRunnable() {
            @Override
            public void run() {
                Map<String, String> customValueMap = new HashMap<>();
                customValue.forEach((content, contentChange) -> {
                    String value = PlaceholderAPI.setPlaceholders(getPlayer(), "%"+content+"%");
                    customValueMap.put(contentChange, value);
                });
                UnrealCoreAPI.setCustomValueMap(getPlayer(), customValueMap);
            }
        };
        schedulerRunnable.runTimer(UnrealGUI.unrealCorePlugin.getJavaPlugin(), 0, 5);

    }

    //把佔位符%%改成{}並存到Map來更新內容
    public String placeholder(String content){
        if(content.startsWith("{") && content.endsWith("}") && content.length() > 30){
            content = content.replace("{", "<[").replace("}", "]>");
            return CustomValueConvert.valueNBT(content, customValue);
        }
        return CustomValueConvert.value(content, customValue);
    }

    @Override
    public void buttonClick(ButtonModule buttonModule, MouseButtonType button, MouseActionType action) {
        if(stop){
            return;
        }

        String id = buttonModule.getModuleData().getPath()+buttonModule.getModuleID();


        if(button == MouseButtonType.Left && action == MouseActionType.Off){

            //關閉GUI
            boolean closeGUI = getFileConfiguration().getBoolean(buttonModule.getFilePath()+".Close");
            if(closeGUI){
                getPlayer().closeInventory();
//                UnrealCoreAPI.inst(this.getPlayer()).getGUIHelper().closeGUI();
            }

            //打開GUI
            String toGUI = getFileConfiguration().getString(buttonModule.getFilePath()+".ToGUI");
            if(toGUI != null){
                GUIController.open(getPlayer(), toGUI);
            }

            //打開連結
            String openUrl = getFileConfiguration().getString(buttonModule.getFilePath()+".OpenUrl");
            if(openUrl != null){
                UnrealCoreAPI.inst(getPlayer()).getCommonHelper().openURL(openUrl);
            }

            //如果冷卻還沒到就不執行以下動作
            if(command_cooldown.contains(id)){
                return;
            }

            //指令冷卻
            int commandCoolDownTick = getFileConfiguration().getInt(buttonModule.getFilePath()+".CommandCoolDown");
            if(commandCoolDownTick > 0){
                command_cooldown.add(id);
                SchedulerFunction.runLater(UnrealGUI.unrealCorePlugin.getJavaPlugin(), ()->{
                    command_cooldown.remove(id);
                }, commandCoolDownTick);
            }

            //指令
            List<String> commandList = getFileConfiguration().getStringList(buttonModule.getFilePath()+".Command");
            if(commandList.isEmpty()){
                String commandString = getFileConfiguration().getString(buttonModule.getFilePath()+".Command");
                if(commandString != null){
                    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
                        PlayerFunction.onCommand(getPlayer(), PlaceholderAPI.setPlaceholders(getPlayer(), commandString));
                    }else {
                        PlayerFunction.onCommand(getPlayer(), commandString);
                    }
                }
            }else {
                commandList.forEach(command->{
                    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
                        PlayerFunction.onCommand(getPlayer(), PlaceholderAPI.setPlaceholders(getPlayer(), command));
                    }else {
                        PlayerFunction.onCommand(getPlayer(), command);
                    }
                });
            }

            //條件式指令
            if(getFileConfiguration().contains(buttonModule.getFilePath()+".CommandCondition")){
                YmlFileUtil.sectionList(getFileConfiguration(), buttonModule.getFilePath()+".CommandCondition").forEach(key->{
                    String condition = getFileConfiguration().getString(buttonModule.getFilePath()+".CommandCondition."+key+".Condition");
                    if(condition != null){

                        condition = PlaceholderAPI.setPlaceholders(getPlayer(), condition);
                        String[] conditions = condition.split("==");
                        if(conditions.length == 2){
                            if(conditions[0].trim().equals(conditions[1].trim())){
                                List<String> commandList2 = getFileConfiguration().getStringList(buttonModule.getFilePath()+".CommandCondition."+key+".Command");
                                commandList2.forEach(command->{
                                    String bb = PlaceholderAPI.setPlaceholders(getPlayer(), command);
                                    PlayerFunction.onCommand(getPlayer(), bb);
                                });
                            }
                        }

                    }

                });
            }


        }

        super.buttonClick(buttonModule, button, action);
    }

    @Override
    public void hover(ModuleComponents moduleComponents, HoverType hoverType, boolean haveItem) {
        if(stop){
            return;
        }

        //Hover的顯示組建設定
        String hover = getFileConfiguration().getString(moduleComponents.getFilePath()+".Hover", "");
        List<ModuleData> moduleDataList = GUIController.topModuleCache.get(hover);
        if(moduleDataList == null){
            return;
        }

        //離開組件範圍
        if(hoverType == HoverType.LEAVE){
            List<String> stringList = GUIController.moduleIDList(moduleDataList);
            UnrealCoreAPI.inst(getPlayer()).getGUIHelper().removeTopModule(stringList);
        }
        //進入組件範圍
        if(hoverType == HoverType.ENTER){

            //延遲避免取消後出發  就不顯示
            SchedulerFunction.runLater(UnrealGUI.unrealCorePlugin.getJavaPlugin(), ()->{
                if(moduleComponents instanceof SlotModule || moduleComponents instanceof ItemModule){
                    if(!haveItem){
                        return;
                    }
                }

                UnrealCoreAPI.inst(getPlayer()).getGUIHelper().addTopModule(moduleDataList);
            }, 1);

        }

        super.hover(moduleComponents, hoverType, haveItem);
    }
    
    @Override
    public void close() {
        stop = true;
        //取消佔位符更新任務
        if(schedulerRunnable != null){
            if(!schedulerRunnable.isCancelled()){
                schedulerRunnable.cancel();
            }
        }

        //把存在客戶端的佔位符值清除
        if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            List<String> customValueList = new ArrayList<>();
            customValue.forEach((content, contentChange) -> customValueList.add(content));
            UnrealCoreAPI.customValueMultiRemove(getPlayer(), customValueList);
        }
        customValue.clear();

        super.close();
    }


    //                    SlotModule slotModule = (SlotModule) moduleComponents;
//
//                    ItemStack itemStack = null;
//                    if(getMainGUIData().getType().equals("Inventory")){
//                        if(slotModule.getSlot() > 4){
//                            itemStack = getPlayer().getOpenInventory().getBottomInventory().getItem(getInventorySlotID(slotModule.getSlot()));
//                        }else {
//                            itemStack = getPlayer().getOpenInventory().getTopInventory().getItem(slotModule.getSlot());
//                        }
//                    }else {
//                        int size = getPlayer().getOpenInventory().getTopInventory().getSize()+(getPlayer().getOpenInventory().getBottomInventory().getSize()-5);
//                        if(slotModule.getSlot() < size){
//                            if(slotModule.getSlot() < size) {
//                                itemStack = getPlayer().getOpenInventory().getItem(slotModule.getSlot());
//                            }
//                        }
//                    }
//
//                    if (itemStack == null || itemStack.getType() == Material.AIR) {
//                        super.hover(moduleComponents, hoverType, haveItem);
//                        return;
//                    }
    //把客戶端InventoryID轉成伺服器用
    public static int getInventorySlotID(int slotID){
        switch (slotID){
            case 0: return 41; case 1: return 42; case 2: return 43; case 3: return 44; case 4: return 45;
            case 5: return 39; case 6: return 38; case 7: return 37; case 8: return 36; case 45: return 40;
            case 9: return 9; case 10: return 10; case 11: return 11; case 12: return 12; case 13: return 13; case 14: return 14; case 15: return 15; case 16: return 16; case 17: return 17;
            case 18: return 18; case 19: return 19; case 20: return 20; case 21: return 21; case 22: return 22; case 23: return 23; case 24: return 24; case 25: return 25; case 26: return 26;
            case 27: return 27; case 28: return 28; case 29: return 29; case 30: return 30; case 31: return 31; case 32: return 32; case 33: return 33; case 34: return 34; case 35: return 35;
            case 36: return 0; case 37: return 1; case 38: return 2; case 39: return 3; case 40: return 4; case 41: return 5; case 42: return 6; case 43: return 7; case 44: return 8;
        }
        return 0;
    }
}
