package com.jixianxueyuan.record;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;

public class ProgressView extends View
{

	public ProgressView(Context context) {
		super(context);
		init(context);
	}

	public ProgressView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init(paramContext);
		
	}

	public ProgressView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		init(paramContext);
	}

	private Paint progressPaint, firstPaint, threePaint,breakPaint;//������ɫ�Ļ���
	private float firstWidth = 4f, threeWidth = 1f;//�ϵ�Ŀ��
	private LinkedList<Integer> linkedList = new LinkedList<Integer>();
	private float perPixel = 0l;
	private float countRecorderTime = 8000;//�ܵ�¼��ʱ��
	
	public void setTotalTime(float time){
		countRecorderTime = time;
	}
	
	private void init(Context paramContext) {
	        
		progressPaint = new Paint();
		firstPaint = new Paint();
		threePaint = new Paint();
		breakPaint = new Paint();

		// ����
		setBackgroundColor(Color.parseColor("#19000000"));

		// ��Ҫ���ȵ���ɫ
		progressPaint.setStyle(Paint.Style.FILL);
		progressPaint.setColor(Color.parseColor("#19e3cf"));

		// һ��һ���Ļ�ɫ����
		firstPaint.setStyle(Paint.Style.FILL);
		firstPaint.setColor(Color.parseColor("#ffcc42"));

		// 3�봦�Ľ���
		threePaint.setStyle(Paint.Style.FILL);
		threePaint.setColor(Color.parseColor("#12a899"));
		
		breakPaint.setStyle(Paint.Style.FILL);
		breakPaint.setColor(Color.parseColor("#000000"));

		DisplayMetrics dm = new DisplayMetrics();
		((Activity)paramContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		perPixel = dm.widthPixels/countRecorderTime;
		
		perSecProgress = perPixel;
		
	}
	
	/**
	 * ����״̬
	 * @author QD
	 *
	 */
	public static enum State {
		START(0x1),PAUSE(0x2);
		
		static State mapIntToValue(final int stateInt) {
			for (State value : State.values()) {
				if (stateInt == value.getIntValue()) {
					return value;
				}
			}
			return PAUSE;
		}

		private int mIntValue;

		State(int intValue) {
			mIntValue = intValue;
		}

		int getIntValue() {
			return mIntValue;
		}
	}
	
	
	private volatile State currentState = State.PAUSE;//��ǰ״̬
	private boolean isVisible = true;//һ��һ���Ļ�ɫ�����Ƿ�ɼ�
	private float countWidth = 0;//ÿ�λ�����ɣ��������ĳ���
	private float perProgress = 0;//��ָ����ʱ��������ÿ�������ĳ���
	private float perSecProgress = 0;//ÿ�����Ӧ�����ص�
	private long initTime;//�������ʱ��ʱ���
	private long drawFlashTime = 0;//�����Ļ�ɫ����ʱ���
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long curTime = System.currentTimeMillis();
		//Log.i("recorder", curTime  - initTime + "");
		countWidth = 0;
		//ÿ�λ��ƶ��������еĶϵ��ʱ��˳�򣬻��Ƴ���
		if(!linkedList.isEmpty()){
			float frontTime = 0;
			Iterator<Integer> iterator = linkedList.iterator();
			while(iterator.hasNext()){
				int time = iterator.next();
				//������λ��ƾ��ε����λ��
				float left = countWidth;
				//������λ��ƾ��ε��յ�λ��
				countWidth += (time-frontTime)*perPixel;
				//���ƽ�����
				canvas.drawRect(left, 0,countWidth,getMeasuredHeight(),progressPaint);
				//���ƶϵ�
				canvas.drawRect(countWidth, 0,countWidth + threeWidth,getMeasuredHeight(),breakPaint);
				countWidth += threeWidth;
				
				frontTime = time;
			}
			//�������봦�Ķϵ�
			 if(linkedList.getLast() <= 3000)
				canvas.drawRect(perPixel*3000, 0,perPixel*3000+threeWidth,getMeasuredHeight(),threePaint);
		}else//�������봦�Ķϵ�
			canvas.drawRect(perPixel*3000, 0,perPixel*3000+threeWidth,getMeasuredHeight(),threePaint);//�������봦�ľ���
		
		//����ָ��ס��Ļʱ��������������
		if(currentState == State.START){
			perProgress += perSecProgress*(curTime - initTime );
			if(countWidth + perProgress <= getMeasuredWidth())
				canvas.drawRect(countWidth, 0,countWidth + perProgress,getMeasuredHeight(),progressPaint);
			else
				canvas.drawRect(countWidth, 0,getMeasuredWidth(),getMeasuredHeight(),progressPaint);
		}
		//����һ��һ���Ļ�ɫ����ÿ500ms����һ��
		if(drawFlashTime==0 || curTime - drawFlashTime >= 500){
			isVisible = !isVisible;
			drawFlashTime = System.currentTimeMillis();
		}
		if(isVisible){
			if(currentState == State.START)
				canvas.drawRect(countWidth + perProgress, 0,countWidth + firstWidth + perProgress,getMeasuredHeight(),firstPaint);
			else
				canvas.drawRect(countWidth, 0,countWidth + firstWidth,getMeasuredHeight(),firstPaint);
		}
		//��������һ��һ���Ļ�ɫ����
		initTime = System.currentTimeMillis();
		invalidate();
	}
	
	/**
	 * ���ý�������״̬
	 * @param state
	 */
	public void setCurrentState(State state){
		currentState = state;
		if(state == State.PAUSE)
			perProgress = perSecProgress;
	}
	
	/**
	 * ��ָ̧��ʱ����ʱ��㱣�浽������
	 * @param time:msΪ��λ
	 */
	public void putProgressList(int time) {
		linkedList.add(time);
	}
}
