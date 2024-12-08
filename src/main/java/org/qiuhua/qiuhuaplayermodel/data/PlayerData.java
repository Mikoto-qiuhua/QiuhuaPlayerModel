package org.qiuhua.qiuhuaplayermodel.data;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.been.entity.type.LivingEntityPoseType;
import com.daxton.unrealcore.been.player.PoseAnimationsBeen;
import com.daxton.unrealcore.been.player.type.PlayerBodyType;
import org.bukkit.entity.Player;
import org.qiuhua.qiuhuaplayermodel.Tool.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    //玩家当前的模型id
    private String modelId = "";
    //玩家当前的动作
    private String animationsId;

    private Player player;
    private ConcurrentHashMap<String,String> animationsMap = new ConcurrentHashMap<>();
    public String getModelId() {
        return this.modelId;
    }
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public PlayerData(Player player){
        this.player = player;
    }


    public String getAnimationsId() {
        return this.animationsId;
    }
    public void setAnimationsId(String animationsId) {
        this.animationsId = animationsId;
    }

    public void addAnimations(String key,String value){
        this.animationsMap.put(key, value);
    }

    public void clearAnimations(){
        this.animationsMap.clear();
    }

    public ConcurrentHashMap<String, String> getAnimationsMap() {
        return this.animationsMap;
    }

    /**
     * 将当前的动作发给指定玩家
     * @param player 给谁看
     */
    public void sendAnimations(Player player){
        List<PoseAnimationsBeen> poseAnimationsBeenList = new ArrayList<>();
        this.animationsMap.forEach((key,value) ->{
            poseAnimationsBeenList.add(new PoseAnimationsBeen(this.player.getUniqueId().toString(),LivingEntityPoseType.valueOf(key), value));
        });
        UnrealCoreAPI.inst().getEntityHelper().setPoseAnimations(poseAnimationsBeenList);
    }


    //将玩家动作设置成默认
    public void setDefaultAnimations(){
        String uuid = this.player.getUniqueId().toString();
        List<PoseAnimationsBeen> poseAnimationsBeenList = new ArrayList<>();
        this.animationsMap.forEach((key,value) ->{
            String defaultAnimationsId = Config.getDefaultAnimationsId(key);
            if(defaultAnimationsId != null) {
                poseAnimationsBeenList.add(new PoseAnimationsBeen(this.player.getUniqueId().toString(),LivingEntityPoseType.valueOf(key), defaultAnimationsId));
            }
        });
        UnrealCoreAPI.inst().getEntityHelper().setPoseAnimations(poseAnimationsBeenList);
        this.animationsMap.clear();
//      player.sendMessage("为你恢复默认动作");
        this.animationsId = "";
    }


}
