package org.qiuhua.qiuhuaplayermodel.data;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.been.entity.type.LivingEntityPoseType;
import com.daxton.unrealcore.been.player.PoseAnimationsBeen;
import com.daxton.unrealcore.been.player.type.PlayerBodyType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.Tool.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HandheldItemsData {

    private String itemType = "";

    private String itemName = "";

    private String itemLore = "";

    public Boolean isAvailable(ItemStack item){
        Material type = item.getType();
        String name = Tool.removeColorCode(item.getItemMeta().getDisplayName());
        List<String> lore = item.getItemMeta().getLore();
        // 检查类型
        if (!itemType.isEmpty() && type != Material.valueOf(itemType)) {
//            Main.getMainPlugin().getLogger().info("物品类型判断不通过 手持:" + type + "   需要的:" + itemType);
            return false;
        }
        // 检查名称
        if (!itemName.isEmpty() && !name.equals(itemName)) {
//            Main.getMainPlugin().getLogger().info("物品名字判断不通过 手持:" + name + "   需要的:" + itemName);
            return false;
        }
        // 检查Lore
        if (!itemLore.isEmpty()) {
            if(lore == null){
//                Main.getMainPlugin().getLogger().info("物品名字判断不通过手持物品没有lore");
                return false;
            }
            if(lore.isEmpty()){
//                Main.getMainPlugin().getLogger().info("物品名字判断不通过手持物品没有lore");
                return false;
            }
            for(String a : lore){
                a = Tool.removeColorCode(a);
                if(a.equals(itemLore)){
//                    Main.getMainPlugin().getLogger().info("lore判断通过");
                   return true;
                }
            }
//            Main.getMainPlugin().getLogger().info("物品lore判断不通过");
            return false;
        }
//        Main.getMainPlugin().getLogger().info("物品判断通过");
        return true;
    }

    private HashMap<String, String> map = new HashMap<>();

    public HandheldItemsData(ConfigurationSection section){
        this.itemLore = section.getString("Item.lore");
        this.itemName = section.getString("Item.name");
        this.itemType = section.getString("Item.type");
        ConfigurationSection animationsSection = section.getConfigurationSection("AnimationsSwitch");
        // 遍历键值对
        if (animationsSection != null) {
            for (String key : animationsSection.getKeys(false)) {
                String value = animationsSection.getString(key);
                map.put(key, value);
            }
        }
    }

    //把这个玩家的动作发给全部玩家
    public void sendAnimations(Player player){
        String uuid = player.getUniqueId().toString();
        PlayerData playerData = PlayerModelData.getPlayerData(player);
        List<PoseAnimationsBeen> poseAnimationsBeenList = new ArrayList<>();
        map.forEach((key,value) ->{
            poseAnimationsBeenList.add(new PoseAnimationsBeen(uuid,LivingEntityPoseType.valueOf(key), value));
            //记录在玩家的数据里面 方便后期恢复初始动作
            playerData.addAnimations(key,value);
        });
        UnrealCoreAPI.inst().getEntityHelper().setPoseAnimations(poseAnimationsBeenList);
    }


}
