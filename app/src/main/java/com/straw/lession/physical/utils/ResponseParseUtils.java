package com.straw.lession.physical.utils;

/**
 * 数据格式解析 字段
 * @author Administrator
 *
 */
public class ResponseParseUtils {

    //resultCode
    public final static String RESULT_CODE_SUCCESS = "0";
    public final static String RESULT_CODE_ERROR = "04";

    //exception
    public final static String ERROR1 = "MalformedURLException";
    public final static String ERROR2 = "ConnectException";
    public final static String ERROR3 = "IOException";
    public final static String ERROR4 = "SocketTimeoutException";

    /*public static boolean parseHeader(JSONObject headerObject){
    	try {
			if (headerObject.has(ERRORCODE) && !TextUtils.isEmpty(headerObject.getString(ERRORCODE)) 
					&& !headerObject.getString(RESPCODE).equals("1")) {
				return false;
			}else {
				return true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }*/
    
    //解析接口返回 记录成功失败
    /*public static boolean parseContentResult(JSONObject contentObject){
    	try {
    		boolean successful = contentObject.getBoolean(successFul);
    		if ( successful ) {
				return true;
			}else {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
    }*/
    
    /**
     * 解析需要的数据
     * @param contentObject
     * @return
     */
   /* public static JSONObject parseContentData(JSONObject contentObject){
    	try {
    		
    		 if (!contentObject.isNull(successResponse)){
    			 JSONObject dataObject = contentObject.getJSONObject(successResponse);
    	    	 return dataObject;
    		 }
    		 return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
    }*/
    
    
    //解析错误信息
   /* public static String parseErrorInfo(JSONObject contentObject){
    	String errorMessage ="";
    	try{
    		if (contentObject.has(ResponseParseUtils.respDescription)){
        		errorMessage = contentObject.getString(ResponseParseUtils.respDescription);
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return errorMessage;
    }*/



}
