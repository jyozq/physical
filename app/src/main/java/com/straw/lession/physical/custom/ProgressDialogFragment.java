package com.straw.lession.physical.custom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.straw.lession.physical.R;

public class ProgressDialogFragment extends DialogFragment {
	private Dialog dialog ;
	public static ProgressDialogFragment getInstance(String whichType,String message) {
		ProgressDialogFragment mDialogFragment = new ProgressDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(TYPE, whichType);
		bundle.putSerializable(MESSAGE, message);
		mDialogFragment.setArguments(bundle);
		return mDialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int defaultSize =(int) getActivity().getResources().getDimension(R.dimen.font_size_14);
		dialog = new Dialog(getActivity() , R.style.PopDialogStyle  );
		dialog.setContentView(R.layout.tips_loading_view);
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (!mDismissable) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						getActivity().finish();
						return true;
					}
				}
				return false;
			}
		});
		String message = getArguments().getString(MESSAGE);
		TextView textView = (TextView) dialog.findViewById(R.id.tips_loading_msg);
		textView.setText(message);

		// 所有网络请求均不可被取消
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		dialog.setCanceledOnTouchOutside(true);


		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		float density = getActivity().getResources().getDisplayMetrics().density;
		int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
		params.width = (int) (screenWidth - 2 * density * mHorizontalMargin);
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
		return dialog;
	}

	public void isCancelable(boolean dismissable) {
		this.mDismissable = dismissable;

	}

	public void setHorizontalMargin(int horizontalMargin) {
		this.mHorizontalMargin = horizontalMargin;
	}

	public static final String TYPE = "type";
	public static final String MESSAGE = "message";
	public static final String DEFAULT = "default";
	public int mHorizontalMargin = 40;
	private static boolean mDismissable = true;
}
