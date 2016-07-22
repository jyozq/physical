package com.straw.lession.physical.http;
/**
 * Created by Administrator on 2014-10-13.
 */

import android.util.Log;

import com.straw.lession.physical.constant.ReqConstant;
import com.straw.lession.physical.utils.Detect;
import com.straw.lession.physical.utils.EncryptUtil;
import com.straw.lession.physical.utils.LoggerFile;
import com.straw.lession.physical.utils.ResponseParseUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Title: AsyncHttpClient
 * @Description: 通讯请求HTTP部分，此类仅限制于Android2.3及以上版本使用，Android2.2及以下版本有BUG
 * <href>http://code.google.com/p/android/issues/detail?id=2939</href>
 * 解决办法:
 * if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {        System.setProperty("http.keepAlive", "false");

 */

public class AsyncHttpClient implements Runnable {
    private final String TAG = "AsyncHttpClient";
    private LoggerFile.Log4jWrapper logger4j;

    private final int CONNECT_TIME_OUT = 30000;
    private final int READ_TIME_OUT = 30000;
    private String requestUrl;
    private AsyncHttpResponseHandler mCallback;
    private RequestType type;
    private RequestParams params;
    private String token;

	private ArrayList<BasicNameValuePair> basicNameValuePairs;

    private boolean isDes = false;  //是否要加密
    
    public AsyncHttpClient(RequestType type , String requestString,
                           ArrayList<BasicNameValuePair> basicNameValuePairs , String token, AsyncHttpResponseHandler callback ) {
    		this.requestUrl = requestString;
    		this.mCallback = callback;
    		this.type = type;
    		this.basicNameValuePairs = basicNameValuePairs;
    		this.logger4j = LoggerFile.getLog4j(TAG);
            this.token = token;
        }

    public AsyncHttpClient(RequestType type , String requestString,
                           ArrayList<BasicNameValuePair> basicNameValuePairs , boolean isDes , AsyncHttpResponseHandler callback ) {
        this.requestUrl = requestString;
        this.mCallback = callback;
        this.type = type;
        this.basicNameValuePairs = basicNameValuePairs;
        this.logger4j = LoggerFile.getLog4j(TAG);
        this.isDes = isDes;
    }

    private String createPOSTString() {
		try {
			if (basicNameValuePairs == null) {
				basicNameValuePairs = new ArrayList<BasicNameValuePair>();
			}
            JSONObject body = new JSONObject();
            for (int i = 0; i < basicNameValuePairs.size(); i++) {
                BasicNameValuePair pair = basicNameValuePairs.get(i);
                body.put(pair.getName(), pair.getValue());
            }

//            if (null !=params2 && params2.size() > 0 ){
//                for (Map.Entry<String, JSONArray> entry : params2.entrySet()) {
//                    String key = entry.getKey();
//                    JSONArray valueArray = entry.getValue();
//                    body.put(key , valueArray );
//                }
//            }
            String postStr = body.toString();
            if (isDes){
                String desStr = EncryptUtil.desEncrypt(postStr , EncryptUtil.SALT_KEY) ;
                return desStr ;
            }else {
                Log.i("999999999999999" , "999999999999999 param:" + body.toString() );
                return body.toString();
            }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

    private String createGetString (){
        try {
            if (basicNameValuePairs == null) {
                basicNameValuePairs = new ArrayList<BasicNameValuePair>();
            }
            StringBuilder post = new StringBuilder();
            post.append("?test=1");
            for (int i = 0; i < basicNameValuePairs.size(); i++) {
                BasicNameValuePair pair = basicNameValuePairs.get(i);
                post.append("&").append(pair.getName()).append("=").append(pair.getValue());
            }
            String result = post.toString().replace("test=1&", "");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    
    public void run() {
        if (type == null) {
            throw new IllegalArgumentException("NetworkRequest must specify a request type");
        }
        StringBuilder sb = new StringBuilder();
        HttpURLConnection conn = null;
        //GET
        if (type == RequestType.GET || type == RequestType.GET_PORTRAIT
                || type == RequestType.GET_AUDIO
                || type == RequestType.GET_IMAGE) {
            // GET请求
            try {
                String getstring  = createGetString();
                String newurl = requestUrl + getstring;
                URL url = new URL(newurl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CONNECT_TIME_OUT);
                conn.setReadTimeout(READ_TIME_OUT);
                if(Detect.notEmpty(token)) {
                    conn.setRequestProperty("Authorization", ReqConstant.TOKEN_PREFIX + token);
                }
                InputStream is = conn.getInputStream();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    if (mCallback != null) {
                        mCallback.sendFailureMessage(null, "response code error:" + conn.getResponseCode());
                    }
                    conn.disconnect();
                    return;
                }
                if (type == RequestType.GET) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    sb.setLength(0);
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    if (mCallback != null) {
                    	HttpResponseBean httpResponseBean = new HttpResponseBean();
                    	try{
                    		httpResponseBean.content = sb.toString();
                    	}catch(Exception e){
                    		
                    	}
                        mCallback.sendSuccessMessage(conn.getResponseCode(), conn.getHeaderFields(), httpResponseBean);
                    }

                } else {
                    logger4j.info("GET  有文件下载");
                    /*File file = null;
                    if (type == RequestType.GET_PORTRAIT) {
                        file = FileUtil.createCacheFile(
                                String.valueOf(requestUrl.hashCode()),
                                FileUtil.Type.PORTRAIT);
                    } else if (type == RequestType.GET_IMAGE) {
                        file = FileUtil.createCacheFile(
                                String.valueOf(requestUrl.hashCode()),
                                FileUtil.Type.IMAGE);
                    } else if (type == RequestType.GET_AUDIO) {
                        file = FileUtil.createCacheFile(
                                String.valueOf(requestUrl.hashCode()),
                                FileUtil.Type.AUDIO);
                    }
                    if (file == null) {
                        if (mCallback != null) {
                            mCallback.onResponse(IO_EXCEPTION_REEOR, null);
                        }
                        return;
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length = 0;
                    while ((length = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, length);
                    }
                    fos.flush();
                    fos.close();
                    if (mCallback != null) {
                        mCallback.onResponse(RESPONSE_SUCCESS, file);
                    }
                    buffer = null;
                    logger4j.info("GET 文件返回:"+file.getAbsolutePath());*/
                }
                conn.disconnect();
                return;
            } catch (MalformedURLException e) {
                logger4j.info("GET MalformedURLException:"+e.toString());
                if (mCallback != null) {
                    mCallback.sendFailureMessage(e, "GET MalformedURLException");
                }
            } catch (IOException e) {
                logger4j.info("GET IOException:"+e.toString());
                if (mCallback != null) {
                    mCallback.sendFailureMessage(e, "GET IOException");
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } 
        
        //post
        else if (type == RequestType.POST) {
            // POST请求
            try {
            	//String poststring = createPOSTString();
                String newurl = requestUrl ;
                //logger4j.info("POST requestUrl:"+requestUrl);
                URL url = new URL(newurl  );
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CONNECT_TIME_OUT);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setReadTimeout(READ_TIME_OUT);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                //conn.setRequestProperty("contentType", "GBK");

                // 数据输出流
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                
                // 构建post数据
                
                /*sb.setLength(0);
                for(ConcurrentHashMap.Entry<String, String> entry : params.urlParams.entrySet()) {
                    final String key = entry.getKey();
                    final String value = entry.getValue();
                    sb.append(key);
                    sb.append("=");
                    //sb.append(value);  //中文会乱码
                    sb.append( URLEncoder.encode(value, "UTF-8") );
                    sb.append("&");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                dos.write(sb.toString().getBytes());*/
                
                String poststring = createPOSTString();
                dos.write(poststring.getBytes());
                
                // 刷新输出流
                dos.flush();
                dos.close();
                //logger4j.info("POST 发送的String:"+ poststring);
                // 获取返回数据
                InputStream is = conn.getInputStream();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    if (mCallback != null) {
                        mCallback.sendFailureMessage(null, "response code error:" + conn.getResponseCode());
                    }
                    conn.disconnect();
                    return;
                }
                sb.setLength(0);
//                BufferedReader br = new BufferedReader( new InputStreamReader(is));
                BufferedReader br = new BufferedReader( new InputStreamReader(is , "UTF-8" ));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                if (mCallback != null) {
                	HttpResponseBean httpResponseBean = new HttpResponseBean();
                	try{
                		/*JSONObject res = new JSONObject(sb.toString());
                    	if (res.has("header")){
                    		httpResponseBean.header = res.getJSONObject("header");
                    	}
                    	if (res.has("body")){
                    		httpResponseBean.body = res.getJSONObject("body").toString();
                    	}*/
                		
                		String result = sb.toString();
                		//result=new  String(result.getBytes("ISO-8859-1"),"UTF-8");
                        //解密
                        if (isDes){
                            String desEncryptResult = EncryptUtil.desDecrypt(result , EncryptUtil.SALT_KEY) ;
                            httpResponseBean.content = desEncryptResult;
                        }else {
                            httpResponseBean.content = result;
                        }

                	}catch(Exception e){
                		
                	}
                    mCallback.sendSuccessMessage(conn.getResponseCode(), conn.getHeaderFields(), httpResponseBean);
                }
                conn.disconnect();
                //logger4j.info("POST "+" 返回:"+sb.toString());
            } catch (MalformedURLException e) {
                logger4j.info("POST MalformedURLException:"+e.toString());
                if (mCallback != null) {
                    mCallback.sendFailureMessage(e, ResponseParseUtils.ERROR1);
                }
            }
            catch (ConnectException e){ //网络连接异常
                logger4j.info("POST ConnectException:"+e.toString());
                if (mCallback != null) {
                    mCallback.sendFailureMessage(e, ResponseParseUtils.ERROR2);
                }
            }
            catch (SocketTimeoutException e){ //连接超时异常
                logger4j.info("POST SocketTimeoutException:"+e.toString());
                if (mCallback != null) {
                    mCallback.sendFailureMessage(e, ResponseParseUtils.ERROR4);
                }
            }
            catch (IOException e) {
                logger4j.info("POST IOException:"+e.toString());  //
                if (mCallback != null) {
                    mCallback.sendFailureMessage(e, ResponseParseUtils.ERROR3);
                }
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } 
        
        //上传
        else if (type == RequestType.POST_IMAGE
                || type == RequestType.POST_AUDIO) {
            if (params == null) {
                throw new IllegalArgumentException("Post request must specify arguments");
            }
            // 数据分割线
            String BOUNDARY = "---------------------------7de8c1a80910";
            // POST请求
            try {
                //logger4j.info("POST requestUrl:"+requestUrl);
                URL url = new URL(requestUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CONNECT_TIME_OUT);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setReadTimeout(READ_TIME_OUT);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty(
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E)");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + BOUNDARY);
                // 末尾数据分隔符
                byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                // 数据输出流
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                // 构建post数据
                sb.setLength(0);

                for(ConcurrentHashMap.Entry<String, String> entry : params.urlParams.entrySet()) {
                    String key = entry.getKey();
                    if (!key.equals("txtUpLoad")) {
                        // 向输出流中填写普通文本数据
                        sb.append("\r\n\r\n\r\n--" + BOUNDARY + "\r\n");
                        sb.append("Content-Disposition: form-data; name=\""
                                + key + "\"" + "\r\n\r\n");
                        sb.append(entry.getValue() + "\r\n");
                        dos.write(sb.toString().getBytes());
                        sb.setLength(0);
                    }
                }

                // 向输出流中填写上传的文件数据
                sb.append("\r\n\r\n--" + BOUNDARY + "\r\n");
                sb.append("Content-Disposition: form-data; name=\"txtUpLoad\"; filename=\""
                        + params.getUrlParams().get("txtUpLoad") + "\"" + "\r\n");
                if (type == RequestType.POST_IMAGE) {
                    sb.append("Content-Type: image/*\r\n\r\n");
                } else {
                    sb.append("Content-Type: audio/*\r\n\r\n");
                }
                dos.write(sb.toString().getBytes());
                sb.setLength(0);
                // 传输文件数据
                FileInputStream fis = new FileInputStream(new File(params.getUrlParams().get("txtUpLoad")));
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, length);
                }
                fis.close();

                dos.write(endData);
                // 刷新输出流
                dos.flush();
                dos.close();

                //logger4j.info("POST 发送的String:"+sb.toString());

                // 获取返回数据
                InputStream is = conn.getInputStream();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    if (mCallback != null) {
                        mCallback.sendFailureMessage(null, "response code error:" + conn.getResponseCode());
                    }
                    conn.disconnect();
                    return;
                }
                sb.setLength(0);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                if (mCallback != null) {
                	HttpResponseBean httpResponseBean = new HttpResponseBean();
                	try{
                		/*JSONObject res = new JSONObject(sb.toString());
                    	if (res.has("header")){
                    		httpResponseBean.header = res.getJSONObject("header");
                    	}
                    	if (res.has("body")){
                    		httpResponseBean.body = res.getJSONObject("body").toString();
                    	}*/
                		httpResponseBean.content = sb.toString();
                	}catch(Exception e){
                		
                	}
                    mCallback.sendSuccessMessage(conn.getResponseCode(), conn.getHeaderFields(), httpResponseBean );
                }
                conn.disconnect();
                //logger4j.info("POST "+" 返回:"+sb.toString());

            } catch (MalformedURLException e) {
                logger4j.info("POST MalformedURLException:"+e.toString());
                if (mCallback != null) {
                    mCallback.sendFailureMessage(e, "GET MalformedURLException");
                }
            } catch (IOException e) {
                logger4j.info("POST IOException:"+e.toString());  //查询失败![0001]
                if (mCallback != null) {
                    mCallback.sendFailureMessage(e, "GET IOException");
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }

    public static enum RequestType {
        /** 一般get请求，返回数据为字符串 */
        GET,
        /** 获取头像缩略图. */
        GET_PORTRAIT,
        /** 获取图片文件. */
        GET_IMAGE,
        /** 获取音频文件 */
        GET_AUDIO,
        /** 一般post请求，返回数据为字符串 */
        POST,
        /** post请求，上传数据为image */
        POST_IMAGE,
        /** post请求，上传数据为audio */
        POST_AUDIO;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
