package com.slabgames.thinkingdata;

import android.app.Activity;
import android.content.Intent;
import android.util.ArraySet;
import android.util.Log;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Set;

import android.app.Application.ActivityLifecycleCallbacks;

import androidx.annotation.NonNull;


import org.godotengine.godot.Dictionary;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import cn.thinkingdata.analytics.TDAnalytics;
import cn.thinkingdata.analytics.TDConfig;
import cn.thinkingdata.analytics.ThinkingAnalyticsSDK;
import kotlin.reflect.KFunction;


public class GodotThinkingData extends GodotPlugin {

    private ThinkingAnalyticsSDK _instance;
    private String _appId;
    private String _serverUrl;
    private String _userId;

    private final String TAG = GodotThinkingData.class.getName();

    private int _callbackId;

    public GodotThinkingData(Godot godot) {
        super(godot);
    }

    @Override
    public String getPluginName() {
        return "GodotThinkingData";
    }

//   @NonNull
//   @Override
//   public List<String> getPluginMethods() {
//       return Arrays.asList(
//               "startConnection",
//                "endConnection",
//               "getConnectionState",
//               "init",
//               "queryPurchases",
//               "acknowledgePurchase",
//               "consumePurchase",
//               "purchase",
//               "querySkuDetails",
//               "isReady"
//       );
//   }


    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            signals = new ArraySet<>();
        }
//        signals.add(new SignalInfo("connected"));
//        signals.add(new SignalInfo("disconnected"));
//        signals.add(new SignalInfo("billing_resume"));
//        signals.add(new SignalInfo("connect_error", Integer.class, String.class));
//        signals.add(new SignalInfo("purchases_updated", Object[].class));
//        signals.add(new SignalInfo("query_purchases_response", Object.class));
//        signals.add(new SignalInfo("purchase_error", Integer.class, String.class));
//        signals.add(new SignalInfo("sku_details_query_completed", Object[].class));
//        signals.add(new SignalInfo("sku_details_query_error", Integer.class, String.class, String[].class));
//        signals.add(new SignalInfo("price_change_acknowledged", Integer.class));
//        signals.add(new SignalInfo("purchase_acknowledged", String.class));
//        signals.add(new SignalInfo("purchase_acknowledgement_error", Integer.class, String.class, String.class));
//        signals.add(new SignalInfo("purchase_consumed", String.class));
//        signals.add(new SignalInfo("purchase_consumption_error", Integer.class, String.class, String.class));

        return signals;
    }

    /*
    @Override
    public Set<SignalInfo> getPluginSignals() {
        return Collections.singleton(loggedInSignal);
    }
    */


    @Override
    public View onMainCreate(Activity activity) {


        return null;
    }

    @UsedByGodot
    public void init(final String appId, final String serverUrl){
        final Activity act = getActivity();
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {



                _instance = ThinkingAnalyticsSDK.sharedInstance(getActivity(),appId,serverUrl);
                _appId = appId;
                _serverUrl = serverUrl;



                Log.d(TAG,"ThinkingAnalyticsSDK plugin inited onCreate");
            }
        });
    }

    @UsedByGodot
    public void login(String userId){
        _instance.login(userId);
        _userId = userId;

        if (_instance != null)
        {
            try {
                JSONObject superProperties = new JSONObject();
                superProperties.put("channel","ta");//string
                superProperties.put("age",1);//number
                superProperties.put("isSuccess",true);//boolean
                superProperties.put("birthday",new Date());//time

                JSONObject object = new JSONObject();
                object.put("key", "value");
                superProperties.put("object",object);//object

                JSONObject object1 = new JSONObject();
                object1.put("key", "value");
                JSONArray arr    = new JSONArray();
                arr.put(object1);
                superProperties.put("object_arr",arr);//array object

    //                    JSONArray  arr1    = new JSONArray();
    //                    arr1.put("value");
    //                    superProperties.put(arr1);//array
                //set super properties
                _instance.setSuperProperties(superProperties);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<ThinkingAnalyticsSDK.AutoTrackEventType> eventTypeList = new ArrayList<>();
            //APP install event
            eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_INSTALL);
            //APP start event
            eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_START);
            //APP end event
            eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_END);
            //enable autotrack event
            ThinkingAnalyticsSDK.sharedInstance(getActivity(), _appId).enableAutoTrack(eventTypeList);
        }
    }

    @UsedByGodot
    public  void sendEvent(String key, String value, String eventName)
    {
        try {
            JSONObject properties = new JSONObject();
            properties.put(key,value);
            _instance.track(eventName,properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onMainDestroy() {

        super.onMainDestroy();
    }


    // Internal methods

    @Override
    public void onMainActivityResult (int requestCode, int resultCode, Intent data)
    {
    }
}
