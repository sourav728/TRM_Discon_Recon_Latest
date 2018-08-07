package com.example.tvd.trm_discon_recon.other;

import android.os.Bundle;
import android.util.Log;

import com.example.tvd.trm_discon_recon.values.GetSetValues;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sourav
 */
public class DataParser {
    //Create a static Bundle object in DataParser class.
    public static Bundle mMyAppsBundle = new Bundle();
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        JSONObject jDistance = null;
        JSONObject jDuration = null;
        JSONObject JAddress = null;

        String startAddress="",endAddress="",travelMode="";
        GetSetValues getsetvalues;
        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<>();
                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    if (j == 0) {
                        Log.d("debug", "Distance: "+((JSONObject)jLegs.get(j)).getJSONObject("distance").getString("text")+"\n"+
                        "Duration: "+((JSONObject)jLegs.get(j)).getJSONObject("duration").getString("text"));
                    }

                    Log.d("Debugg","StartAddresss..."+((JSONObject)jLegs.get(j)).getString("start_address"));
                    Log.d("Debugg","EndAddressssss..."+((JSONObject)jLegs.get(j)).getString("end_address"));

                    startAddress = ((JSONObject)jLegs.get(j)).getString("start_address");
                    //Set key values pair in that bundle from anywhere. like this:
                    DataParser.mMyAppsBundle.putString("startAddress",startAddress);

                    endAddress = ((JSONObject)jLegs.get(j)).getString("end_address");
                    DataParser.mMyAppsBundle.putString("endAddress",endAddress);

                    /** Getting distance from the json data */
                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                    HashMap<String, String> hmDistance = new HashMap<String, String>();
                    hmDistance.put("distance", jDistance.getString("text"));

                    /** Getting duration from the json data */
                    jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    HashMap<String, String> hmDuration = new HashMap<String, String>();
                    hmDuration.put("duration", jDuration.getString("text"));

                    /**Getting Full address**/
                    HashMap<String, String> hmAddress = new HashMap<String, String>();
                    hmAddress.put("address", startAddress);

                   /* GetSetValues getSetValues = new GetSetValues();
                    getSetValues.setStartaddress(startAddress);*/

                    /** Adding distance object to the path **/
                    path.add(hmDistance);

                    /** Adding duration object to the path */
                    path.add(hmDuration);

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");
                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("lat", Double.toString((list.get(l)).latitude) );
                            hm.put("lng", Double.toString((list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    travelMode = ((JSONObject)jSteps.get(j)).getString("travel_mode");
                    DataParser.mMyAppsBundle.putString("travel_mode",travelMode);

                    Log.d("Debugg","Mode"+travelMode);

                    routes.add(path);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }



        return routes;
    }


    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
