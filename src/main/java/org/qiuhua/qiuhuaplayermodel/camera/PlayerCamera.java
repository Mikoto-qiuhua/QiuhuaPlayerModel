package org.qiuhua.qiuhuaplayermodel.camera;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.been.player.camera.CameraPositionBeen;
import com.daxton.unrealcore.been.player.camera.CameraRotationBeen;

import com.daxton.unrealcore.been.player.type.CameraTargetType;
import com.daxton.unrealcore.been.player.type.PlayerCameraType;
import org.bukkit.entity.Player;
import org.qiuhua.qiuhuaplayermodel.Tool.Config;

import java.util.UUID;

public class PlayerCamera {

    public static void sendPlayer(Player player) {
        boolean isEnable = Config.getConfig().getBoolean("Camera.Enable");
        if (!isEnable){
            return;
        }
        UUID uuid = player.getUniqueId();
        String x = Config.getConfig().getString("Camera.X");
        String y = Config.getConfig().getString("Camera.Y");
        String z = Config.getConfig().getString("Camera.Z");

        //设置位置
        CameraPositionBeen cameraPositionBeen = new CameraPositionBeen(PlayerCameraType.THIRD_PERSON_BACK, CameraTargetType.SELF, uuid.toString(), x, y, z);
        UnrealCoreAPI.inst(player).getPlayerHelper().cameraPositionSet(cameraPositionBeen);

    }


}
