package org.qiuhua.qiuhuaplayermodel.model;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.animations.AnimationsController;
import org.qiuhua.qiuhuaplayermodel.data.PlayerModelData;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class PlayerModelController {

    //模型名称和模型对象
    public static Map<String, PlayerModelBeen> playerModelBeenMap = new HashMap<>();


    private static final File folder = new File(Main.getMainPlugin().getDataFolder(), "Model");

    public static File getFolder(){
        return folder;
    }


    //加载模型的全部配置文件
    public static void loadModelFiles(File folder){
        // 检查文件夹是否存在且是一个文件夹
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            // 遍历文件夹中的所有文件和文件夹
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 如果是文件夹，则递归调用该方法，传入文件夹作为参数
                        loadModelFiles(file);
                    } else if (file.isFile() && file.getName().endsWith(".yml")) {
                        // 检查文件是否是一个文件且以 .yml 结尾
                        // 加载文件的配置
                        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                        String fileName = file.getName().replace(".yml", "");
                        // 将加载的配置存入 Map 中，键为文件名（去除 .yml 后缀），值为配置对象
                        PlayerModelBeen playerModelBeen = new PlayerModelBeen(config);
                        playerModelBeenMap.put(fileName, playerModelBeen);
//                        Main.getMainPlugin().getLogger().info("读取模型配置 -> " + fileName);
                    }
                }
            }
        }
    }




    //使用模型id添加
    public static void addModel(Player player, String modelId){
        if (playerModelBeenMap.containsKey(modelId)) {
            PlayerModelData.setModelId(player, modelId);
            PlayerModelBeen playerModelBeen = playerModelBeenMap.get(modelId);
            playerModelBeen.sendModel(player);
            //给玩家设置模型动作
            PlayerModelController.playerModelBeenMap.get(modelId).setDIYAnimations(player, player.getInventory().getItemInMainHand());
        }
    }



    //使用存储的数据添加
    public static void addModel(Player player){
        String modelId = PlayerModelData.getModelId(player);
        if (playerModelBeenMap.containsKey(modelId)) {
            PlayerModelBeen playerModelBeen = playerModelBeenMap.get(modelId);
            playerModelBeen.sendModel(player);
            //给玩家设置模型动作
            PlayerModelController.playerModelBeenMap.get(modelId).setDIYAnimations(player, player.getInventory().getItemInMainHand());
        }
    }



    //a 是true就删除这个玩家的模型记录
    public static void delModel(Player player, Boolean isRemove){
        String uuid = player.getUniqueId().toString();
        if(isRemove){
            PlayerModelData.getPlayerData(player).setDefaultAnimations();
            PlayerModelData.getAllPlayerModelData().remove(player.getUniqueId());
        }
        UnrealCoreAPI.inst().getPlayerHelper().modelRemove(uuid);
        UnrealCoreAPI.inst(player).getPlayerHelper().handModelRemove();
    }


    public static Set<String> getKeyList(){
        return playerModelBeenMap.keySet();
    }


}
