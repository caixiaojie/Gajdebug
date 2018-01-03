package com.fskj.gaj.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fskj.gaj.R;


public class ExitLoginDialog {
	public interface OnConfirmClickListener {
		void onLeft();

		void onRight();
	}

	public static void show(Activity activity,  String content,
                            final OnConfirmClickListener listener, boolean cancel) {
		show(activity,  content, "取消","确定", listener,cancel);
	}

	public static void show(Activity activity, String content, String btn1, String btn2,
                            final OnConfirmClickListener listener, boolean cancel){
		final AlertDialog dialog = new AlertDialog.Builder(activity).setCancelable(cancel).create();// 创建对话框
		dialog.setCanceledOnTouchOutside(cancel);
		dialog.show();

		Window window = dialog.getWindow();// 获取窗口对象
		window.setContentView(R.layout.dialog_exit_login);// 设置内容布局

		TextView txtMessageText=(TextView)window.findViewById(R.id.txt_message_text);
		TextView txtMessageLeft=(TextView)window.findViewById(R.id.txt_message_btnleft);
		TextView txtMessageRight=(TextView)window.findViewById(R.id.txt_message_btnright);


		if(content!=null){
		txtMessageText.setText(content);
	}else{
		txtMessageText.setText(R.string.app_name);
	}
		
		
		if(btn1!=null){
			txtMessageLeft.setText(btn1);
		}
		
		if(btn2!=null){
			txtMessageRight.setText(btn2);
		}
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		WindowManager m = window.getWindowManager();
		Display d = m.getDefaultDisplay();
		WindowManager.LayoutParams p = window.getAttributes();
		Point outSize = new Point();
		d.getSize(outSize);
		// getSize (Point outSize)
		p.width = (int) (outSize.x * 0.75);
//		p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
//		p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//使其可以获得焦点
		window.setAttributes(p);
		txtMessageLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (listener != null) {
					listener.onLeft();
				}
			}
		});

		txtMessageRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (listener != null) {
					listener.onRight();
				}
			}
		});
	}
}
