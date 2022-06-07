package com.sml.utils.util;

import java.util.LinkedHashMap;

public class DtoMap<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 3801124242820219131L;

    /**
     * @Method Name : getString
     * @Method Desc : String 반환 (get 된 데이터가 null 이면 키문자를 대문자로 한번 더 추출)
     * @User        : dswi
     * @Date @Time  : 2012. 2. 10. 오전 10:13:51
     * @History     :
     * @param paramObject
     * @return
     */
    public String getString(Object paramObject) {
        if (super.get(paramObject) == null) {
            return StringUtil.nullValue(super.get(paramObject.toString().toUpperCase()));
        }
        return StringUtil.nullValue(super.get(paramObject));
    }

    /* (non-Javadoc)
     * @see java.util.LinkedHashMap#get(java.lang.Object)
     * Object 반환 (get 된 데이터가 null 이면 키문자를 대문자로 한번 더 추출)
     */
    public V get(Object paramObject) {
        if (paramObject instanceof String && super.get(paramObject) == null) {
            return super.get(paramObject.toString().toUpperCase());
        } else {
            return super.get(paramObject);
        }
    }

    /* (non-Javadoc)
     * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
     * ibatis에서 select 결과 컬럼명을 대문자로 넘겨주는데 소문자도 같이 등록이 가능하도록 셋팅
     */
	/*
	public V put(K paramK, V paramV) {
		if (paramK instanceof String) {
			super.put((K) paramK.toString().toLowerCase(), paramV);
		}
		return super.put(paramK, paramV);
	}*/
}
