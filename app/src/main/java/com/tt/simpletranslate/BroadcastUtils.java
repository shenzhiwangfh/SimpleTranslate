package com.tt.simpletranslate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Map;

public class BroadcastUtils {

    private static BroadcastUtils utils;
    private static LocalBroadcastManager manager;
    private final Context context;

    public final static int ID_TRANSLATION_RESULT = 0;
    private final static String ACTION_TRANSLATION_RESULT = "TRANSLATION_RESULT";

    private static Map<Integer, String> map = new HashMap<>();

    static {
        map.put(ID_TRANSLATION_RESULT, ACTION_TRANSLATION_RESULT);
    }

    public interface OnSendListener {
        void send(int id, Intent intent);
    }

    private OnSendListener l;

    public void setOnSendListener(OnSendListener l) {
        this.l = l;
    }

    private BroadcastUtils(Context context) {
        this.context = context;
    }

    public static BroadcastUtils getInstance(Context context) {
        if (utils == null) utils = new BroadcastUtils(context.getApplicationContext());
        return utils;
    }

    public void register() {
        if (manager == null) {
            IntentFilter filter = new IntentFilter();
            for (String action : map.values()) {
                filter.addAction(action);
            }
            manager = LocalBroadcastManager.getInstance(context);
            manager.registerReceiver(receiver, filter);
        }
    }

    public void unregister() {
        if (manager != null) manager.unregisterReceiver(receiver);
    }

    public void sendMessage(int id, Intent intent) {
        intent.putExtra("id", id);
        intent.setAction(map.get(id));
        if (manager != null) manager.sendBroadcast(intent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("id", -1);
            if (l != null) l.send(id, intent);
        }
    };
}
