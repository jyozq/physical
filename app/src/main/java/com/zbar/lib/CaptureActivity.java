package com.zbar.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.straw.lession.physical.R;
import com.straw.lession.physical.activity.base.ThreadBaseActivity;
import com.straw.lession.physical.constant.Gender;
import com.straw.lession.physical.custom.AlertDialogUtil;
import com.straw.lession.physical.db.DBService;
import com.straw.lession.physical.utils.AppPreference;
import com.straw.lession.physical.utils.DateUtil;
import com.straw.lession.physical.utils.Detect;
import com.straw.lession.physical.vo.LoginInfoVo;
import com.straw.lession.physical.vo.db.StudentDevice;
import com.straw.lession.physical.vo.item.StudentItemInfo;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

public class CaptureActivity extends ThreadBaseActivity implements Callback,View.OnClickListener {

	private static final String TAG = "CaptureActivity";
	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;
	private Button nextBtn;
	private Button lightBtn;
	private TextView student_name,student_no,device_no,gender;
	private StudentItemInfo currentStudent;
	private ArrayList<StudentItemInfo> studentItemInfos;
	private LoginInfoVo loginInfoVo;
	private Dialog dialog;

	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qr_scan);

		currentStudent = (StudentItemInfo)getIntent().getSerializableExtra("student");
		studentItemInfos = (ArrayList<StudentItemInfo>)getIntent().getSerializableExtra("students");

		// 初始化 CameraManager
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
		nextBtn = (Button) findViewById(R.id.btn_next_student);
		lightBtn = (Button) findViewById(R.id.btn_light);
		student_name = (TextView)findViewById(R.id.student_name);
		student_no = (TextView)findViewById(R.id.student_no);
		device_no = (TextView)findViewById(R.id.device_no);
		gender = (TextView)findViewById(R.id.gender);

		nextBtn.setOnClickListener(this);
		lightBtn.setOnClickListener(this);
		student_name.setText(currentStudent.getName());
		student_no.setText(currentStudent.getCode());
		String deviceNo = currentStudent.getDeviceNo();
		device_no.setText(Detect.notEmpty(deviceNo)?deviceNo:getResources().getString(R.string.unmatch));
		gender.setText(Gender.getName(currentStudent.getGender()));

		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
			lightBtn.setText(R.string.btn_close_light);
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
			lightBtn.setText(R.string.btn_open_light);
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		try{
			loginInfoVo = AppPreference.getLoginInfo();
		}catch(Exception ex){
			ex.printStackTrace();
			Log.e(TAG,"",ex);
			return;
		}

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void doAfterGetToken() {

	}

	@Override
	protected void loadDataFromService() {

	}

	@Override
	protected void loadDataFromLocal() {

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		device_no.setText(result);
		List<StudentDevice> studentDeviceInfos = DBService.getInstance(this)
				.getStudentDeviceInfoNotUploaded(currentStudent.getStudentIdR(),loginInfoVo.getUserId(),
						currentStudent.getCourseDefindIdR());
		boolean isAdd = true;
		if(Detect.notEmpty(studentDeviceInfos)) {
			for (StudentDevice studentDevice : studentDeviceInfos) {
				if (DateUtil.isToday(studentDevice.getBindTime())) {
					isAdd = false;
					studentDevice.setDeviceNo(result);
					studentDevice.setBindTime(new Date());
				}
			}
		}
		if(isAdd){
			StudentDevice studentDevice = new StudentDevice();
			studentDevice.setCourseDefineIdR(currentStudent.getCourseDefindIdR());
			studentDevice.setBindTime(new Date());
			studentDevice.setDeviceNo(result);
			studentDevice.setIsUploaded(false);
			studentDevice.setStudentIdR(currentStudent.getStudentIdR());
			studentDevice.setTeacherIdR(loginInfoVo.getUserId());
			DBService.getInstance(this).addStudentDevice(studentDevice);
		}
		dialog = AlertDialogUtil.showAlertWindow(this, -1,
				"学生" + currentStudent.getName() + "(学号:" + currentStudent.getCode() + ")与设备" + result + "已绑定", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						switchToNext();
						CaptureActivity.this.dialog.dismiss();
						handler.sendEmptyMessage(R.id.restart_preview);
					}
				});
	}

	private void switchToNext() {
		currentStudent = studentItemInfos.remove(0);
		student_name.setText(currentStudent.getName());
		student_no.setText(currentStudent.getCode());
		device_no.setText(getResources().getString(R.string.unmatch));
		gender.setText(Gender.getName(currentStudent.getGender()));
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(false);
			

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(CaptureActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_next_student:
				// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
				handler.sendEmptyMessage(R.id.restart_preview);
				break;
			case R.id.btn_light:
				light();
				break;
			default:
				break;
		}
	}
}