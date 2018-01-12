package com.wei.permissionsetting.widget;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wei.permissionsetting.R;

/**
 * @author: WEI
 * @date: 2018/1/12
 */

public class ToastTips extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastTips(Context context) {
        super(context);
    }

    public static ToastTips makeText(Context context, CharSequence text, int duration) {
        ToastTips result = new ToastTips(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.view_pointstips, null);
        TextView tv = (TextView) v.findViewById(R.id.tips_msg);
        tv.setText(text);

        TextView tv2 = (TextView) v.findViewById(R.id.tips_score);
        tv2.setText("+3");

        result.setView(v);
        // setGravity方法用于设置位置，此处为垂直居中
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);
        return result;
    }
}
