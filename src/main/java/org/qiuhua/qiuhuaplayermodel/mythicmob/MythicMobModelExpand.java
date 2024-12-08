package org.qiuhua.qiuhuaplayermodel.mythicmob;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.been.entity.base.EntityBoundBox;
import com.daxton.unrealcore.been.entity.base.EntityDisplayBeen;
import com.daxton.unrealcore.been.entity.gecko.GeckoEntityModelBeen;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.qiuhua.qiuhuaplayermodel.data.MythicMobModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MythicMobModelExpand {

    /**
     * 尝试给mm实体添加模型文件 会查找对应的选项 没有则不添加
     * @param entity
     */
    public static void addModel(Entity entity, String modelId, String hitBox){
        if(modelId == null || modelId.equals("")){
            return;
        }
        UUID uuid = entity.getUniqueId();
        MythicMobModelData.getEntityModelData().put(uuid, modelId);
        GeckoEntityModelBeen geckoEntityModelBeen = new GeckoEntityModelBeen(uuid.toString(), "QiuhuaPlayerModel", modelId);
        UnrealCoreAPI.inst().getEntityHelper().setEntityModel(geckoEntityModelBeen);
        EntityDisplayBeen entityDisplayBeen = new EntityDisplayBeen(uuid.toString(), false);
        UnrealCoreAPI.inst().getEntityHelper().setEntityDisplay(entityDisplayBeen);
        if(hitBox != null && !hitBox.equals("")){
            String[] parts = hitBox.split(",");
            if(parts.length == 3){
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
                EntityBoundBox entityBoundBox = new EntityBoundBox(entity.getUniqueId().toString(), x, y, z);
                UnrealCoreAPI.inst().getEntityHelper().setEntityBoundBox(entityBoundBox);
                UnrealCoreAPI.inst().getEntityHelper().setEntityBoundBoxServer(entityBoundBox);
            }
        }
    }



    //开服时获取全部实体 检查是不是mm实体后 在去尝试给他套模型
    public static void startServerLoadModel(List<Entity> entities){
        for (Entity entity : entities){
            if(MythicMobModelData.getEntityModelData().containsKey(entity.getUniqueId())){
                continue;
            }
            if(MythicBukkit.inst().getMobManager().isMythicMob(entity)){
                Optional<ActiveMob> activeMob = MythicBukkit.inst().getMobManager().getActiveMob(entity.getUniqueId());
                if (activeMob.isPresent()){
                    String modelId = activeMob.get().getType().getConfig().getString("ModelId");
                    String hitBox = activeMob.get().getType().getConfig().getString("HitBox");
                    MythicMobModelExpand.addModel(entity, modelId, hitBox);
                }
            }
        }
    }




    /**
     * 移除实体身上的全部模型
     * @param entity
     */
    public static void removeModel(Entity entity){
        UUID uuid = entity.getUniqueId();
        ConcurrentHashMap<UUID, String> data = MythicMobModelData.getEntityModelData();
        if(data.containsKey(uuid)){
            MythicMobModelData.getEntityModelData().remove(uuid);
            UnrealCoreAPI.inst().getEntityHelper().removeAllEntityModel(uuid.toString());
        }
    }

    /**
     * 给这个玩家发送当前已存在的实体模型  玩家进服时必须发 才能看见模型
     * @param player
     */
    public static void sendModel(Player player) {
        List<GeckoEntityModelBeen> geckoEntityModelBeenList = new ArrayList<>();
        List<EntityDisplayBeen> entityDisplayBeenList = new ArrayList<>();
        List<EntityBoundBox> entityBoundBoxList = new ArrayList<>();
        MythicMobModelData.getEntityModelData().forEach((uuid, modelId) -> {
            GeckoEntityModelBeen geckoEntityModelBeen = new GeckoEntityModelBeen(uuid.toString(), "QiuhuaPlayerModel", modelId);
            EntityDisplayBeen entityDisplayBeen = new EntityDisplayBeen(uuid.toString(), false);
            geckoEntityModelBeenList.add(geckoEntityModelBeen);
            entityDisplayBeenList.add(entityDisplayBeen);

            Optional<ActiveMob> activeMob = MythicBukkit.inst().getMobManager().getActiveMob(uuid);
            if(activeMob.isPresent()){
                String hitBox = activeMob.get().getType().getConfig().getString("HitBox");
                if(hitBox != null && !hitBox.equals("")){
                    String[] parts = hitBox.split(",");
                    if(parts.length == 3){
                        double x = Double.parseDouble(parts[0]);
                        double y = Double.parseDouble(parts[1]);
                        double z = Double.parseDouble(parts[2]);
                        EntityBoundBox entityBoundBox = new EntityBoundBox(uuid.toString(), x, y, z);
                        entityBoundBoxList.add(entityBoundBox);
                    }
                }

            }
        });
        UnrealCoreAPI.inst().getEntityHelper().setEntityBoundBox(entityBoundBoxList);
        UnrealCoreAPI.inst().getEntityHelper().setEntityBoundBoxServer(entityBoundBoxList);
        UnrealCoreAPI.inst(player).getEntityHelper().setEntityModel(geckoEntityModelBeenList);
        UnrealCoreAPI.inst(player).getEntityHelper().setEntityDisplay(entityDisplayBeenList);
    }


}
