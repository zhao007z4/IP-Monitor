package com.zzy.net.sortlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zzy.net.R;

public class SideBar extends View
{
	private static final int DEFAULT_TEXT_COLOR =0xff336598;
	private static final int SELECT_TEXT_COLOR = 0xff3399ff;
	private static final int SELECT_BG_COLOR = Color.RED;

	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
	        "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
	private int choose = -1;
	private Paint paint = new Paint();

	private TextView mTextDialog;
	private float iLetterFontSize = 0f;

	private int textColor;
	private int selectTextColor;
	private int bgColor;
	private int bgRes;
	private DisplayMetrics dm;

	public void setTextView(TextView mTextDialog)
	{
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public SideBar(Context context, AttributeSet attrs)
	{
		this(context, attrs,0);
	}

	public SideBar(Context context)
	{
		this(context,null);
	}

	private void init(Context context, AttributeSet attrs)
	{
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SideBar, 0, 0);

		textColor = ta.getColor(R.styleable.SideBar_textColor, DEFAULT_TEXT_COLOR);
		bgColor = ta.getColor(R.styleable.SideBar_bgColor, SELECT_BG_COLOR);
		selectTextColor = ta.getColor(R.styleable.SideBar_bgColor, SELECT_TEXT_COLOR);
		bgRes = ta.getResourceId(R.styleable.SideBar_bgDraw,0);

		dm = getContext().getResources().getDisplayMetrics();
	}

	private float MeasureStringHeight(String Text, float TextSize)
	{
		this.paint.setTextSize(TextSize);
		this.paint.setTypeface(Typeface.DEFAULT);
		this.paint.setStrokeWidth(0.0F);
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setTextAlign(Paint.Align.LEFT);
		Rect r = new Rect();
		this.paint.getTextBounds(Text, 0, Text.length(), r);
		return r.height();
	}

	private float getLetterHeight(float singleHeight)
	{
		float iHeight=0;
		float textSize = 0f;
		for(int i=14;i>=0;i--)
		{
			textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, dm);
			iHeight = MeasureStringHeight("A",textSize);
			if(iHeight<singleHeight-10)
			{

				return textSize;
			}
		}
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, dm);
	}

	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / b.length;
		
		if(iLetterFontSize<1f)
		{
			iLetterFontSize = getLetterHeight(singleHeight);
		}

		for (int i = 0; i < b.length; i++)
		{
			paint.setColor(textColor);
			paint.setTypeface(Typeface.DEFAULT);
			paint.setAntiAlias(true);
			paint.setTextSize(iLetterFontSize);

			if (i == choose)
			{
				paint.setColor(selectTextColor);
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);

		switch (action)
		{
		case MotionEvent.ACTION_UP:
			setBackgroundColor(0x00000000);
			choose = -1;//
			invalidate();
			if (mTextDialog != null)
			{
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			if(bgRes !=0)
			{
				setBackgroundResource(bgRes);
			}
			else
			{
				setBackgroundColor(bgColor);
			}
			if (oldChoose != c)
			{
				if (c >= 0 && c < b.length)
				{
					if (listener != null)
					{
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null)
					{
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}

					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener)
	{
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener
	{
		public void onTouchingLetterChanged(String s);
	}

}