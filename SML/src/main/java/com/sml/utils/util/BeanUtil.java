package com.sml.utils.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BeanUtil {

    /**
     *
     * @param bean
     * @param properties
     * @throws Exception
     */
    public static void populate(Object bean, Map<String, Object> properties) throws Exception {
        Class<?> clazz = bean.getClass();
        Class<?> superClazz = clazz.getSuperclass();
        Field[] superFields = superClazz.getDeclaredFields();
        for(Field superField:superFields) {
            System.out.println("super:"+superField.getName()+"/"+superField.getType());
            setProperties(properties, superField, bean);
        }
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields) {
            System.out.println(field.getName()+"/"+field.getType());
            setProperties(properties, field, bean);
        }
        //BeanUtils.populate(bean, properties);
    }

    /**
     *
     * @param properties
     * @param field
     * @param bean
     * @throws Exception
     */
    public static void setProperties(Map<String, Object> properties, Field field, Object bean) throws Exception {
        //구분자 제거위한 셋팅 시작
        Map<String, Object> rcsMap = new HashMap<String, Object>();
        String rcsValues = StringUtil.nullValue(properties.get("removeColumnSeparator"));
        if (!rcsValues.equals("")) {
            String[] cols = new String[]{};
            String amp = "&";
            String exc = "!";
            if (rcsValues.indexOf(amp) != -1) {
                cols = rcsValues.split(amp);
            } else {
                cols = new String[1];
                cols[0] = rcsValues;
            }
            for(String col:cols) {
                if (col.indexOf(exc) != -1) {
                    String[] kv = col.split(exc);
                    rcsMap.put(kv[0], kv[1]);
                }
            }
        }
        //구분자 제거위한 셋팅 종료

        field.setAccessible(true);
        //List<String> removeDates = new ArrayList<String>();
        Set<?> keySet =  properties.keySet();
        Iterator<?> keyIter = keySet.iterator();
        while(keyIter.hasNext()) {
            String tempKey = (String) keyIter.next();
            String tempValue = StringUtil.nullValue(properties.get(tempKey));

            //구분자 제거 시작
            Set<?> rcskeySet =  rcsMap.keySet();
            Iterator<?> rcskeyIter = rcskeySet.iterator();
            while(rcskeyIter.hasNext()) {
                String rcsKey = (String) rcskeyIter.next();
                String rcsValue = StringUtil.nullValue(rcsMap.get(rcsKey));
                if (rcsKey.equals(tempKey)) {
                    tempValue = tempValue.replaceAll(rcsValue, "");
                }
            }
            //구분자 제거 종료

            if (tempKey.equals(field.getName())) {
                String type = StringUtil.nullValue(field.getType());
                if (type.indexOf("String") != -1) {
                    //properties.put(tempKey, new String(tempValue));
                    field.set(bean, new String(tempValue));
                } else if (type.indexOf("BigDecimal") != -1) {
                    tempValue = tempValue.replace(",","");
                    //properties.put(tempKey, !tempValue.equals("")?new BigDecimal(tempValue):new BigDecimal(0));
                    field.set(bean, !tempValue.equals("")?new BigDecimal(tempValue):new BigDecimal(0));
                } else if (type.indexOf("Long") != -1) {
                    //properties.put(tempKey, !tempValue.equals("")?new Long(tempValue):new Long(0));
                    field.set(bean, !tempValue.equals("")?new Long(tempValue):new Long(0));
                } else if (type.indexOf("Integer") != -1) {
                    //properties.put(tempKey, !tempValue.equals("")?new Integer(tempValue):new Integer(0));
                    field.set(bean, !tempValue.equals("")?new Integer(tempValue):new Integer(0));
                } else if (type.indexOf("Double") != -1) {
                    //properties.put(tempKey, !tempValue.equals("")?new Double(tempValue):new Double(0));
                    field.set(bean, !tempValue.equals("")?new Double(tempValue):new Double(0));
                } else if (type.indexOf("Float") != -1) {
                    //properties.put(tempKey, !tempValue.equals("")?new Float(tempValue):new Float(0));
                    field.set(bean, !tempValue.equals("")?new Float(tempValue):new Float(0));
                } else if (type.indexOf("Byte") != -1) {
                    //properties.put(tempKey, new Byte(tempValue));
                    field.set(bean, new Byte(tempValue));
                } else if (type.indexOf("byte[]") != -1) {
                    //properties.put(tempKey, tempValue.getBytes());
                    field.set(bean, tempValue.getBytes());
                } else if (type.indexOf("Boolean[]") != -1) {
                    //properties.put(tempKey, new Boolean(tempValue));
                    field.set(bean, new Boolean(tempValue));
                } else if (type.indexOf("Boolean") != -1) {
                    //properties.put(tempKey, new Boolean(tempValue));
                    field.set(bean, new Boolean(tempValue));
                } else if (type.indexOf("Object") != -1) {
                    //properties.put(tempKey, new String(tempValue));
                    field.set(bean, new String(tempValue));
                } else if (type.indexOf("int") != -1) {
                    //properties.put(tempKey, !tempValue.equals("")?new Integer(tempValue):new Integer(0));
                    field.set(bean, !tempValue.equals("")?new Integer(tempValue):new Integer(0));
                } else if (type.indexOf("Short") != -1) {
                    //properties.put(tempKey, !tempValue.equals("")?new Integer(tempValue):new Integer(0));
                    field.set(bean, !tempValue.equals("")?new Short(tempValue):new Short("0"));
                } else if (type.indexOf("Date") != -1) {
                    if (tempValue != null &&!tempValue.equals("") && tempValue.length() > 1) {
                        //properties.put(tempKey, new Date(tempValue));
                        field.set(bean, new Date(tempValue));
                    } else {
                        //properties.put(tempKey, new Date());
                        //removeDates.add(tempKey);
                        field.set(bean, null);
                    }
                } else {
                    //properties.put(tempKey, new String(tempValue));
                    field.set(bean, new String(tempValue));
                }
            }
        }
        field.setAccessible(true);
        /*
        for(String removeDate:removeDates) {
        	field.setAccessible(true);
        	//Date d = null;
        	field.set(bean, null);
        	field.setAccessible(false);
        	properties.remove(removeDate);
        }*/
    }
}
