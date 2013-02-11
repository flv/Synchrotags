package fr.voltanite.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class Utils {

	public static void popDebug(Context context, String message) {
		Toast.makeText(context, message, 1000000).show();
	}
	
	public static void textViewDebug(Activity act,Context context, int viewId, String message)
	{
		View linearLayout = act.findViewById(viewId);		
		TextView txt1 = new TextView(context);
		txt1.setText(message);
		((ViewGroup) linearLayout).addView(txt1);
	}
	
	public static void emptyLinearLayout(Activity act, Context context, int viewId)
	{
		LinearLayout linearLayout = (LinearLayout) act.findViewById(viewId);
		linearLayout.removeAllViews();
	}

}
