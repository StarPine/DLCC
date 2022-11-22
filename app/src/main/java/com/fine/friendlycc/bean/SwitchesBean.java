package com.fine.friendlycc.bean;

import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/12/25 16:02
 * Description: This is SwitchesBean
 */
public class SwitchesBean {

    private List<SwitchInfoSet> switchInfos;

    public List<SwitchInfoSet> getSwitchInfos() {
        return switchInfos;
    }

    public void setSwitchInfos(List<SwitchInfoSet> switchInfos) {
        this.switchInfos = switchInfos;
    }

    @Override
    public String toString() {
        return "SwitchesBean{" +
                "switchInfos=" + switchInfos +
                '}';
    }

    public class SwitchInfoSet {
        private String key;
        private int value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "SwitchInfoSet{" +
                    "key='" + key + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
}