package org.qiuhua.qiuhuaplayermodel.mythicmob;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.qiuhua.qiuhuaplayermodel.animations.AnimationsController;

public class AnimationsMythicMechanic implements ITargetedEntitySkill {
    private final PlaceholderString animationPlaceholder;
    private final PlaceholderInt countPlaceholder;
    private final PlaceholderInt tickPlaceholder;
    private final PlaceholderInt priorityPlaceholder;
    private final PlaceholderDouble speedPlaceholder;

    private final PlaceholderString coverPlaceholder;

    public AnimationsMythicMechanic(MythicLineConfig mlc) {
        this.animationPlaceholder = mlc.getPlaceholderString("key", null);
        this.countPlaceholder = mlc.getPlaceholderInteger("count", 1);
        this.tickPlaceholder = mlc.getPlaceholderInteger("tick", 20);
        this.coverPlaceholder = mlc.getPlaceholderString("cover", "true");
        this.priorityPlaceholder = mlc.getPlaceholderInteger("priority", 3);
        this.speedPlaceholder = mlc.getPlaceholderDouble("speed", 1);
    }


    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target){
        Entity entity = target.getBukkitEntity();
        String animation = this.animationPlaceholder.get(data, target);
        int count = this.countPlaceholder.get(data, target);
        int tick = this.tickPlaceholder.get(data, target);
        int priority = this.priorityPlaceholder.get(data, target);
        double speed = this.speedPlaceholder.get(data, target);
        boolean cover;
        try {
            cover = Boolean.parseBoolean(this.coverPlaceholder.get(data, target));
        } catch (NumberFormatException e) {
            return SkillResult.INVALID_CONFIG;
        }
        if(entity instanceof Player player){
            AnimationsController.playPlayerAnimations(player,animation,tick,count,cover,priority,speed);
        }else {
            AnimationsController.playEntityAnimations(entity,animation,tick,count);
        }
        return SkillResult.SUCCESS;
    }

}
