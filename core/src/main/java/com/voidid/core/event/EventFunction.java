package com.voidid.core.event;

import org.json.JSONException;
import org.json.JSONObject;

public interface EventFunction {
    /** 执行 **/
    void run(String eventName, JSONObject data) throws JSONException;
}
