package com.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FaceInfo {
	public int[] mfacesId = new int[] { R.mipmap.f000, R.mipmap.f001,
			R.mipmap.f002, R.mipmap.f003, R.mipmap.f004, R.mipmap.f005,
			R.mipmap.f006, R.mipmap.f007, R.mipmap.f008, R.mipmap.f009,
			R.mipmap.f010, R.mipmap.f011, R.mipmap.f012, R.mipmap.f013,
			R.mipmap.f014, R.mipmap.f015, R.mipmap.f016, R.mipmap.f017,
			R.mipmap.f018, R.mipmap.f019, R.mipmap.f020, R.mipmap.f021,
			R.mipmap.f022, R.mipmap.f023 };
	public List<String> faceList=new ArrayList<>();
	private Context mContext;

	public FaceInfo(Context context) {
		mContext = context;
		faceList.add("笑脸");
		faceList.add("微笑");
		faceList.add("流汗");
		faceList.add("偷笑");
		faceList.add("拜拜");
		faceList.add("敲打");
		faceList.add("擦汗");
		faceList.add("猪头");
		faceList.add("玫瑰");
		faceList.add("哭泣");
		faceList.add("大哭");
		faceList.add("憨笑");
		faceList.add("耍酷");
		faceList.add("大闹");
		faceList.add("委屈");
		faceList.add("狗屎");
		faceList.add("炸弹");
		faceList.add("菜刀");
		faceList.add("憨厚");
		faceList.add("色相");
		faceList.add("害羞");
		faceList.add("大兵");
		faceList.add("大吐");
		faceList.add("好感");
	}

	public SpannableStringBuilder imagistSpan(String tag) {
		Pattern p = Pattern.compile("#\\{(.+?)\\}");
		Matcher m = p.matcher(tag);
		SpannableStringBuilder ss = null;
		ss = new SpannableStringBuilder(tag);
		int position;
		while (m.find()) {
			String find = m.group(1);
			position = 0;
			for (String face : faceList) {
				if (find.compareTo(face) == 0) {
					break;
				}
				position++;
			}
			Drawable drawable = mContext.getResources().getDrawable(
					mfacesId[position]);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2,
					drawable.getIntrinsicHeight() / 2);
			ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM),
					m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return ss;
	}

}
