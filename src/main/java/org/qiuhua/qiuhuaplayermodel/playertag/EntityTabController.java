package org.qiuhua.qiuhuaplayermodel.playertag;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.application.method.SchedulerRunnable;
import com.daxton.unrealcore.been.effects.entity.EntityEffectsTextBeen;
import com.daxton.unrealcore.been.effects.remove.EntityEffectsRemoveBeen;
import com.daxton.unrealcore.been.world.been.LocationBeen;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.Tool.Config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EntityTabController {
    public static ConcurrentHashMap<String ,EntityEffectsTextBeen> playerEffectsTextBeenDefaultList = new ConcurrentHashMap<>();
    public static SchedulerRunnable schedulerRunnable;


    public static void load() {
        if(!Config.getConfig().getBoolean("PlayerNameTag.Enable", false)){
            return;
        }
        //获取配置信息
        ConfigurationSection configurationSection = Config.getConfig().getConfigurationSection("PlayerNameTag.Tag");
        for(String tag : configurationSection.getKeys(false)){
            ConfigurationSection tagSection = configurationSection.getConfigurationSection(tag);
            if(tagSection != null){
                String text = tagSection.getString("Text", "");
                text = text.replace("&", "§");
                String positionX = tagSection.getString("PositionX", "0");
                String positionY = tagSection.getString("PositionY", "0");
                String positionZ = tagSection.getString("PositionZ", "0");
                String rotateX = tagSection.getString("RotateX", "0");
                String rotateY = tagSection.getString("RotateY", "0");
                String rotateZ = tagSection.getString("RotateZ", "0");
                String size = tagSection.getString("Size", "1");
                LocationBeen locationBeen = new LocationBeen(positionX, positionY, positionZ, rotateX, rotateY, rotateZ, size);
                EntityEffectsTextBeen entityEffectsTextBeen = new EntityEffectsTextBeen("", "PlayerTab_" + tag, text, "0.2", true, locationBeen);
                playerEffectsTextBeenDefaultList.put(tag, entityEffectsTextBeen);
            }
        }
        placeholderChange();
    }


    public static void reload() {
        if(!Config.getConfig().getBoolean("PlayerNameTag.Enable", false)){
            return;
        }
        schedulerRunnable.cancel();
        //给玩家清理掉旧的文本数据
        playerEffectsTextBeenDefaultList.forEach((tag, been) -> {
            List<EntityEffectsRemoveBeen> entityEffectsRemoveBeenList = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach((player) -> {
                String uuidString = player.getUniqueId().toString();
                EntityEffectsRemoveBeen entityEffectsRemoveBeen = new EntityEffectsRemoveBeen(uuidString, been.getMark());
                entityEffectsRemoveBeenList.add(entityEffectsRemoveBeen);
            });
            UnrealCoreAPI.inst().getEntityEffectsHelper().removeText(entityEffectsRemoveBeenList);
            playerEffectsTextBeenDefaultList.clear();
        });
        load();
    }

    //给玩家刷新papi
    public static void placeholderChange() {
        int refresh = Config.getConfig().getInt("PlayerNameTag.Refresh", 5);
        schedulerRunnable = new SchedulerRunnable() {
            @Override
            public void run() {
                playerEffectsTextBeenDefaultList.forEach((tag, been) -> {
                    // 遍历所有在线玩家
                    Iterator<? extends Player> playerList = Bukkit.getOnlinePlayers().iterator();
                    Player p;
                    //这里针对每个玩家都进行处理 嵌套一个迭代
                    //因为不需要给自己发送这些数据信息
                    while(playerList.hasNext()) {
                        List<EntityEffectsTextBeen> effectsTextBeenList = new ArrayList<>();
                        p = playerList.next();
                        //再次遍历玩家 这里如果是当前玩家 那就不添加进去数据
                        Player finalP = p;
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            if(player != finalP){
                                String uuidString = player.getUniqueId().toString();
                                // 复制实体效果文本对象
                                EntityEffectsTextBeen entityEffectsTextBeen = been.copy();
                                entityEffectsTextBeen.setUuidString(uuidString);

                                // 获取文本值并设置占位符
                                String value = entityEffectsTextBeen.getText(0);
                                entityEffectsTextBeen.setText(0, PlaceholderAPI.setPlaceholders(player, value));

                                // 将处理后的实体效果文本对象添加到列表中
                                effectsTextBeenList.add(entityEffectsTextBeen);
                            }
                        });
                        UnrealCoreAPI.inst(p).getEntityEffectsHelper().addText(effectsTextBeenList);
                    }
                });
            }
        };
        schedulerRunnable.runTimerAsync(Main.getMainPlugin(),refresh, refresh);
    }


}
