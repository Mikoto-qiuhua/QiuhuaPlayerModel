package org.qiuhua.qiuhuaplayermodel.Tool;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.been.entity.base.EntityDisplayBeen;
import com.daxton.unrealcore.been.entity.gecko.GeckoEntityModelBeen;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.camera.PlayerCamera;
import org.qiuhua.qiuhuaplayermodel.data.MythicMobModelData;
import org.qiuhua.qiuhuaplayermodel.model.PlayerModelController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {

    public static void saveAllConfig(){
        //创建一个插件文件夹路径为基础的 并追加下一层。所以此时的文件应该是Config.yml
        //exists 代表是否存在
        if (!(new File(Main.getMainPlugin().getDataFolder(),"Config.yml").exists()))
            Main.getMainPlugin().saveResource("Config.yml", false);
        if (!(new File(Main.getMainPlugin().getDataFolder(), "Model/player_female.yml").exists()))
            Main.getMainPlugin().saveResource("Model/player_female.yml", false);
        if (!(new File (Main.getMainPlugin().getDataFolder() ,"BlockBench/player_female.bbmodel").exists()))
            Main.getMainPlugin().saveResource("BlockBench/player_female.bbmodel", false);
    }

    public static YamlConfiguration load(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }



    //通过预设uuid加载模型
    public static void loadCustomEntityModels(){
        ConfigurationSection section = Config.getConfig().getConfigurationSection("CustomEntityModels");
        if(section == null){
            return;
        }
        for(String modelId : section.getKeys(false)){
            List<GeckoEntityModelBeen> geckoEntityModelBeenList = new ArrayList<>();
            List<String> uuidList =  section.getStringList(modelId);
            List<EntityDisplayBeen> entityDisplayBeenList = new ArrayList<>();
            uuidList.forEach(uuid ->{
                GeckoEntityModelBeen geckoEntityModelBeen = new GeckoEntityModelBeen(uuid, "QiuhuaPlayerModel", modelId);
                EntityDisplayBeen entityDisplayBeen = new EntityDisplayBeen(uuid, false);
                geckoEntityModelBeenList.add(geckoEntityModelBeen);
                entityDisplayBeenList.add(entityDisplayBeen);
                MythicMobModelData.getEntityModelData().put(UUID.fromString(uuid), modelId);
            });
            UnrealCoreAPI.inst().getEntityHelper().setEntityModel(geckoEntityModelBeenList);
            UnrealCoreAPI.inst().getEntityHelper().setEntityDisplay(entityDisplayBeenList);
        }
    }


    //加载文件
    public static void load(){
        Config.load();
        BBModel.load();
        //加载自定义uuid实体
        loadCustomEntityModels();
        PlayerModelController.loadModelFiles(PlayerModelController.getFolder());
    }

    //执行重载命令
    public static void reload(){
        load();
        Bukkit.getOnlinePlayers().forEach((player) -> {
            String uuidString = player.getUniqueId().toString();
            UnrealCoreAPI.inst().getPlayerHelper().modelRemove(uuidString);
            UnrealCoreAPI.inst(player).getPlayerHelper().handModelRemove();
            BBModel.sendModel(player);
            PlayerModelController.addModel(player);
            PlayerCamera.sendPlayer(player);
        });

    }


    //去除颜色代码
    public static String removeColorCode(String str){
        Pattern pattern = Pattern.compile("§[0-9a-fklmnor]");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }


}
