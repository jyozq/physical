package com.straw.lession.physical.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by straw on 2016/7/18.
 */
public abstract class TaskHandler {
    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;

    protected Handler handler;
    public TaskHandler(){
        if(Looper.myLooper()!=null){
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    doHandleMessage(msg);
                }
            };
        }
    }

    /**
     * Fired when the request is started, override to handle in your own code
     */
    public void onStart() {}

    /**
     * Fired in all cases when the request is finished, after both success and failure, override to handle in your own code
     */
    public void onFinish() {}

    /**
     * Fired when a request returns successfully, override to handle in your own code
     */
    public abstract void onSuccess(TaskResult result);

    /**
     * Fired when a request fails to complete, override to handle in your own code
     * @param error the underlying cause of the failure
     * @deprecated use {@link #onFailure(Throwable, String)}
     */
    @Deprecated
    public void onFailure(Throwable error) {}

    /**
     * Fired when a request fails to complete, override to handle in your own code
     * @param error the underlying cause of the failure
     * @param content the response body, if any
     */
    public abstract void onFailure(Throwable error, String content);

    public void sendSuccessMessage(TaskResult result) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, result));
    }

    public void sendFailureMessage(Throwable e, String responseBody) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{e, responseBody}));
    }

    public void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    public void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    protected void sendMessage(Message msg) {
        if(handler != null){
            handler.sendMessage(msg);
        } else {
            doHandleMessage(msg);
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, response);
        }else{
            msg = Message.obtain();
            msg.what = responseMessage;
            msg.obj = response;
        }
        return msg;
    }

    public void doHandleMessage(Message msg){
        Object[] response;
        TaskResult taskResult;

        switch(msg.what) {
            case SUCCESS_MESSAGE:
                taskResult = (TaskResult)msg.obj;
                onSuccess(taskResult);
                break;
            case FAILURE_MESSAGE:
                response = (Object[])msg.obj;
                onFailure((Throwable)response[0], (String)response[1]);
                break;
            case START_MESSAGE:
                onStart();
                break;
            case FINISH_MESSAGE:
                onFinish();
                break;
            default:
                onSelf();
                break;
        }
    }

    protected abstract void onSelf();
}
