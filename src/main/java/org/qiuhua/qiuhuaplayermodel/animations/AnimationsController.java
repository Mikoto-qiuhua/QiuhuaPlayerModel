package org.qiuhua.qiuhuaplayermodel.animations;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.been.entity.gecko.GeckoEntityAnimationsBeen;
import com.daxton.unrealcore.been.entity.type.LivingEntityPoseType;
import com.daxton.unrealcore.been.player.type.PlayerBodyType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.qiuhua.qiuhuaplayermodel.data.MythicMobModelData;
import org.qiuhua.qiuhuaplayermodel.data.PlayerModelData;

import java.util.UUID;

public class AnimationsController {



    /**
     *  给玩家播放一个指定动作
     * @param player 玩家对象
     * @param animations  动作的id
     * @param tick  动作的持续时间
     * @param count 动作播放次数
     * @param cover 是否覆盖当前动作
     * @param priority 设置动作上下半身   1上半身 2下半身 3全身
     * @param speed 播放速度
     */
    public static void playPlayerAnimations(Player player, String animations, int tick, int count, boolean cover, int priority, double speed) {
        UUID uuid = player.getUniqueId();
        if(PlayerModelData.getAllPlayerModelData().containsKey(uuid)){
            GeckoEntityAnimationsBeen geckoEntityAnimationsBeen = new GeckoEntityAnimationsBeen(uuid.toString(),"QiuhuaPlayerModel",animations, tick, count, cover);
            geckoEntityAnimationsBeen.setPriority(priority);
            geckoEntityAnimationsBeen.setSpeed(speed);
            UnrealCoreAPI.inst().getEntityHelper().setEntityAnimations(geckoEntityAnimationsBeen);
            UnrealCoreAPI.inst(player).getPlayerHelper().setFirstViewAnimations(geckoEntityAnimationsBeen);

        }
    }


    public static void playEntityAnimations(Entity entity, String animations, int tick, int count) {
        UUID uuid = entity.getUniqueId();
        if(MythicMobModelData.getEntityModelData().containsKey(uuid)){
            GeckoEntityAnimationsBeen geckoEntityAnimationsBeen = new GeckoEntityAnimationsBeen(uuid.toString(), "QiuhuaPlayerModel", animations,tick,count);
            UnrealCoreAPI.inst().getEntityHelper().setEntityAnimations(geckoEntityAnimationsBeen);
        }
    }


}
