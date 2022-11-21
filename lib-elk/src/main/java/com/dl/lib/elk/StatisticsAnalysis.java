package com.dl.lib.elk;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 统计分析上报
 */
public class StatisticsAnalysis {
    private static final String TAG = StatisticsAnalysis.class.getSimpleName();
    private static final int COLLECT_COUNT_DEFAULT = 5;
    private static int COLLECT_COUNT = COLLECT_COUNT_DEFAULT;
    private static boolean SEND_IMMEDIATELY = true;
    private static final List<String> statisticsList = new ArrayList<>();

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static void init(boolean immediately) {
        SEND_IMMEDIATELY = immediately;
        COLLECT_COUNT = COLLECT_COUNT_DEFAULT;
    }

    public static String transferLongToDate() {
        @SuppressLint("SimpleDateFormat")
        Date date = new Date(Long.parseLong(System.currentTimeMillis() + ""));
        return sdf.format(date);
    }

    public static void doSendStatistics(String statisticsParam) {
        String localTime = transferLongToDate();
        statisticsParam += "`otm="+localTime+"`rtm="+localTime+"";
        doSendStatistics(SEND_IMMEDIATELY, statisticsParam);
    }

    private static void doSendStatistics(boolean immediately, String statisticsString) {
        if (TextUtils.isEmpty(statisticsString)) {
            return;
        }

        if (immediately) {
            StatisticsManager.getInstance().sendStatistics( statisticsString);
        } else {
            saveStatistics(statisticsString);
        }
    }

    private static synchronized void saveStatistics(String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }

        statisticsList.add(string);
        int configCollectCount = 0;
        if (StatisticsManager.getInstance().getStatisticsConfig() != null) {
            configCollectCount = StatisticsManager.getInstance().getStatisticsConfig().getLgsCollectCount();
        }
        if (configCollectCount == 0) {
            configCollectCount = COLLECT_COUNT;
        }
        if (statisticsList.size() >= configCollectCount) {
            String statisticsString = TextUtils.join("\n", statisticsList);
            StatisticsManager.getInstance().sendStatistics(statisticsString);
            statisticsList.clear();
        }
    }

    public static synchronized void sendSaveStatistics() {
        if (statisticsList.size() > 0) {
            String statisticsString = TextUtils.join("\n", statisticsList);
            StatisticsManager.getInstance().sendStatistics(statisticsString);
            statisticsList.clear();
        }
    }

}
