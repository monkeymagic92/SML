package com.sml.util;

import com.nhncorp.lucy.security.xss.XssPreventer;
import com.sml.core.StringUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonUtil {

    private static Log logger = LogFactory.getLog(CommonUtil.class);

    /**
     * List 객체 값 로그 보기
     * @param list
     */
    public static void listInfo(List list) {
        if(list == null) {
            return;
        }
        int len = list.size();
        if(list != null){
            logger.info("########### Map Info Start ###########");
            for(int i=0; i<len; i++){
                DtoMap dtoMap = (DtoMap)list.get(i);
                CommonUtil.mapInfo(dtoMap);
            }
            logger.info("########### Map Info End   ###########");
        }
    }

    /**
     * Map 객체 값 로그 보기
     * @param map
     */
    public static void mapInfo(Map map) {
        if(map == null) {
            return;
        }

        String tempKey = null;
        String tempValue = null;
        String result = "";

        Set<?> keySet =  map.keySet();
        Iterator<?> keyIter = keySet.iterator();

        while(keyIter.hasNext()) {
            tempKey = (String) keyIter.next();
            tempValue = StringUtil.nullValue(map.get(tempKey));
            tempValue = parseStringFielterDQ(tempValue);
            result += " "+tempKey+":"+tempValue+",";
        }
        logger.info("Map => "+result);

    }

    /**
     * Map 객체 값 String 반환
     * @param map
     */
    public static String mapInfoToString(Map map) {
        if(map == null) {
            return "";
        }

        String tempKey = null;
        String tempValue = null;
        String result = "";

        Set<?> keySet =  map.keySet();
        Iterator<?> keyIter = keySet.iterator();

        while(keyIter.hasNext()) {
            tempKey = (String) keyIter.next();
            tempValue = StringUtil.nullValue(map.get(tempKey));
            tempValue = parseStringFielterDQ(tempValue);
            result += " "+tempKey+":"+tempValue+",";
        }
        return result;
    }

    public static void jsonResponse(HttpServletResponse response, String result, String message) throws Exception {
        JSONObject jobj = new JSONObject();
        jobj.put("result", result);
        jobj.put("message", message);
        String strAll = jobj.toString();
        strAll = parseStringFielter(strAll);

        //out.write(obj.toJSONString());
        //response.setContentType("application/json");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(strAll);
    }

    public static void jsonResponse(HttpServletResponse response, String result, String message, Map map) throws Exception {
        String strAll = "";
        String strStart = "{";
        String strEnd = "}";
        String sep = ":";
        String comma = ",";

        if(map == null) {
            return;
        }

        Set<?> keySet =  map.keySet();
        Iterator<?> keyIter = keySet.iterator();

        int i = 0;
        strAll += strStart;
        strAll += "\"result\"";
        strAll += sep;
        strAll += "\""+result+"\"";
        strAll += comma;
        strAll += "\"message\"";
        strAll += sep;
        strAll += "\""+message+"\"";
        while(keyIter.hasNext()) {
            if (i == 0) {
                strAll += comma;
            }
        	/*String tempKey = (String) keyIter.next();
        	String tempValue = StringUtil.nullValue(map.get(tempKey));
        	tempValue = parseStringFielterDQ(tempValue);
            String key = "\""+tempKey+"\"";
    		String value = "\""+tempValue+"\"";
    		strAll += key+sep+value;
    		if ((++i) < map.size()) {
    			strAll += comma;
    		}*/
            String tempKey = (String) keyIter.next();
            if (map.get(tempKey) instanceof Map) {
                strAll += "\""+tempKey+"\" : ";
                strAll += parseMapToJson((Map) map.get(tempKey));
            } else if (map.get(tempKey) instanceof List) {
                strAll += "\""+tempKey+"\" : ";
                strAll += parseListToJson((List) map.get(tempKey));
            } else {
                String tempValue = StringUtil.nullValue(map.get(tempKey));
                tempValue = parseStringFielterDQ(tempValue);
                String key = "\""+tempKey+"\"";
                String value = "\""+tempValue+"\"";
                strAll += key+sep+value;
            }
            if ((++i) < map.size()) {
                strAll += comma;
            }
        }
        strAll += strEnd;

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(strAll);
    }

    public static void jsonResponse(HttpServletResponse response, Map map) throws Exception {
        String strAll = "";
        String strStart = "{";
        String strEnd = "}";
        String sep = ":";
        String comma = ",";

        if(map == null) {
            return;
        }
        Set<?> keySet =  map.keySet();
        Iterator<?> keyIter = keySet.iterator();

        int i = 0;
        strAll += strStart;
        while(keyIter.hasNext()) {
            String tempKey = (String) keyIter.next();
            //System.out.println("tempKey:"+tempKey + ", map.size:" + map.size() + ", i:" + i);

            if (map.get(tempKey) instanceof Map) {
                strAll += "\""+tempKey+"\" : ";
                strAll += parseMapToJson((Map) map.get(tempKey));
            } else if (map.get(tempKey) instanceof List) {
                strAll += "\""+tempKey+"\" : ";
                strAll += parseListToJson((List) map.get(tempKey));
            } else {
                String tempValue = StringUtil.nullValue(map.get(tempKey));
                tempValue = parseStringFielterDQ(tempValue);
                String key = "\""+tempKey+"\"";
                String value = "\""+tempValue+"\"";
                strAll += key+sep+value;
            }
            if ((++i) < map.size()) {
                strAll += comma;
            }
        }
        strAll += strEnd;

        //System.out.println("jsonResponse:"+strAll);
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(strAll);
    }

    private static String parseStringFielter(String tempValue) {
        tempValue = tempValue.indexOf("\r")!=-1?tempValue.replaceAll("\r", "\\\\r"):tempValue;
        tempValue = tempValue.indexOf("\n")!=-1?tempValue.replaceAll("\n", "\\\\n"):tempValue;

        //에러를 발생 시키는 not-valid JSON characters를 치환한다.
        //�, , , , , , , , , , , ,  등
        tempValue = tempValue.replaceAll("[\u0000-\u001C]", "");

        // lucy-xss-filter 를 통해 치환된 파라미터를 JSONObject 로 변환 시 오류가 발생함으로 부득이 치환 전 상태로 변경함
        tempValue = XssPreventer.unescape(tempValue);

        return tempValue;
    }

    private static String parseStringFielterDQ(String tempValue) {
        //System.out.println("& tempValue : "+tempValue);

		/*
		tempValue = tempValue.indexOf("\r")!=-1?tempValue.replaceAll("\r", "\\\\r"):tempValue;
		tempValue = tempValue.indexOf("\n")!=-1?tempValue.replaceAll("\n", "\\\\n"):tempValue;
		tempValue = tempValue.indexOf("\"")!=-1?tempValue.replaceAll("\"", "\\\\\""):tempValue;
		*/
        //json객체에서 유효하지 않는 개행문자를 치환한다.
        tempValue = tempValue.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r\n", "\\n")
                .replace("\t", "\\t")
                .replace("\n", "\\n");

        //에러를 발생 시키는 not-valid JSON characters를 치환한다.
        //�, , , , , , , , , , , ,  등
        tempValue = tempValue.replaceAll("[\u0000-\u001C]", "");
        //System.out.println("& tempValue : "+tempValue);

        // lucy-xss-filter 를 통해 치환된 파라미터를 JSONObject 로 변환 시 오류가 발생함으로 부득이 치환 전 상태로 변경함
        tempValue = XssPreventer.unescape(tempValue);

        return tempValue;
    }

    private static String parseMapToJson(Map map) {
        String strAll = "";
        String strStart = "{";
        String strEnd = "}";
        String sep = ":";
        String comma = ",";
        //System.out.println("map.size():"+map.size());
        if(map == null) {
            return "\"\"";
        }

        Set<?> keySet =  map.keySet();
        Iterator<?> keyIter = keySet.iterator();

        int i = 0;
        strAll += strStart;
        while(keyIter.hasNext()) {
            String tempKey = (String) keyIter.next();
            String tempValue = StringUtil.nullValue(map.get(tempKey));
            tempValue = parseStringFielterDQ(tempValue);
            String key = "\""+tempKey+"\"";
            String value = "\""+tempValue+"\"";

            strAll += key+sep+value;
            if ((++i) < map.size()) {
                strAll += comma;
            }
        }
        strAll += strEnd;

        return strAll;
    }

    private static String parseListToJson(List list) {
        String strAll = "";
        String strStart = "[";
        String strEnd = "]";
        System.out.println("list.size():"+list.size());
        if(list == null) {
            return "\"\"";
        }

        strAll += strStart;
        for(int i=0; i<list.size(); i++) {
            strAll += parseMapToJson((Map) list.get(i));
            if (i < (list.size()-1)) {
                strAll += ",";
            }
        }
        strAll += strEnd;

        return strAll;
    }

    public static void jsonResponse(HttpServletResponse response, List list) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"list\" : [");
        response.getWriter().write(jsonStr.toString());

        for(int i=0; i<list.size(); i++) {
            jobj.putAll((Map)list.get(i));
            String tempValue = jobj.toString();
            tempValue = parseStringFielter(tempValue);
            response.getWriter().write(tempValue);
            if (i < (list.size()-1)) {
                response.getWriter().write(",");
            }
            jobj.clear();
        }
        response.getWriter().write("]}");
    }

    public static void jsonResponse(HttpServletResponse response, List list, String str) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"str\" : \""+str+"\",");
        jsonStr.append("\"list\" : [");
        response.getWriter().write(jsonStr.toString());

        for(int i=0; i<list.size(); i++) {
            jobj.putAll((Map)list.get(i));
            String tempValue = jobj.toString();
            tempValue = parseStringFielter(tempValue);
            response.getWriter().write(tempValue);
            if (i < (list.size()-1)) {
                response.getWriter().write(",");
            }
            jobj.clear();
        }
        response.getWriter().write("]}");
    }

    public static void jsonResponse(HttpServletResponse response, Map info, List list) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        String page = StringUtil.nullValue(info.get("page"));
        String total = StringUtil.nullValue(info.get("total"));
        String records = StringUtil.nullValue(info.get("records"));

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"page\" : \""+page+"\",");
        jsonStr.append("\"total\" : "+total+",");
        jsonStr.append("\"records\" : \""+records+"\",");
        jsonStr.append("\"user\" : [");
        response.getWriter().write(jsonStr.toString());

        for(int i=0; i<list.size(); i++) {
            jobj.putAll((Map)list.get(i));
            String tempValue = jobj.toString();
            tempValue = parseStringFielter(tempValue);
            response.getWriter().write(tempValue);
            if (i < (list.size()-1)) {
                response.getWriter().write(",");
            }
            jobj.clear();
        }
        response.getWriter().write("]}");
    }

    public static void mblJsonResponseUserInfo(HttpServletResponse response, Map userInfo) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        String userId = StringUtil.nullValue(userInfo.get("USER_ID"));
        String userNm = StringUtil.nullValue(userInfo.get("USER_NM"));
        String relPsnNo = StringUtil.nullValue(userInfo.get("REL_PSN_NO"));
        String mdmUseYn = StringUtil.nullValue(userInfo.get("MDM_USE_YN"));

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"result\" : \"SUCCESS\",\"userInfo\" :");
        jsonStr.append("{\"userId\" : \""+userId+"\", ");
        jsonStr.append("\"userNm\" : \""+userNm+"\", ");
        jsonStr.append("\"relPsnNo\" : \""+relPsnNo+"\", ");
        jsonStr.append("\"mdmUseYn\" : \""+mdmUseYn+"\"}}");

        response.getWriter().write(jsonStr.toString());

		/*jsonStr.append("\"sttsList\" : [");


		for(int i=0; i<sttsList.size(); i++) {
			jobj.putAll((Map)sttsList.get(i));
			String tempValue = jobj.toString();
			tempValue = parseStringFielter(tempValue);
			response.getWriter().write(tempValue);
			if (i < (sttsList.size()-1)) {
				response.getWriter().write(",");
			}
			jobj.clear();
		}*/

		/*response.getWriter().write("],\"sttsDcdList\" : [");

		for(int i=0; i<sttsDcdList.size(); i++) {
			jobj.putAll((Map)sttsDcdList.get(i));
			String tempValue = jobj.toString();
			tempValue = parseStringFielter(tempValue);
			response.getWriter().write(tempValue);
			if (i < (sttsDcdList.size()-1)) {
				response.getWriter().write(",");
			}
			jobj.clear();
		}*/

        /*response.getWriter().write("]}}");*/
    }

    public static void mblJsonResponseLogin(HttpServletResponse response, Map userInfo, List sttsList
            , List menulist, String levelColNm, String labelColNm, String relColNm, String idColNm) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        String userId = StringUtil.nullValue(userInfo.get("USER_ID"));
        String userNm = StringUtil.nullValue(userInfo.get("USER_NM"));

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"result\" : \"SUCCESS\"");
        response.getWriter().write(jsonStr.toString());

		/*jsonStr.append("\"userInfo\" :");
		jsonStr.append("{\"userId\" : \""+userId+"\",");
		jsonStr.append("\"userNm\" : "+userNm+",");

		jsonStr.append("\"sttsList\" : [");
		response.getWriter().write(jsonStr.toString());

		for(int i=0; i<sttsList.size(); i++) {
			jobj.putAll((Map)sttsList.get(i));
			String tempValue = jobj.toString();
			tempValue = parseStringFielter(tempValue);
			response.getWriter().write(tempValue);
			if (i < (sttsList.size()-1)) {
				response.getWriter().write(",");
			}
			jobj.clear();
		}

		response.getWriter().write("]},");*/

        if(menulist.size() > 0){
            StringBuffer jsonStrTree = new StringBuffer();
            jsonStrTree.append(",\"menuTree\" :");

            String json = "";
            json = getJsonTree(menulist, levelColNm, labelColNm, relColNm, idColNm);

            jsonStrTree.append(json);

            response.getWriter().write(jsonStrTree.toString());
        }

        response.getWriter().write("}");
    }



    public static void jsonResponse(HttpServletResponse response, List list, String page_num, int page_size, int count) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        String page = page_num;
        String total = ((count/page_size)+(count%page_size==0?0:1))+"";
        String records = count+"";

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"page\" : \""+page+"\",");
        jsonStr.append("\"total\" : "+total+",");
        jsonStr.append("\"records\" : \""+records+"\",");
        jsonStr.append("\"user\" : [");
        response.getWriter().write(jsonStr.toString());
        logger.debug(list.size()+"/"+page+"/"+total+"/"+records);
        for(int i=0; i<list.size(); i++) {
            jobj.putAll((Map)list.get(i));
            String tempValue = jobj.toString();
            tempValue = parseStringFielter(tempValue);
            response.getWriter().write(tempValue);
            if (i < (list.size()-1)) {
                response.getWriter().write(",");
            }
            jobj.clear();
        }
        response.getWriter().write("]}");
    }

    public static void mblJsonResponse(HttpServletResponse response, List list, String page_num, int page_size, int count) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        String page = page_num;
        String total = ((count/page_size)+(count%page_size==0?0:1))+"";
        String records = count+"";

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"pageNum\" : \""+page+"\",");
        jsonStr.append("\"pageSize\" : "+page_size+",");
        jsonStr.append("\"totalCount\" : \""+records+"\",");
        jsonStr.append("\"list\" : [");
        response.getWriter().write(jsonStr.toString());
        logger.debug(list.size()+"/"+page+"/"+total+"/"+records);
        for(int i=0; i<list.size(); i++) {
            jobj.putAll((Map)list.get(i));
            String tempValue = jobj.toString();
            tempValue = parseStringFielter(tempValue);
            response.getWriter().write(tempValue);
            if (i < (list.size()-1)) {
                response.getWriter().write(",");
            }
            jobj.clear();
        }
        response.getWriter().write("]}");
    }


    public static void mblJsonResponse(HttpServletResponse response, String result) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"result\" : \""+result+"\"}");

        response.getWriter().write(jsonStr.toString());
    }

    /**
     * Tree 목록 데이터 Json 데이터 타입으로 클라이언트에 전송
     * @param response
     * @param list
     * @param levelColNm
     * @param labelColNm
     * @throws Exception
     */
    public static void jsonResponseTree(HttpServletResponse response, List list, String levelColNm, String labelColNm) throws Exception {
        //String json ="[{\"data\":\"Anode\",\"metadata\":{\"id\":\"1\"},\"children\":[{\"data\":\"child1\",\"metadata\":{\"id\":\"2\"}},{\"data\":\"A child2\",\"metadata\":{\"id\":\"3\"}}]}]";

        //String newline = "\n";
        String newline = "";
        String comma = ",";
        String json = "";
        json += "[{";
        int preLevel = 0;
        int i = 0;

        Iterator<?> it = list.iterator();
        while(it.hasNext()) {
            Map map = (Map) it.next();
            int treeLevel = Integer.parseInt(StringUtil.nullValue(map.get(levelColNm)));
            String treeLabel = StringUtil.nullValue(map.get(labelColNm));

            if (preLevel > 0) {
                if (preLevel+1 == treeLevel) {
                    json += newline+comma+"\"children\":[{";
                } else {
                    for(int k=preLevel; k>treeLevel; k--) {
                        json += newline+"}]";
                    }
                    if ((++i) < list.size()) {
                        json += "}"+comma+"{";
                    }
                }
            }

            json += newline+"\"data\":\""+treeLabel+"\"";
            json += comma;
            json += "\"metadata\":{";

            Set<?> keySet =  map.keySet();
            String tempKey = null;
            String tempValue = null;
            Iterator<?> keyIter = keySet.iterator();

            int j = 0;
            while(keyIter.hasNext()) {
                tempKey = (String) keyIter.next();
                tempValue = StringUtil.nullValue(map.get(tempKey));
                tempValue = parseStringFielterDQ(tempValue);
                json += "\""+tempKey+"\":\""+tempValue+"\"";
                if ((++j) < map.size()) {
                    json += comma;
                }
            }

            json += "}";

            preLevel = treeLevel;
        }

        for(int l=preLevel; l>0; l--) {
            json += newline+"}]";
        }

        //System.out.println("json:"+json);

        response.setContentType("application/json");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(json);
    }

    public static void jsonResponseTreeId(HttpServletResponse response, List list, String levelColNm, String labelColNm, String labelId) throws Exception {
        //String json ="[{\"data\":\"Anode\",\"metadata\":{\"id\":\"1\"},\"children\":[{\"data\":\"child1\",\"metadata\":{\"id\":\"2\"}},{\"data\":\"A child2\",\"metadata\":{\"id\":\"3\"}}]}]";

        //String newline = "\n";
        String newline = "";
        String comma = ",";
        String json = "";
        json += "[{";
        int preLevel = 0;
        int i = 0;

        Iterator<?> it = list.iterator();
        while(it.hasNext()) {
            Map map = (Map) it.next();
            int treeLevel = Integer.parseInt(StringUtil.nullValue(map.get(levelColNm)));
            String treeLabel = StringUtil.nullValue(map.get(labelColNm));
            String treeLabelId = StringUtil.nullValue(map.get(labelId));

            if (preLevel > 0) {
                if (preLevel+1 == treeLevel) {
                    json += newline+comma+"\"children\":[{";
                } else {
                    for(int k=preLevel; k>treeLevel; k--) {
                        json += newline+"}]";
                    }
                    if ((++i) < list.size()) {
                        json += "}"+comma+"{";
                    }
                }
            }

            //json += newline+"\"data\":\""+treeLabel+"\"";
            json += newline+"\"data\":\""+treeLabel+"\"";
            json += comma;
            json += "\"metadata\":{ ";

            Set<?> keySet =  map.keySet();
            String tempKey = null;
            String tempValue = null;
            Iterator<?> keyIter = keySet.iterator();

            int j = 0;
            while(keyIter.hasNext()) {
                tempKey = (String) keyIter.next();
                tempValue = StringUtil.nullValue(map.get(tempKey));
                tempValue = parseStringFielterDQ(tempValue);
                json += "\""+tempKey+"\":\""+tempValue+"\"";
                if ((++j) < map.size()) {
                    json += comma;
                }
            }

            json += "}";

            json += comma;
            json += "\"attr\":{\"id\":\""+treeLabelId+"\"} ";

            preLevel = treeLevel;
        }

        for(int l=preLevel; l>0; l--) {
            json += newline+"}]";
        }

        //System.out.println("json:"+json);

        response.setContentType("application/json");
//		response.setContentType("text/html; charset=UTF-8");
        response.setContentType("charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(json);
    }

    private static String getJsonTree(List list, String levelColNm, String labelColNm, String relColNm, String idColNm){
        String newline = "";
        String comma = ",";
        String json = "";
        json += "[{";
        int preLevel = 0;
        int i = 0;

        Iterator<?> it = list.iterator();
        while(it.hasNext()) {
            Map map = (Map) it.next();
            int treeLevel = Integer.parseInt(StringUtil.nullValue(map.get(levelColNm)));
            String treeLabel = StringUtil.nullValue(map.get(labelColNm));

            if (preLevel > 0) {
                if (preLevel+1 == treeLevel) {
                    json += newline+comma+"\"children\":[{";
                } else {
                    for(int k=preLevel; k>treeLevel; k--) {
                        json += newline+"}]";
                    }
                    if ((++i) < list.size()) {
                        json += "}"+comma+"{";
                    }
                }
            }

            json += newline+"\"data\":\""+treeLabel+"\"";
            json += comma;
            json += "\"metadata\":{";

            Set<?> keySet =  map.keySet();
            String tempKey = null;
            String tempValue = null;
            Iterator<?> keyIter = keySet.iterator();

            int j = 0;
            while(keyIter.hasNext()) {
                tempKey = (String) keyIter.next();
                tempValue = StringUtil.nullValue(map.get(tempKey));
                tempValue = parseStringFielterDQ(tempValue);
                json += "\""+tempKey+"\":\""+tempValue+"\"";
                if ((++j) < map.size()) {
                    json += comma;
                }
            }

            json += "}";

            json += comma;
            json += "\"attr\":{";

            tempValue = StringUtil.nullValue(map.get(idColNm));
            json += "\"id\":\""+tempValue+"\"";

            json += comma;

            tempValue = StringUtil.nullValue(map.get(relColNm));
            json += "\"rel\":\""+tempValue+"\"";

            json += "}";


            preLevel = treeLevel;
        }

        for(int l=preLevel; l>0; l--) {
            json += newline+"}]";
        }

        return json;
    }

    public static void jsonResponseTree(HttpServletResponse response, List list, String levelColNm, String labelColNm, String relColNm, String idColNm) throws Exception {
        //String json ="[{\"data\":\"Anode\",\"metadata\":{\"id\":\"1\"},\"children\":[{\"data\":\"child1\",\"metadata\":{\"id\":\"2\"}},{\"data\":\"A child2\",\"metadata\":{\"id\":\"3\"}}]}]";

        //String newline = "\n";
		/*String newline = "";
		String comma = ",";
		String json = "";
		json += "[{";
		int preLevel = 0;
		int i = 0;

		Iterator<?> it = list.iterator();
		while(it.hasNext()) {
			Map map = (Map) it.next();
			int treeLevel = Integer.parseInt(StringUtil.nullValue(map.get(levelColNm)));
			String treeLabel = StringUtil.nullValue(map.get(labelColNm));

			if (preLevel > 0) {
				if (preLevel+1 == treeLevel) {
					json += newline+comma+"\"children\":[{";
				} else {
					for(int k=preLevel; k>treeLevel; k--) {
						json += newline+"}]";
					}
					if ((++i) < list.size()) {
		            	json += "}"+comma+"{";
		    		}
				}
			}

			json += newline+"\"data\":\""+treeLabel+"\"";
			json += comma;
			json += "\"metadata\":{";

			Set<?> keySet =  map.keySet();
	        String tempKey = null;
	        String tempValue = null;
	        Iterator<?> keyIter = keySet.iterator();

	        int j = 0;
	        while(keyIter.hasNext()) {
	            tempKey = (String) keyIter.next();
	            tempValue = StringUtil.nullValue(map.get(tempKey));
	            tempValue = parseStringFielterDQ(tempValue);
	            json += "\""+tempKey+"\":\""+tempValue+"\"";
	            if ((++j) < map.size()) {
	            	json += comma;
	    		}
	        }

	        json += "}";

	        json += comma;
			json += "\"attr\":{";

			tempValue = StringUtil.nullValue(map.get(idColNm));
			json += "\"id\":\""+tempValue+"\"";

			json += comma;

			tempValue = StringUtil.nullValue(map.get(relColNm));
			json += "\"rel\":\""+tempValue+"\"";

			json += "}";


	        preLevel = treeLevel;
		}

		for(int l=preLevel; l>0; l--) {
			json += newline+"}]";
		}*/

        String json = "";
        json = getJsonTree(list, levelColNm, labelColNm, relColNm, idColNm);

        //System.out.println("json:"+json);

        response.setContentType("application/json");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(json);
    }

    public static String comboCodeName(List list) throws Exception {
        String strAll = "";
        for(int i=0; i<list.size(); i++) {
            Map map = (Map) list.get(i);
            strAll += StringUtil.nullValue(map.get("CODE")) + ":" + StringUtil.nullValue(map.get("NAME"));
            if (i < (list.size()-1)) {
                strAll += ";";
            }
        }
        return strAll;
    }

    public static String stringSelectDataJqgrid(List<?> list) throws Exception {
        String strAll = "";
        for(int i=0; i<list.size(); i++) {
            Map map = (Map) list.get(i);
            strAll += StringUtil.nullValue(map.get("CODE")) + ":" + StringUtil.nullValue(map.get("NAME"));
            if (i < (list.size()-1)) {
                strAll += ";";
            }
        }
        return strAll;
    }

    public static String stringSelectDataJqgridFilter(List<?> list) throws Exception {

        StringBuilder strAll = new StringBuilder();
        String key;
        Iterator iter;
        for(int i=0; i<list.size(); i++) {
            Map map = (Map) list.get(i);

            iter = map.keySet().iterator();
            while(iter.hasNext()){
                key = (String)iter.next();
                strAll.append(key).append(":").append(StringUtil.nullValue(map.get(key)));
                if(iter.hasNext()) strAll.append("^");
            }

            if (i < (list.size()-1)) {
                strAll.append(";");
            }
        }

        return strAll.toString();
    }

    /**
     *
     * @param paramString
     * @param strDiv1
     * @param strDiv2
     * @return
     */
    public static Map<String, String> getParamUser(String paramString, String strDiv1, String strDiv2) {
        Map<String, String> paramUser = new HashMap<String, String>();
        String[] params = paramString.split(strDiv1);
		/*for(int i=0; i<params.length; i++) {
			try {
				String[] entry = params[i].split(strDiv2);
				paramUser.put(entry[0], entry[1]);
			} catch(Exception e) { }
		}*/

        for (int i=0;i < params.length;i++) {
            String entry = params[i];
            String[] keyValue = entry.split(strDiv2,-1);

            if(StringUtil.isNullToString(keyValue[1]).equals("")==false){
                paramUser.put(StringUtil.isNullToString(keyValue[0]), StringUtil.isNullToString(keyValue[1]));
            } else {
                paramUser.put(StringUtil.isNullToString(keyValue[0]), "");
            }
        }

        return paramUser;
    }

    /**
     *
     * @param response
     * @param userInfo
     * @param sttsList
     * @param menulist
     * @param levelColNm
     * @param labelColNm
     * @param relColNm
     * @param idColNm
     * @return
     * @throws Exception
     */
    public static String mblJsonResponseLoginSso(HttpServletResponse response, Map userInfo, List sttsList, List menulist, String levelColNm,
                                                 String labelColNm, String relColNm, String idColNm) throws Exception {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        String userId = StringUtil.nullValue(userInfo.get("USER_ID"));
        String userNm = StringUtil.nullValue(userInfo.get("USER_NM"));

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"result\" : \"SUCCESS\"");


		/*jsonStr.append("\"userInfo\" :");
		jsonStr.append("{\"userId\" : \""+userId+"\",");
		jsonStr.append("\"userNm\" : "+userNm+",");

		jsonStr.append("\"sttsList\" : [");
		response.getWriter().write(jsonStr.toString());

		for(int i=0; i<sttsList.size(); i++) {
			jobj.putAll((Map)sttsList.get(i));
			String tempValue = jobj.toString();
			tempValue = parseStringFielter(tempValue);
			response.getWriter().write(tempValue);
			if (i < (sttsList.size()-1)) {
				response.getWriter().write(",");
			}
			jobj.clear();
		}

		response.getWriter().write("]},");*/
        String json = "";
        StringBuffer jsonStrTree = null;
        if(menulist.size() > 0){
            jsonStrTree = new StringBuffer();
            jsonStrTree.append(",\"menuTree\" :");

            json = getJsonTree(menulist, levelColNm, labelColNm, relColNm, idColNm);

            jsonStrTree.append(json);

        }

        jsonStr.append(jsonStrTree).append("}");
        return jsonStr.toString();

    }


    public static void jsonResponseAppDownloadInfo(HttpServletResponse response, Map<String, Object> appDownloadInfo) throws IOException
    {
        JSONObject jobj = new JSONObject();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        String appVersion = StringUtil.nullValue(appDownloadInfo.get("APP_VERSION"));

        StringBuffer jsonStr = new StringBuffer();
        jsonStr.append("{\"result\" : \"SUCCESS\",\"appDownloadInfo\" :");
        jsonStr.append("{\"appVersion\" : \""+appVersion+"\"}}");

        response.getWriter().write(jsonStr.toString());
    }

    public static String parseListToJsonP(List list) {
        String strAll = "";
        String strStart = "[";
        String strEnd = "]";
        System.out.println("CommonUtil.parseListToJsonP() list.size():"+list.size());
        if(list == null) {
            return "\"\"";
        }

        strAll += strStart;
        for(int i=0; i<list.size(); i++) {
            strAll += parseMapToJson((Map) list.get(i));
            if (i < (list.size()-1)) {
                strAll += ",";
            }
        }
        strAll += strEnd;

        return strAll;
    }

    public static String mapKeysToString(Map map) throws Exception {
        StringBuilder sb = new StringBuilder();

        if (map == null) {
            return null;
        }

        Set<?> keySet = map.keySet();
        Iterator<?> keyIter = keySet.iterator();

        int i = 0;
        while (keyIter.hasNext()) {
            if(sb.length() > 0) {
                sb.append(',');
            }
            sb.append((String) keyIter.next());
        }

        //System.out.println(sb.toString());

        return sb.toString();
    }

    /**
     * @category 서버모드
     * @return 로컬:local, 개발:dev, 운영:real
     */
    public static String serverMode() throws Exception{
        String mode = "dev";

        Properties props = System.getProperties();
        String commandName = StringUtil.nullConvert((String) props.get("sun.java.command")).toUpperCase();

        String[] compareValsReal = {"DGSTTHWAS1_UDGIST1", "DGSTTHWAS1_UDGIST2", "DGSTTHWAS2_UDGIST1", "DGSTTHWAS2_UDGIST2"};//운영 WAS 명
        String[] compareValsLocal = {"ORG.APACHE.CATALINA.STARTUP.BOOTSTRAP START"};

        // 운영인지 체크
        for(String v : compareValsReal){
            if(commandName.indexOf(v) > -1){
                mode = "real";

                break;
            }
        }

        // 로컬인지 체크
        for(String v : compareValsLocal){
            if(commandName.indexOf(v) > -1){
                mode = "local";

                break;
            }
        }

        return mode;
    }
}
