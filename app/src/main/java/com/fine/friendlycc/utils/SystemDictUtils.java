package com.fine.friendlycc.utils;

import com.blankj.utilcode.util.StringUtils;
import com.fine.friendlycc.R;
import com.fine.friendlycc.app.Injection;
import com.fine.friendlycc.bean.ConfigItemBean;
import com.fine.friendlycc.bean.OccupationConfigItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wulei
 */
public class SystemDictUtils {

    public static String getCityByIds(List<Integer> ids) {
        if (ids == null) {
            return "";
        }
        List<ConfigItemBean> data = Injection.provideDemoRepository().readCityConfig();
        StringBuffer sb = new StringBuffer();
        if (data != null && !data.isEmpty()) {
            for (ConfigItemBean config : data) {
                for (int i = 0; i < ids.size(); i++) {
                    if (ids.get(i).intValue() == config.getId()) {
                        if (i == ids.size() - 1) {
                            sb.append(config.getName());
                        } else {
                            sb.append(config.getName()).append("/");
                        }
                    }

                }
            }
        }
        return sb.toString();
    }

    //根据id数组查询所有城市得到数据
    public static String getCityByIdsAll(List<Integer> ids) {
        if (ids == null) {
            return "";
        }
        List<ConfigItemBean> data = Injection.provideDemoRepository().readCityConfigAll();
        StringBuffer sb = new StringBuffer();
        if (data != null && !data.isEmpty()) {
            for (ConfigItemBean config : data) {
                for (int i = 0; i < ids.size(); i++) {
                    if (ids.get(i).intValue() == config.getId()) {
                        if (i == ids.size() - 1) {
                            sb.append(config.getName());
                        } else {
                            sb.append(config.getName()).append("/");
                        }
                    }

                }
            }
        }
        return sb.toString();
    }

    public static String getCityById(Integer id) {
        List<ConfigItemBean> data = Injection.provideDemoRepository().readCityConfig();
        StringBuffer sb = new StringBuffer();
        if (id == null) {
            sb.append(StringUtils.getString(R.string.playcc_unknown));
        } else {
            if (data != null && !data.isEmpty()) {
                for (ConfigItemBean config : data) {
                    if (id.intValue() == config.getId()) {
                        sb.append(config.getName());
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String getHopeObjectByIds(Integer id) {
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        return getHopeObjectByIds(ids);
    }

    public static String getHopeObjectByIds(List<Integer> ids) {
        if (ids == null) {
            return "";
        }
        List<ConfigItemBean> data = Injection.provideDemoRepository().readHopeObjectConfig();
        StringBuffer sb = new StringBuffer();
        if (data != null && !data.isEmpty()) {
            for (ConfigItemBean config : data) {
                for (int i = 0; i < ids.size(); i++) {
                    if (ids.get(i) == null || config.getId() == null) {
                        continue;
                    }
                    if (ids.get(i).intValue() == config.getId().intValue()) {
                        sb.append(config.getName()).append("/");
                    }

                }
            }
        }
        String str = sb.toString();
        if (str.indexOf("/") != -1) {
            return str.substring(0, str.length() - 1);
        }
        return sb.toString();
    }


    public static String getWeightById(Integer id) {
        if (id == null) {
            return "";
        }
        List<ConfigItemBean> data = Injection.provideDemoRepository().readWeightConfig();
        StringBuffer sb = new StringBuffer();
        for (ConfigItemBean c : data) {
            if (c.getId().intValue() == id.intValue()) {
                sb.append(c.getName());
            }
        }
        return sb.toString();
    }

    public static String getHeightById(Integer id) {
        if (id == null) {
            return "";
        }
        List<ConfigItemBean> data = Injection.provideDemoRepository().readHeightConfig();
        StringBuffer sb = new StringBuffer();
        for (ConfigItemBean c : data) {
            if (c.getId().intValue() == id.intValue()) {
                sb.append(c.getName());
            }
        }
        return sb.toString();
    }

    /**
     * 根据工作ID查询工作名称
     *
     * @param id 工作ID
     * @return
     */
    public static String getOccupationById(int id) {
        List<OccupationConfigItemBean> list = Injection.provideDemoRepository().readOccupationConfig();
        String name = StringUtils.getString(R.string.playcc_unknown);
        for (OccupationConfigItemBean occupationConfigItemEntity : list) {
            boolean find = false;
            for (OccupationConfigItemBean.ItemEntity itemEntity : occupationConfigItemEntity.getItem()) {
                if (itemEntity.getId() == id) {
                    find = true;
                    name = itemEntity.getName();
                    break;
                }
            }
            if (find) {
                break;
            }
        }
        return name;
    }

    /**
     * 根据工作ID查询工作名称
     *
     * @param id 工作ID
     * @return
     */
    public static String getOccupationByIdOnNull(int id) {
        List<OccupationConfigItemBean> list = Injection.provideDemoRepository().readOccupationConfig();
        String name = null;
        for (OccupationConfigItemBean occupationConfigItemEntity : list) {
            boolean find = false;
            for (OccupationConfigItemBean.ItemEntity itemEntity : occupationConfigItemEntity.getItem()) {
                if (itemEntity.getId() == id) {
                    find = true;
                    name = itemEntity.getName();
                    break;
                }
            }
            if (find) {
                break;
            }
        }
        return name;
    }

    /**
     * 根据身高配置ID查询身高
     *
     * @param id 身高ID
     * @return
     */
    public static String getHeightById(int id) {
        List<ConfigItemBean> list = Injection.provideDemoRepository().readHeightConfig();
        String name = StringUtils.getString(R.string.playcc_unknown);
        for (ConfigItemBean configItemEntity : list) {
            if (configItemEntity.getId().intValue() == id) {
                name = configItemEntity.getName();
                break;
            }
        }
        return name;
    }

    /**
     * 根据体重配置ID查询体重
     *
     * @param id 体重ID
     * @return
     */
    public static String getWidthById(int id) {
        List<ConfigItemBean> list = Injection.provideDemoRepository().readWeightConfig();
        String name = StringUtils.getString(R.string.playcc_unknown);
        for (ConfigItemBean configItemEntity : list) {
            if (configItemEntity.getId().intValue() == id) {
                name = configItemEntity.getName();
                break;
            }
        }
        return name;
    }
}