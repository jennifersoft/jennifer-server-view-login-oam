package com.aries.oam.adapter.util;

import java.util.HashMap;

/**
 * Key의 대/소문자 구분 없이 사용 가능한 Map
 * ex) map.put( "AAA", "123" ) 으로 등록하였을경우
 *     map.get( "AAA" ) 또는 map.get( "aaa" ) 모두 사용가능
 * apache common collection 의 CaseInsensitiveMap 사용 권장
 */
public class OAMInsensitiveMap extends HashMap<String, String> {
    public OAMInsensitiveMap() {
        super();
    }

    public boolean containsKey(String key) {
        if (key == null) {
            return false;
        }

        key = key.toUpperCase();
        return super.containsKey(key);
    }

    public String get(String key) {
        if (key == null) {
            return null;
        }

        key = key.toUpperCase();
        return super.get(key);
    }

    public String put(String key, String val) {
        if (key == null) {
            return null;
        }

        key = key.toUpperCase();
        return super.put(key, val);
    }

    public String remove(String key) {
        if (key == null) {
            return null;
        }

        key = key.toUpperCase();
        return super.remove(key);
    }
}
