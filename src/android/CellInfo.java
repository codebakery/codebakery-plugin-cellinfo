package io.codebakery.cellinfo;

import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class CellInfo extends CordovaPlugin {
    public static final String TAG = "CellInfo";
    private TelephonyManager telephonyManager;

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.telephonyManager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArray of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getNeighboringCellInfo")) {
            callbackContext.success(this.getNeighboringCellInfo());
        } else if (action.equals("getPrimaryCellInfo")) {
            callbackContext.success(this.getPrimaryCellInfo());
        } else if (action.equals("getNetworkType")) {
            callbackContext.success(this.getNetworkType());
        }
        else {
            return false;
        }
        return true;
    }


    private JSONArray getNeighboringCellInfo() throws JSONException {
        JSONArray response = new JSONArray();
        List<NeighboringCellInfo> cellInfoList = this.telephonyManager.getNeighboringCellInfo();

        for(final NeighboringCellInfo info : cellInfoList) {
            JSONObject jsonInfo = new JSONObject();

            // http://stackoverflow.com/questions/9808396/android-cellid-not-available-on-all-carriers
            int cid = info.getCid();
            if (this.telephonyManager.getNetworkType() == this.telephonyManager.NETWORK_TYPE_UMTS && cid != -1) {
                jsonInfo.put("cid", cid & 0xffff);
            } else {
                jsonInfo.put("cid", cid);
            }

            jsonInfo.put("lac", info.getLac());
            jsonInfo.put("psc", info.getPsc());
            jsonInfo.put("networkType", info.getNetworkType());
            jsonInfo.put("rssi", info.getRssi());
            response.put(jsonInfo);
        }
        return response;
    }

    private JSONObject getPrimaryCellInfo() throws JSONException {
        JSONObject response = new JSONObject();
        CellLocation location = this.telephonyManager.getCellLocation();
        GsmCellLocation gsmLocation = (GsmCellLocation) location;

        // http://stackoverflow.com/questions/9808396/android-cellid-not-available-on-all-carriers
        int cid = gsmLocation.getCid();
        if (this.telephonyManager.getNetworkType() == this.telephonyManager.NETWORK_TYPE_UMTS && cid != -1) {
            response.put("cid", cid & 0xffff);
        } else {
            response.put("cid", cid);
        }

        response.put("lac", gsmLocation.getLac());
        return response;
    }

    private String getNetworkType() {
        int networkType = this.telephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT: return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA: return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE: return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EHRPD: return "eHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0: return "EVDO rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A: return "EVDO rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B: return "EVDO rev. B";
            case TelephonyManager.NETWORK_TYPE_GPRS: return "GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA: return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA: return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSPAP: return "HSPA+";
            case TelephonyManager.NETWORK_TYPE_HSUPA: return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_IDEN: return "iDen";
            case TelephonyManager.NETWORK_TYPE_LTE: return "LTE";
            case TelephonyManager.NETWORK_TYPE_UMTS: return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN: return "Unknown";
            default: return "New type of network";
        }
    }

}
