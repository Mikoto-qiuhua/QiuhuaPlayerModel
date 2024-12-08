package org.qiuhua.qiuhuaplayermodel.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.qiuhua.genshinrolesystem.api.event.SwitchEvent;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.Tool.Config;
import org.qiuhua.qiuhuaplayermodel.model.PlayerModelController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GenshinRoleSystemListener implements Listener {



    public void register() {
        if(Config.getConfig().getBoolean("GenshinRoleSystem.Enable")){
            Bukkit.getPluginManager().registerEvents(this, Main.getMainPlugin());
        }
    }

    //切换角色事件
    @EventHandler
    public void onSwitchEvent(SwitchEvent event){
        if(!Config.getConfig().getBoolean("GenshinRoleSystem.Enable")){
            return;
        }
        Player player = event.getPlayer();
        String roleId = event.getRoleId();
        UUID uuid = player.getUniqueId();
        PlayerModelController.delModel(player, true);
        if(roleId == null || roleId.equals("") || roleId.equals("无角色")){
            PlayerModelController.delModel(player, true);
            return;
        }
        //获取这个角色的对应模型
        String modelId = Config.getStr("GenshinRoleSystem.RoleId." + roleId);
        if(modelId == null || modelId.equals("")){
            PlayerModelController.delModel(player, true);
            return;
        }
        PlayerModelController.addModel(player,modelId);
    }
}
