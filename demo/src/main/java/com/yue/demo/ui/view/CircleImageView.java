package com.yue.demo.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yue.demo.R;

/**
 * 自定义圆形ImageView图片
 * 
 * @author chengyue
 * 
 */
public class CircleImageView extends ImageView {

	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 2;

	private static final int DEFAULT_BORDER_WIDTH = 0;
	private static final int DEFAULT_FILL_WIDTH = 0;
	private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
	private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
	private static final boolean DEFAULT_BORDER_OVERLAY = false;

	//图片显示区域(初始图片显示区域为mBorderRect)
	private final RectF mDrawableRect = new RectF();
	//含边界显示区域
	private final RectF mBorderRect = new RectF();

	private final Matrix mShaderMatrix = new Matrix();
	private final Paint mBitmapPaint = new Paint();
	private final Paint mBorderPaint = new Paint();
	private final Paint mFillPaint = new Paint();

	private int mBorderColor = DEFAULT_BORDER_COLOR;
	private int mBorderWidth = DEFAULT_BORDER_WIDTH;
	private int mFillColor = DEFAULT_FILL_COLOR;
	//中间圆环宽度
	private int mFillWidth = DEFAULT_FILL_WIDTH;

	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private int mBitmapWidth;
	private int mBitmapHeight;

	//内环图片半径
	private float mDrawableRadius;
	//边界半径
	private float mBorderRadius;
	

	private ColorFilter mColorFilter;

	private boolean mReady;
	private boolean mSetupPending;
	private boolean mBorderOverlay;

	public CircleImageView(Context context) {
		super(context);
		init();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 构造函数
	 */
	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 通过obtainStyledAttributes 获得一组值赋给 TypedArray（数组） ,
		// 这一组值来自于res/values/attrs.xml中的name="CircleImageView"的declare-styleable中。
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleImageView, defStyle, 0);
		// 通过TypedArray提供的一系列方法getXXXX取得我们在xml里定义的参数值；
		// 获取边界的宽度
		mBorderWidth = a.getDimensionPixelSize(
				R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
		// 获取边界的颜色
		mBorderColor = a.getColor(R.styleable.CircleImageView_border_color,
				DEFAULT_BORDER_COLOR);
		mBorderOverlay = a.getBoolean(
				R.styleable.CircleImageView_border_overlay,
				DEFAULT_BORDER_OVERLAY);
		mFillColor =a.getColor(R.styleable.CircleImageView_fill_color,
				DEFAULT_FILL_COLOR);
		
		mFillWidth =a.getDimensionPixelSize(R.styleable.CircleImageView_fill_width,
				DEFAULT_FILL_WIDTH);
		// 调用 recycle() 回收TypedArray,以便后面重用
		a.recycle();
		System.out.println("CircleImageView -- 构造函数");
		init();
	}

	/**
	 * 作用就是保证第一次执行setup函数里下面代码要在构造函数执行完毕时调用
	 */
	private void init() {
		// 在这里ScaleType被强制设定为CENTER_CROP，就是将图片水平垂直居中，进行缩放。
		super.setScaleType(SCALE_TYPE);
		mReady = true;

		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}

	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException(String.format(
					"ScaleType %s not supported.", scaleType));
		}
	}

	@Override
	public void setAdjustViewBounds(boolean adjustViewBounds) {
		if (adjustViewBounds) {
			throw new IllegalArgumentException(
					"adjustViewBounds not supported.");
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//如果图片不存在就不画
		if (mBitmap == null) {
			return;
		}

		if (mFillColor != Color.TRANSPARENT) {
			canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f,
					mDrawableRadius, mFillPaint);
		}
		//绘制内圆形，参数内圆半径，图片画笔为mBitmapPaint
		canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f,
				mDrawableRadius-mFillWidth, mBitmapPaint);
		//如果圆形边缘的宽度不为0 我们还要绘制带边界的外圆形 参数外圆半径，边界画笔为mBorderPaint
		if (mBorderWidth != 0) {
			canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f,
					mBorderRadius, mBorderPaint);
		}
		
	}
	

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int borderColor) {
		if (borderColor == mBorderColor) {
			return;
		}

		mBorderColor = borderColor;
		mBorderPaint.setColor(mBorderColor);
		invalidate();
	}

	public void setBorderColorResource(int borderColorRes) {
		setBorderColor(getContext().getResources().getColor(borderColorRes));
	}

	public int getFillColor() {
		return mFillColor;
	}

	public void setFillColor(int fillColor) {
		if (fillColor == mFillColor) {
			return;
		}

		mFillColor = fillColor;
		mFillPaint.setColor(fillColor);
		invalidate();
	}

	public void setFillColorResource(int fillColorRes) {
		setFillColor(getContext().getResources().getColor(fillColorRes));
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		if (borderWidth == mBorderWidth) {
			return;
		}

		mBorderWidth = borderWidth;
		setup();
	}

	public boolean isBorderOverlay() {
		return mBorderOverlay;
	}

	public void setBorderOverlay(boolean borderOverlay) {
		if (borderOverlay == mBorderOverlay) {
			return;
		}

		mBorderOverlay = borderOverlay;
		setup();
	}
	
	public int getmFillWidth() {
		return mFillWidth;
	}

	public void setmFillWidth(int mFillWidth) {
		this.mFillWidth = mFillWidth;
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		System.out.println("setImageDrawable -- setup");
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
		setup();
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		if (cf == mColorFilter) {
			return;
		}

		mColorFilter = cf;
		applyColorFilter();
		invalidate();
	}

	private void applyColorFilter() {
		if (mBitmapPaint != null) {
			mBitmapPaint.setColorFilter(mColorFilter);
		}
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 这个函数很关键，进行图片画笔边界画笔(Paint)一些重绘参数初始化：
	 * 构建渲染器BitmapShader用Bitmap来填充绘制区域,设置样式以及内外圆半径计算等，
	 * 以及调用updateShaderMatrix()函数和 invalidate()函数；
	 */
	private void setup() {
		// 因为mReady默认值为false,所以第一次进这个函数的时候if语句为真进入括号体内
		// 设置mSetupPending为true然后直接返回，后面的代码并没有执行。
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (getWidth() == 0 && getHeight() == 0) {
			return;
		}

		// 防止空指针异常
		if (mBitmap == null) {
			invalidate();
			return;
		}

		// 构建渲染器，用mBitmap来填充绘制区域 ，参数值代表如果图片太小的话 就直接拉伸
		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		// 设置图片画笔反锯齿
		mBitmapPaint.setAntiAlias(true);
		// 设置图片画笔渲染器
		mBitmapPaint.setShader(mBitmapShader);
		// 设置边界画笔样式

		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);//画笔颜色
		mBorderPaint.setStrokeWidth(mBorderWidth);//画笔边界宽度

		mFillPaint.setStyle(Paint.Style.FILL);
		mFillPaint.setAntiAlias(true);
		mFillPaint.setColor(mFillColor);

		//获取的原图片的宽高
		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();

		
       
        // 设置含边界显示区域，取的是CircleImageView的布局实际大小，为方形，查看xml也就是160dp(240px)  getWidth得到是某个view的实际尺寸
		mBorderRect.set(0, 0, getWidth(), getHeight());
		//计算 圆形带边界部分（外圆）的最小半径，取mBorderRect的宽高减去一个边缘大小(画笔的宽度)的一半的较小值
		mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f,
				(mBorderRect.width() - mBorderWidth) / 2.0f);

		// 初始图片显示区域为mBorderRect（CircleImageView的布局实际大小）
		mDrawableRect.set(mBorderRect);
		if (!mBorderOverlay) {
			//通过inset方法  使得图片显示的区域从mBorderRect大小上下左右内移边界的宽度形成区域，查看xml边界宽度为2dp（3px）,所以方形边长为就是160-4=156dp(234px)
			mDrawableRect.inset(mBorderWidth, mBorderWidth);
		}
		//这里计算的是内圆的最小半径，也即去除边界宽度的半径
		mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f,
				mDrawableRect.width() / 2.0f);

		applyColorFilter();
		//设置渲染器的变换矩阵也即是mBitmap用何种缩放形式填充
		updateShaderMatrix();
		//手动触发ondraw()函数 完成最终的绘制
		invalidate();
	}

	/**
	    * 这个函数为设置BitmapShader的Matrix参数，设置最小缩放比例，平移参数。
	    * 作用：保证图片损失度最小和始终绘制图片正中央的那部分
	    */
	    private void updateShaderMatrix() {
	        float scale;
	        float dx = 0;
	        float dy = 0;
	 
	        mShaderMatrix.set(null);
	        // 这里不好理解 这个不等式也就是(mBitmapWidth / mDrawableRect.width()) > (mBitmapHeight / mDrawableRect.height())
	        //取最小的缩放比例
	        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
	            //y轴缩放 x轴平移 使得图片的y轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
	            scale = mDrawableRect.height() / (float) mBitmapHeight;
	            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
	        } else {
	            //x轴缩放 y轴平移 使得图片的x轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
	            scale = mDrawableRect.width() / (float) mBitmapWidth;
	            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
	        }
	        // shaeder的变换矩阵，我们这里主要用于放大或者缩小。
	        mShaderMatrix.setScale(scale, scale);
	        // 平移
	        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);
	        // 设置变换矩阵
	        mBitmapShader.setLocalMatrix(mShaderMatrix);
	    }

	
}