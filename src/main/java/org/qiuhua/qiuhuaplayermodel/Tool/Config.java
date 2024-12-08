package org.qiuhua.qiuhuaplayermodel.Tool;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.model.PlayerModelBeen;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static FileConfiguration config;

    public static String getStr(String val){
        return config.getString(val);
    }

    public static Map<String, String> defaultAnimationsMap = new HashMap<>();

    public static int getInt(String val){
        return config.getInt(val);
    }


    public static void load(){
        config = Tool.load(new File(Main.getMainPlugin().getDataFolder(),"Config.yml"));
        defaultAnimationsMap.clear();
        ConfigurationSection animationsSection = config.getConfigurationSection("DefaultAnimations");
        if (animationsSection != null) {
            for(String key: animationsSection.getKeys(false)){
                defaultAnimationsMap.put(key, animationsSection.getString(key));
            }
        }
    }

    public static FileConfiguration getConfig(){
        return config;
    }


    public static String getDefaultAnimationsId(String key){
        if(defaultAnimationsMap.containsKey(key)){
            return defaultAnimationsMap.get(key);
        }
        return null;
    }
}
