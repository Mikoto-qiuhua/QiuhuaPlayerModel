package org.qiuhua.qiuhuaplayermodel;

import org.bukkit.plugin.java.JavaPlugin;
import org.qiuhua.qiuhuaplayermodel.Tool.Tool;
import org.qiuhua.qiuhuaplayermodel.command.QiuhuaPlayerModelCommand;
import org.qiuhua.qiuhuaplayermodel.listener.GenshinRoleSystemListener;
import org.qiuhua.qiuhuaplayermodel.listener.MythicMobsListener;
import org.qiuhua.qiuhuaplayermodel.listener.PlayerListener;
import org.qiuhua.qiuhuaplayermodel.playertag.EntityTabController;

public class Main extends JavaPlugin {
    private static Main mainPlugin;
    public static Main getMainPlugin(){
        return mainPlugin;
    }


    //启动时运行
    @Override
    public void onEnable(){
        //设置主插件
        mainPlugin = this;
        Tool.saveAllConfig();
        Tool.load();
        EntityTabController.load();
        new QiuhuaPlayerModelCommand().register();
        new PlayerListener().register();
        new MythicMobsListener().register();
        new GenshinRoleSystemListener().register();
    }


    //关闭时运行
    @Override
    public void onDisable(){


    }

    //执行重载命令时运行
    @Override
    public void reloadConfig(){
        Tool.reload();
        EntityTabController.reload();
    }


}