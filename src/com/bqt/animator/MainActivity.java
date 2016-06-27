package com.bqt.animator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridLayout;
	public class MainActivity extends Activity implements OnCheckedChangeListener {
	    private GridLayout gl_container;//放置按钮的父布局
	    private int mVal;//按钮的编号
	    private LayoutTransition mTransition;//布局动画，当容器中的视图发生变化时存在过渡的动画效果
	    private CheckBox mAppear, mChangeAppear, mDisAppear, mChangeDisAppear;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        gl_container = (GridLayout) findViewById(R.id.gl_container);
	        mAppear = (CheckBox) findViewById(R.id.id_appear);
	        mChangeAppear = (CheckBox) findViewById(R.id.id_change_appear);
	        mDisAppear = (CheckBox) findViewById(R.id.id_disappear);
	        mChangeDisAppear = (CheckBox) findViewById(R.id.id_change_disappear);
	        //CheckBox选中状态改变监听，默认全部选中，即动画全部开启
	        mAppear.setOnCheckedChangeListener(this);
	        mChangeAppear.setOnCheckedChangeListener(this);
	        mDisAppear.setOnCheckedChangeListener(this);
	        mChangeDisAppear.setOnCheckedChangeListener(this);
	        mTransition = new LayoutTransition();
	        gl_container.setLayoutTransition(mTransition);
	    }
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	        mTransition = new LayoutTransition();
	        //当一个View在ViewGroup中【出现】时，对此【View】设置的动画
	        mTransition.setAnimator(LayoutTransition.APPEARING, //=2，下面使用的是自定义的动画
	                (mAppear.isChecked() ? ObjectAnimator.ofFloat(this, "scaleX", 0, 1) : null));
	        //当一个View在ViewGroup中【出现】时，对此View对其他View位置造成影响，对【其他View】设置的动画
	        mTransition.setAnimator(LayoutTransition.CHANGE_APPEARING,//=0，下面都是使用默认的动画
	                (mChangeAppear.isChecked() ? mTransition.getAnimator(LayoutTransition.CHANGE_APPEARING) : null));
	        //当一个View在ViewGroup中【消失】时，对此【View】设置的动画
	        mTransition.setAnimator(LayoutTransition.DISAPPEARING, //=3
	                (mDisAppear.isChecked() ? mTransition.getAnimator(LayoutTransition.DISAPPEARING) : null));
	        //当一个View在ViewGroup中【消失】时，对此View对其他View位置造成影响，对【其他View】设置的动画
	        mTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,//=1
	                (mChangeDisAppear.isChecked() ? mTransition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING) : null));
	        gl_container.setLayoutTransition(mTransition);
	    }
	    // 向GridLayout中添加按钮
	    public void addBtn(View view) {
	        final Button button = new Button(this);
	        button.setText("" + (++mVal));
	        gl_container.addView(button, gl_container.getChildCount());//放置在最后那个位置
	        button.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                gl_container.removeView(button);
	            }
	        });
	    }
	    //XML属性动画1，单个动画
	    public void xmlAnim1(View view) {
	        // 加载动画
	        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.scalex);
	        mAppear.setPivotX(0);//设置缩放、反转时的中心点或者轴
	        mAppear.invalidate();//设置后要显示的调用invalidate才能生效
	        anim.setTarget(mAppear);
	        anim.start();
	    }
	    //XML属性动画2，多动画
	    public void xmlAnim2(View view) {
	        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.scale);
	        mChangeAppear.setPivotX(0);//设置缩放、反转时的中心点或者轴
	        mChangeAppear.setPivotY(0);
	        mChangeAppear.invalidate();//设置后要显示的调用invalidate
	        anim.setTarget(mChangeAppear);
	        anim.start();
	    }
	    //Keyframe动画
	    public void keyframe(View view) {
	        Keyframe kf0 = Keyframe.ofInt(0, 300);
	        Keyframe kf1 = Keyframe.ofInt(0.2f, 800);//动画开始1/5时 Width=600
	        Keyframe kf2 = Keyframe.ofInt(0.4f, 300);
	        Keyframe kf3 = Keyframe.ofInt(0.6f, 100);
	        Keyframe kf4 = Keyframe.ofInt(0.8f, 50);
	        Keyframe kf5 = Keyframe.ofInt(1f, 500);
	        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe("width", kf0, kf1, kf2, kf3, kf4, kf5);
	        ObjectAnimator rotationAnim = ObjectAnimator.ofPropertyValuesHolder(mDisAppear, pvhRotation);
	        rotationAnim.setDuration(5000).start();
	    }
	    //新版本View动画
	    public void viewAnimForNew(View view) {
	        mChangeDisAppear.animate().alpha(0.2F).y(1000).setDuration(2000).withStartAction(new Runnable() {
	            @Override
	            public void run() {
	                mChangeDisAppear.setX(200);
	                mChangeDisAppear.setRotation(180);
	            }
	        }).withEndAction(new Runnable() {
	            @Override
	            public void run() {
	                mChangeDisAppear.setX(200);
	                mChangeDisAppear.setY(300);
	                mChangeDisAppear.setAlpha(1.0f);
	                mChangeDisAppear.setRotation(-30);//是相对初始值转了-30°
	            }
	        }).start();
	    }
	}