package org.qiuhua.qiuhuaplayermodel.Tool;


import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.application.base.ObjectUtil;
import org.bukkit.entity.Player;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.model.PlayerModelBeen;
import org.qiuhua.qiuhuaplayermodel.model.PlayerModelController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BBModel {

    //全部.bbmodel文件的列表
    private static final List<File> fileList = new ArrayList<>();
    private static final File folder = new File(Main.getMainPlugin().getDataFolder(), "BlockBench");


    //加载方案文件夹
    public static void load(){
        // 如果指定文件夹不存在，或者不是一个文件夹，则退出
        if (!folder.exists() || !folder.isDirectory()) {
            Main.getMainPlugin().getLogger().warning("未读取到BlockBench文件夹");
            return;
        }
        //清空
        fileList.clear();
        // 遍历指定文件夹内的所有文件
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if(file.isFile() && file.getName().endsWith(".bbmodel")) {
                // 是Blockbench文件，进行处理
                fileList.add(file);
//                Main.getMainPlugin().getLogger().info("读取bb模型文件 -> " + file.getName());
            }
        }

    }


    //给指定玩家发
    public static void sendModel(Player player){
        for(File file : fileList){
            UnrealCoreAPI.inst(player).getResourceHelper().sendModelResources(file);
        }
    }


    //给全部玩家发
    public static void sendModel(){
        for(File file : fileList){
            UnrealCoreAPI.inst().getResourceHelper().sendModelResources(file);
        }
    }


}
