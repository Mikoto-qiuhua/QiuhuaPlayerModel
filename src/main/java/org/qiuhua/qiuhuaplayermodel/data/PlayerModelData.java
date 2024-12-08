package org.qiuhua.qiuhuaplayermodel.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerModelData {


    private static final ConcurrentHashMap<UUID, PlayerData> allPlayerModelData = new ConcurrentHashMap<>();


    public static Boolean isPlayerModelData(Player player){
        UUID uuid = player.getUniqueId();
        return allPlayerModelData.containsKey(uuid);
    }


    public static ConcurrentHashMap<UUID, PlayerData> getAllPlayerModelData(){
        return allPlayerModelData;
    }

    public static PlayerData getPlayerData(Player player){
        UUID uuid = player.getUniqueId();
        if(!allPlayerModelData.containsKey(uuid)){
            PlayerData data = new PlayerData(player);
            allPlayerModelData.put(uuid, data);
        }
        return allPlayerModelData.get(uuid);

    }

    public static PlayerData getPlayerData(UUID uuid){
        if(!allPlayerModelData.containsKey(uuid)){
            PlayerData data = new PlayerData(Bukkit.getPlayer(uuid));
            allPlayerModelData.put(uuid, data);
        }
        return allPlayerModelData.get(uuid);

    }

    public static String getModelId(Player player){
        return getPlayerData(player).getModelId();
    }

    public static void setModelId(Player player, String modelId){
        getPlayerData(player).setModelId(modelId);
    }

}
