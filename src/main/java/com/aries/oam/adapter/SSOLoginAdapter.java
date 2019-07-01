package com.aries.oam.adapter;

import com.aries.extension.data.UserData;
import com.aries.extension.handler.SSOLoginHandler;
import com.aries.extension.util.LogUtil;
import com.aries.extension.util.PropertyUtil;
import com.aries.oam.adapter.util.OAMInsensitiveMap;
import com.aries.oam.adapter.util.OAMUtil;

import javax.servlet.http.HttpServletRequest;

public class SSOLoginAdapter implements SSOLoginHandler {
    public UserData preHandle(HttpServletRequest request) {
        OAMInsensitiveMap headerMap = new OAMInsensitiveMap();
        OAMUtil.httpHeaderToMap(request, headerMap);

        String sso_user_id = headerMap.get(PropertyUtil.getValue("oam", "OAM_HEADER_KEY", "OAM_USER_ID"));
        String sso_user_password = PropertyUtil.getValue("oam", "OAM_SINGLE_PASSWORD", "");

        LogUtil.debug("preHandle check: " + sso_user_id + " (" + sso_user_password + ")");

        if(sso_user_id == null || sso_user_id.trim().length() <= 0) {
            LogUtil.error("sso_user_id not found") ;
            return null;
        }

        return new UserData(sso_user_id, sso_user_password);
    }
}
