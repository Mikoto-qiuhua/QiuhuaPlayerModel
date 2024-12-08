package org.qiuhua.qiuhuaplayermodel.listener;


import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.plugin.Plugin;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.mythicmob.AnimationsMythicMechanic;
import org.qiuhua.qiuhuaplayermodel.mythicmob.MythicMobModelExpand;

public class MythicMobsListener implements Listener {


    public void register(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("MythicMobs");
        if(plugin != null && plugin.isEnabled()){
            Bukkit.getPluginManager().registerEvents(this, Main.getMainPlugin());
        }
    }



    //当实体尝试被加载时给他们套模型
    @EventHandler
    public void onEntitiesLoadEvent(EntitiesLoadEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(Main.getMainPlugin(), new Runnable() {
            @Override
            public void run() {
                MythicMobModelExpand.startServerLoadModel(event.getEntities());
            }
        });
    }




    //在怪物生成时为怪物套上模型
    @EventHandler
    public void onMythicMobSpawn(MythicMobSpawnEvent event) {
        if(event.isCancelled()){
            return;
        }
        Entity entity = event.getEntity();
        String modeId = event.getMobType().getConfig().getString("ModelId");
        String hitBox = event.getMobType().getConfig().getString("HitBox");

        MythicMobModelExpand.addModel(entity, modeId, hitBox);
    }

    //在怪物死亡时卸载模型
    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event) {
        Entity entity = event.getEntity();
        MythicMobModelExpand.removeModel(entity);
    }


    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent e){
        if(e.getMechanicName().equalsIgnoreCase("qh-animations") || e.getMechanicName().equalsIgnoreCase("qhanimations")){
            e.register(new AnimationsMythicMechanic(e.getConfig()));
        }
    }

}
