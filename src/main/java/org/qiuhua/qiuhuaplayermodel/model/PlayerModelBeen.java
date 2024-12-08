package org.qiuhua.qiuhuaplayermodel.model;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.application.base.YmlFileUtil;
import com.daxton.unrealcore.been.entity.base.EntityDisplayBeen;
import com.daxton.unrealcore.been.entity.gecko.*;
import com.daxton.unrealcore.been.entity.type.LivingEntityPoseType;
import com.daxton.unrealcore.been.item.type.EquipmentSlotType;
import com.daxton.unrealcore.been.player.PoseAnimationsBeen;
import com.daxton.unrealcore.been.player.gecko.FirstViewPlaceBeen;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.qiuhua.qiuhuaplayermodel.data.HandheldItemsData;
import org.qiuhua.qiuhuaplayermodel.data.PlayerModelData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerModelBeen {
    private String modelID;
    private boolean firstModelEnable;
    private boolean thirdModelEnable;
    private boolean originalMaterial;
    private float firstDistance;
    private float firstHeight;
    private List<GeckoEntityHiddenPartsBeen> firstRemovePart = new ArrayList<>();
    private List<GeckoEntityHiddenPartsBeen> thirdRemovePart = new ArrayList<>();
    private List<GeckoEntityItemBeen> geckoEntityItemBeenList = new ArrayList<>();




    //这里开始为动作数据
    //玩家当前模型的动作数据
    private final ConcurrentHashMap<String, HandheldItemsData> handheldItemsData = new ConcurrentHashMap<>();


    public PlayerModelBeen(FileConfiguration modelConfig) {
        //模型基础配置
        this.modelID = YmlFileUtil.getString(modelConfig, "Model", "player_female");
        this.firstModelEnable = YmlFileUtil.getBoolean(modelConfig, "First.Enable", false);
        this.thirdModelEnable = YmlFileUtil.getBoolean(modelConfig, "Third.Enable", false);
        this.originalMaterial = YmlFileUtil.getBoolean(modelConfig, "OriginalMaterial", false);
        this.firstDistance = YmlFileUtil.getFloat(modelConfig, "First.Distance", 0.0F);
        this.firstHeight = YmlFileUtil.getFloat(modelConfig, "First.Height", 0.0F);
        YmlFileUtil.getStringList(modelConfig, "First.RemovePart").forEach((partName) -> {
            String partId = partName;
            boolean child = true;
            if (partName.contains(",")) {
                String[] partNames = partName.split(",");
                if (partNames.length == 2) {
                    partId = partNames[0].trim();
                    child = Boolean.parseBoolean(partNames[1].trim());
                }
            }
            GeckoEntityHiddenPartsBeen geckoEntityHiddenPartsBeen = new GeckoEntityHiddenPartsBeen("", "QiuhuaPlayerModel", partId, false, true, child);
            this.firstRemovePart.add(geckoEntityHiddenPartsBeen);
        });
        YmlFileUtil.getStringList(modelConfig, "Third.RemovePart").forEach((partName) -> {
            String partId = partName;
            boolean child = true;
            if (partName.contains(",")) {
                String[] partNames = partName.split(",");
                if (partNames.length == 2) {
                    partId = partNames[0].trim();
                    child = Boolean.parseBoolean(partNames[1].trim());
                }
            }
            GeckoEntityHiddenPartsBeen geckoEntityHiddenPartsBeen = new GeckoEntityHiddenPartsBeen("", "QiuhuaPlayerModel", partId, false, true, child);
            this.thirdRemovePart.add(geckoEntityHiddenPartsBeen);
        });
        YmlFileUtil.sectionList(modelConfig, "Items").forEach((key) -> {
            String showLocation = YmlFileUtil.getString(modelConfig, "Items." + key + ".ShowLocation", "");
            EquipmentSlotType equipmentSlotType = EquipmentSlotType.to(YmlFileUtil.getString(modelConfig, "Items." + key + ".EquipmentSlot", ""));
            double posX = YmlFileUtil.getDouble(modelConfig, "Items." + key + ".PosX", 0.0);
            double posY = YmlFileUtil.getDouble(modelConfig, "Items." + key + ".PosY", 0.0);
            double posZ = YmlFileUtil.getDouble(modelConfig, "Items." + key + ".PosZ", 0.0);
            float rotX = YmlFileUtil.getFloat(modelConfig, "Items." + key + ".RotX", 0.0F);
            float rotY = YmlFileUtil.getFloat(modelConfig, "Items." + key + ".RotY", 0.0F);
            float rotZ = YmlFileUtil.getFloat(modelConfig, "Items." + key + ".RotZ", 0.0F);
            GeckoEntityItemBeen geckoEntityItemBeen = new GeckoEntityItemBeen("", "QiuhuaPlayerModel", key, showLocation, equipmentSlotType, posX, posY, posZ, rotX, rotY, rotZ);
            this.geckoEntityItemBeenList.add(geckoEntityItemBeen);
        });



        // 获取 HandheldItems 部分
        ConfigurationSection handheldItemsSection = modelConfig.getConfigurationSection("AnimationsController.HandheldItems");
        // 检查是否为 null
        if (handheldItemsSection != null) {
            // 获取全部键
            for (String key : handheldItemsSection.getKeys(false)) {
                // 拿到每个键对应的 ConfigurationSection
                ConfigurationSection section = handheldItemsSection.getConfigurationSection(key);
                if (section != null) {
                    this.handheldItemsData.put(key, new HandheldItemsData(section));
//                    Main.getMainPlugin().getLogger().info("模型" + this.thirdModel + "添加动作类型->" + key);
                }
            }
        }
    }

    public void sendModel(Player player){
        String uuidString = player.getUniqueId().toString();
        GeckoEntityModelBeen geckoEntityModelBeen = new GeckoEntityModelBeen(uuidString, "QiuhuaPlayerModel", this.modelID, true, this.originalMaterial);
        List<GeckoEntityItemBeen> geckoEntityItemBeenList = new ArrayList<>();
        this.geckoEntityItemBeenList.forEach((geckoEntityItemBeen) -> {
            GeckoEntityItemBeen geckoEntityItemBeen1 = geckoEntityItemBeen.copy();
            geckoEntityItemBeen1.setUuidString(uuidString);
            geckoEntityItemBeenList.add(geckoEntityItemBeen1);
        });

        if(this.thirdModelEnable){
            UnrealCoreAPI.inst().getEntityHelper().setEntityModel(geckoEntityModelBeen);
            UnrealCoreAPI.inst().getEntityHelper().setEntityItem(geckoEntityItemBeenList);
            EntityDisplayBeen entityDisplayBeen = new EntityDisplayBeen(uuidString, false);
            UnrealCoreAPI.inst().getEntityHelper().setEntityDisplay(entityDisplayBeen);
            UnrealCoreAPI.inst().getEntityHelper().setEntityHiddenParts(this.thirdRemovePart);
        }else {
            GeckoModelRemoveBeen geckoModelRemoveBeen = new GeckoModelRemoveBeen(uuidString, "QiuhuaPlayerModel");
            UnrealCoreAPI.inst().getEntityHelper().removeEntityModel(geckoModelRemoveBeen);
            EntityDisplayBeen entityDisplayBeen = new EntityDisplayBeen(uuidString, true);
            UnrealCoreAPI.inst().getEntityHelper().setEntityDisplay(entityDisplayBeen);
        }
        if(this.firstModelEnable){
            UnrealCoreAPI.inst(player).getPlayerHelper().setFirstViewModel(geckoEntityModelBeen);
            UnrealCoreAPI.inst(player).getPlayerHelper().setFirstViewItem(geckoEntityItemBeenList);
            UnrealCoreAPI.inst(player).getPlayerHelper().setFirstViewHiddenParts(this.firstRemovePart);
            UnrealCoreAPI.inst(player).getPlayerHelper().setFirstViewPlace(new FirstViewPlaceBeen(this.firstDistance, this.firstHeight));
        }else{
            UnrealCoreAPI.inst(player).getPlayerHelper().removeFirstViewModel();
        }
    }

    //给玩家发送目标玩家的模型
    public void sendModel(Player player, Player tPlayer){
        String uuidString = tPlayer.getUniqueId().toString();
        GeckoEntityModelBeen geckoEntityModelBeen = new GeckoEntityModelBeen(uuidString, "QiuhuaPlayerModel", this.modelID, true, this.originalMaterial);
        List<GeckoEntityItemBeen> geckoEntityItemBeenList = new ArrayList<>();
        this.geckoEntityItemBeenList.forEach((geckoEntityItemBeen) -> {
            GeckoEntityItemBeen geckoEntityItemBeen1 = geckoEntityItemBeen.copy();
            geckoEntityItemBeen1.setUuidString(uuidString);
            geckoEntityItemBeenList.add(geckoEntityItemBeen1);
        });

        if(this.thirdModelEnable){
            UnrealCoreAPI.inst().getEntityHelper().setEntityModel(geckoEntityModelBeen);
            UnrealCoreAPI.inst().getEntityHelper().setEntityItem(geckoEntityItemBeenList);
            EntityDisplayBeen entityDisplayBeen = new EntityDisplayBeen(uuidString, false);
            UnrealCoreAPI.inst().getEntityHelper().setEntityDisplay(entityDisplayBeen);
            UnrealCoreAPI.inst().getEntityHelper().setEntityHiddenParts(this.thirdRemovePart);
        }else {
            GeckoModelRemoveBeen geckoModelRemoveBeen = new GeckoModelRemoveBeen(uuidString, "QiuhuaPlayerModel");
            UnrealCoreAPI.inst().getEntityHelper().removeEntityModel(geckoModelRemoveBeen);
            EntityDisplayBeen entityDisplayBeen = new EntityDisplayBeen(uuidString, true);
            UnrealCoreAPI.inst().getEntityHelper().setEntityDisplay(entityDisplayBeen);
        }



    }




    public HandheldItemsData getHandheldItems(String id) {
        return this.handheldItemsData.get(id);
    }

    //给玩家设置自定义动作
    public void setDIYAnimations(Player player, ItemStack item){
        if(item != null && item.getType() != Material.AIR){
            for(String key : this.handheldItemsData.keySet()){
//                player.sendMessage("检测动作->" + key);
//                player.sendMessage(String.valueOf(this.handheldItemsData));
                HandheldItemsData value = this.handheldItemsData.get(key);
                //如果物品符号条件那他会返回true
                Boolean isAvailabl = value.isAvailable(item);
                if(isAvailabl){
                    value.sendAnimations(player);
                    PlayerModelData.getPlayerData(player).setAnimationsId(key);
//                    player.sendMessage("为你添加动作 " + key);
                    return;
                }
            }
        }
        PlayerModelData.getPlayerData(player).setDefaultAnimations();
    }


    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public void setFirstModelEnable(boolean firstModelEnable) {
        this.firstModelEnable = firstModelEnable;
    }

    public void setThirdModelEnable(boolean thirdModelEnable) {
        this.thirdModelEnable = thirdModelEnable;
    }

    public void setOriginalMaterial(boolean originalMaterial) {
        this.originalMaterial = originalMaterial;
    }

    public void setFirstDistance(float firstDistance) {
        this.firstDistance = firstDistance;
    }

    public void setFirstHeight(float firstHeight) {
        this.firstHeight = firstHeight;
    }

    public void setFirstRemovePart(List<GeckoEntityHiddenPartsBeen> firstRemovePart) {
        this.firstRemovePart = firstRemovePart;
    }

    public void setThirdRemovePart(List<GeckoEntityHiddenPartsBeen> thirdRemovePart) {
        this.thirdRemovePart = thirdRemovePart;
    }

    public void setGeckoEntityItemBeenList(List<GeckoEntityItemBeen> geckoEntityItemBeenList) {
        this.geckoEntityItemBeenList = geckoEntityItemBeenList;
    }

    public String getModelID() {
        return this.modelID;
    }

    public boolean isFirstModelEnable() {
        return this.firstModelEnable;
    }

    public boolean isThirdModelEnable() {
        return this.thirdModelEnable;
    }

    public boolean isOriginalMaterial() {
        return this.originalMaterial;
    }

    public float getFirstDistance() {
        return this.firstDistance;
    }

    public float getFirstHeight() {
        return this.firstHeight;
    }

    public List<GeckoEntityHiddenPartsBeen> getFirstRemovePart() {
        return this.firstRemovePart;
    }

    public List<GeckoEntityHiddenPartsBeen> getThirdRemovePart() {
        return this.thirdRemovePart;
    }

    public List<GeckoEntityItemBeen> getGeckoEntityItemBeenList() {
        return this.geckoEntityItemBeenList;
    }





}
