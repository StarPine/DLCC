package com.dl.playfun.app.config;

import com.dl.playfun.app.Injection;

/**
 * Author: 彭石林
 * Time: 2022/1/11 14:47
 * Description: This is TbarCenterImgConfig
 */
public class TbarCenterImgConfig {
    private static volatile TbarCenterImgConfig tbarCenterImgConfig = null;

    //设置图片
    private int imgSrcPath = -1;
    public static TbarCenterImgConfig getInstance(){
        if(tbarCenterImgConfig==null){
            synchronized (TbarCenterImgConfig.class){
                tbarCenterImgConfig = new TbarCenterImgConfig();
            }
        }
        return tbarCenterImgConfig;
    }

    public int getImgSrcPath() {
        if(imgSrcPath==-1){
            int gamePlayLogoImg = Injection.provideDemoRepository().readSwitches("GamePlayLogoImg");
            if(gamePlayLogoImg==0){
                return -1;
            }
            return gamePlayLogoImg;
        }
        return imgSrcPath;
    }

    public void setImgSrcPath(int imgSrcPath) {
        this.imgSrcPath = imgSrcPath;
        Injection.provideDemoRepository().putSwitches("GamePlayLogoImg",imgSrcPath);
    }
}
