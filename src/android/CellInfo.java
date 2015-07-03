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
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class CellInfo extends CordovaPlugin {
    public static final String TAG = "CellInfo";

    private class PrimaryCellPhoneStateListener extends PhoneStateListener {
        private CallbackContext callbackContext;
        private CordovaInterface cordova;

        public PrimaryCellPhoneStateListener(CordovaInterface cordova, CallbackContext callbackContext) {
            super();
            this.cordova = cordova;
            this.callbackContext = callbackContext;
        }

        private void returnCellInfo(int gsmSignalStrength) throws JSONException {
            JSONObject response = new JSONObject();
            TelephonyManager telephonyManager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            CellLocation location = telephonyManager.getCellLocation();
            GsmCellLocation gsmLocation = (GsmCellLocation) location;

            // http://stackoverflow.com/questions/9808396/android-cellid-not-available-on-all-carriers
            int cid = gsmLocation.getCid();
            int networkType = telephonyManager.getNetworkType();
            String generalNetworkType = CellInfo.networkTypeGeneral(networkType);
            if (generalNetworkType.equals("3G") && cid != -1) {
                response.put("cid", cid & 0xffff);
            } else {
                response.put("cid", cid);
            }

            response.put("lac", gsmLocation.getLac());
            response.put("psc", gsmLocation.getPsc());
            response.put("networkType", CellInfo.networkTypeToString(networkType));
            response.put("generalNetworkType", generalNetworkType);
            response.put("rssi", CellInfo.asuToDbm(networkType, gsmSignalStrength));
            callbackContext.success(response);
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            try {
                this.returnCellInfo(signalStrength.getGsmSignalStrength());
            } catch (JSONException e) {
                callbackContext.error("JSONException: " + e.getMessage());
            }
            this.unregister();
        }

        public void register() {
            TelephonyManager telephonyManager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(this, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }

        public void unregister() {
            TelephonyManager telephonyManager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
        }

    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArray of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        if (action.equals("getNeighboringCellInfo")) {
            this.getNeighboringCellInfo(callbackContext);
        } else if (action.equals("getPrimaryCellInfo")) {
            this.getPrimaryCellInfo(callbackContext);
        } else {
            return false;
        }
        return true;
    }


    private void getNeighboringCellInfo(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                JSONArray response = new JSONArray();
                TelephonyManager telephonyManager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                List<NeighboringCellInfo> cellInfoList = telephonyManager.getNeighboringCellInfo();

                try {
                    for(final NeighboringCellInfo info : cellInfoList) {
                        JSONObject jsonInfo = new JSONObject();

                        // http://stackoverflow.com/questions/9808396/android-cellid-not-available-on-all-carriers
                        int cid = info.getCid();
                        int networkType = info.getNetworkType();
                        String generalNetworkType = CellInfo.networkTypeGeneral(networkType);
                        if (generalNetworkType.equals("3G") && cid != -1) {
                            jsonInfo.put("cid", cid & 0xffff);
                        } else {
                            jsonInfo.put("cid", cid);
                        }

                        jsonInfo.put("lac", info.getLac());
                        jsonInfo.put("psc", info.getPsc());
                        jsonInfo.put("networkType", CellInfo.networkTypeToString(networkType));
                        jsonInfo.put("generalNetworkType", generalNetworkType);
                        jsonInfo.put("rssi", CellInfo.asuToDbm(networkType, info.getRssi()));
                        response.put(jsonInfo);
                    }
                    callbackContext.success(response);
                } catch (JSONException e) {
                    callbackContext.error("JSONException: " + e.getMessage());
                }
            }
        });
    }

    private void getPrimaryCellInfo(final CallbackContext callbackContext) {
        PrimaryCellPhoneStateListener listener = new PrimaryCellPhoneStateListener(cordova, callbackContext);
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                listener.register();
            }
        });
    }

    /**
     * Converts NETWORK_TYPE_* constants to network type names.
     *
     * @param networkType       Network type constant (NETWORK_TYPE_*).
     * @return                  Name of network type.
     */
    private static String networkTypeToString(int networkType) {
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

    /**
     * Converts NETWORK_TYPE_* to general network type ('2G', '3G, '4G', 'CDMA').
     *
     * @param networkType       Network type constant (NETWORK_TYPE_*).
     * @return                  '2G', '3G, '4G', 'CDMA' or 'Unknown'.
     */
    private static String networkTypeGeneral(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return "Unknown";
        }
    }

    /**
     * Converts ASU signal strength level to dBM depending on network type.
     *
     * @param networkType       Network type constant (NETWORK_TYPE_*).
     * @param asu               ASU signal strength level.
     * @return                  Signal strength in dBm.
     */
    private static int asuToDbm(int networkType, int asu) {
        String generalNetworkType = CellInfo.networkTypeGeneral(networkType);
        if (generalNetworkType.equals("2G")) {
            return 2 * asu - 113;
        } else if (generalNetworkType.equals("3G")) {
            return asu - 116;
        } else {
            return asu;
        }
    }
}
