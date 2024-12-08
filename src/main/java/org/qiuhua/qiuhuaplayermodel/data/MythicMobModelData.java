package org.qiuhua.qiuhuaplayermodel.data;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MythicMobModelData {

    //全部mm怪物的模型记录  key是怪物uuid  value是模型id
    private static final ConcurrentHashMap<UUID, String> allEntityModelData = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<UUID, String> getEntityModelData(){
        return allEntityModelData;
    }

}
