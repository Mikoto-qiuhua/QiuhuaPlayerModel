package org.qiuhua.qiuhuaplayermodel.listener;




import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.common.event.PlayerKeyBoardEvent;
import com.daxton.unrealcore.communication.event.PlayerConnectionDisconnectionEvent;
import com.daxton.unrealcore.epicfight.event.EpicFightColliderHitEvent;
import com.daxton.unrealresource.event.UnrealResourceLoadFinishEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.Tool.BBModel;
import org.qiuhua.qiuhuaplayermodel.Tool.Config;
import org.qiuhua.qiuhuaplayermodel.camera.PlayerCamera;
import org.qiuhua.qiuhuaplayermodel.model.PlayerModelBeen;
import org.qiuhua.qiuhuaplayermodel.model.PlayerModelController;
import org.qiuhua.qiuhuaplayermodel.data.PlayerModelData;
import org.qiuhua.qiuhuaplayermodel.mythicmob.MythicMobModelExpand;

import java.util.UUID;


public class PlayerListener implements Listener {


    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Main.getMainPlugin());
    }


    //玩家材质包加载完毕时
    @EventHandler
    public void onUnrealResourceLoadFinishEvent(UnrealResourceLoadFinishEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getMainPlugin(), new Runnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                //修改视角
                PlayerCamera.sendPlayer(player);
                //发送模型文件给玩家
                BBModel.sendModel(player);
                //给这个玩家发其他玩家的模型加载
                for(UUID uuid : PlayerModelData.getAllPlayerModelData().keySet()){
                    String modelId = PlayerModelData.getAllPlayerModelData().get(uuid).getModelId();
                    PlayerModelBeen playerModelBeen = PlayerModelController.playerModelBeenMap.get(modelId);
                    if(playerModelBeen != null){
                        Player aplayer = Bukkit.getPlayer(uuid);
                        if(aplayer == null){
                            PlayerModelData.getAllPlayerModelData().remove(uuid);
                            continue;
                        }
                        playerModelBeen.sendModel(player ,aplayer);
                    }
                    //发送当前动作
                    String animationsId = PlayerModelData.getAllPlayerModelData().get(uuid).getAnimationsId();
                    if(animationsId != null && !animationsId.equals("")){
                        PlayerModelData.getPlayerData(uuid).sendAnimations(player);
                    }
                }
                //如果玩家有模型id 就加载
                if(PlayerModelData.isPlayerModelData(player)){
                    //Main.getMainPlugin().getLogger().info("已为玩家 -> " + player.getUniqueId() + " 添加模型 " + PlayerModelData.getAllPlayerModelData().get(player.getUniqueId()));
                    PlayerModelController.addModel(player);
                }else {
                    //如果玩家没有模型id 那就检查是否有默认的模型
                    String defaultModel = Config.getStr("DefaultModel");
                    //Main.getMainPlugin().getLogger().info("已为玩家 -> " + player.getUniqueId() + " 添加模型 DefaultModel");
                    if(defaultModel != null && !defaultModel.equals("")){
                        PlayerModelController.addModel(player, defaultModel);
                    }
                }
                //加载mm模型
                MythicMobModelExpand.sendModel(player);
                if(Config.getConfig().getBoolean("PlayerNameTag.Enable", false)){
                    //隐藏玩家名字
                    UnrealCoreAPI.inst(player).getPlayerHelper().hideTypeNameAdd("entity.minecraft.player");
                }
            }
        });
    }

    //玩家退出服务器
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerModelController.delModel(player, Config.getConfig().getBoolean("QuitRecord"));
    }




    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        ItemStack item = player.getInventory().getItem(newSlot);
        //如果玩家有模型在身上 就尝试切换动作名称
        if(PlayerModelData.isPlayerModelData(player)){
            //获取玩家的模型配置
            String modelId = PlayerModelData.getModelId(player);
            if(PlayerModelController.playerModelBeenMap.containsKey(modelId)){
//                player.sendMessage("你的当前模型->" + modelId);
                //给玩家设置模型动作
                PlayerModelController.playerModelBeenMap.get(modelId).setDIYAnimations(player, item);
            }
        }
    }





}
