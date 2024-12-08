package org.qiuhua.qiuhuaplayermodel.command;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.been.effects.entity.EntityEffectsImageBeen;
import com.daxton.unrealcore.been.world.been.LocationBeen;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.qiuhua.qiuhuaplayermodel.Main;
import org.qiuhua.qiuhuaplayermodel.model.PlayerModelController;
import org.qiuhua.qiuhuaplayermodel.animations.AnimationsController;

import java.util.ArrayList;
import java.util.List;

public class QiuhuaPlayerModelCommand implements CommandExecutor, TabExecutor {

    public void register() {
        Bukkit.getPluginCommand("QiuhuaPlayerModel").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        switch (args[0]){
            case "reload":
                if(sender.hasPermission("qiuhuaplayermodel.reload")){
                    Main.getMainPlugin().reloadConfig();
                    sender.sendMessage("重载完成");
                }
                break;
            case "addmodel":
                player = Bukkit.getPlayer(args[2]);
                if(player != null && sender.hasPermission("qiuhuaplayermodel.addmodel")){
                    PlayerModelController.addModel(player, args[1]);
                }
                break;
            case "delmodel":
                player = Bukkit.getPlayer(args[1]);
                if(player != null && sender.hasPermission("qiuhuaplayermodel.delmodel")){
                    PlayerModelController.delModel(player, true);
                }
                break;
            case "animations":
                player = Bukkit.getPlayer(args[1]);
                if(player != null && sender.hasPermission("qiuhuaplayermodel.animations")){
                    AnimationsController.playPlayerAnimations(player, args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]), Boolean.parseBoolean(args[5]), Integer.parseInt(args[6]), Double.parseDouble(args[7]));
                }
                break;
        }
        return true;
    }
    
    
    
    
    

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player){
            List<String> result = new ArrayList<>();
            if(args.length == 1){
                if (player.hasPermission("qiuhuaplayermodel.reload")){
                    result.add("reload");
                }
                if (player.hasPermission("qiuhuaplayermodel.addmodel")){
                    result.add("addmodel");
                }
                if (player.hasPermission("qiuhuaplayermodel.delmodel")){
                    result.add("delmodel");
                }
                if (player.hasPermission("qiuhuaplayermodel.animations")){
                    result.add("animations");
                }

                return result;
            }
            if(args[0].equals("addmodel") && args.length == 2 && player.hasPermission("qiuhuaplayermodel.addmodel")){
                result.addAll(PlayerModelController.getKeyList());
                return result;
            }
            if(player.hasPermission("qiuhuaplayermodel.animations")){
                if(args[0].equals("animations") && args.length == 2){
                    Bukkit.getOnlinePlayers().forEach((playerName) -> {
                        result.add(playerName.getName());
                    });
                    return result;
                }
                if(args[0].equals("animations") && args.length == 3){
                    result.add("动作的id");
                    return result;
                }
                if(args[0].equals("animations") && args.length == 4){
                    result.add("动作的持续时间 数字 tick单位");
                    return result;
                }
                if(args[0].equals("animations") && args.length == 5){
                    result.add("动作播放次数 数字");
                    return result;
                }
                if(args[0].equals("animations") && args.length == 6){
                    result.add("是否覆盖当前动作 true/false");
                    return result;
                }
                if(args[0].equals("animations") && args.length == 7){
                    result.add("设置动作上下半身   1上半身 2下半身 3全身");
                    return result;
                }
                if(args[0].equals("animations") && args.length == 8){
                    result.add("播放速度");
                    return result;
                }
            }
        }
        return null;
    }
}
