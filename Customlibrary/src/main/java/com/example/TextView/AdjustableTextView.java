package com.example.TextView;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: Tong_Pao
 * 包名: com.example.Custom
 * 创建时间: 2020/11/27 20:43
 */
public class AdjustableTextView {
    private String string;
    //控件id
    private TextView textView;
    //设置要显示的行数
    private int length;
    private String hiddenString;
    private String displayString;

    public AdjustableTextView(TextView textView, int length) {
        this.textView = textView;
        this.length = length;
    }

    public void hiddenText() {

        string = textView.getText().toString();

        //设置textView可点击
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        //注册一个需要隐藏文本的Text的观察者来监听视图树，防止报错
        ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();

   /*interface ViewTreeObserver.OnGlobalLayoutListener
   当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时，所要调用的回调函数的接口类  */
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //注册一个需要隐藏文本的Text的观察者来监听视图树
                ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();

                //移除监听，防止后续继续监听
                viewTreeObserver.removeOnGlobalLayoutListener(this);

                //如果文本的行数大于要求的行数
                if (textView.getLineCount() > length) {
                    //得到文本前三行最后一个字符的位置
                    int lineEnd = textView.getLayout().getLineEnd(length - 1);

                    //拼接需要显示的字符串
                    hiddenString = string.substring(0, lineEnd - 5) + "...展开";
                    textView.setText(getClickableSpan(lineEnd));
                }
            }
        });
    }

    private SpannableStringBuilder getClickableSpan(int count) {

        class Listener extends ClickableSpan implements View.OnClickListener {
            //文字点击事件
            @Override
            public void onClick(@NonNull View widget) {
                textView.setText(string);
                //调用显示文本的拼接方法
                displayText();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }

        SpannableStringBuilder textSpannedBuilder = new SpannableStringBuilder(hiddenString);
        int left = 0;
        int right = 0;
        while (hiddenString.indexOf("#",right+1) != -1){
            left = hiddenString.indexOf("#",right)+1;
            right = hiddenString.indexOf("#",left)+1;
            textSpannedBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), left-1, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textSpannedBuilder.setSpan(new Listener(), left-1, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //设置前景色为红色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ff0000"));
        //设置字体大小
        textSpannedBuilder.setSpan(new RelativeSizeSpan(1.1f), count - 5, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体可点击
        textSpannedBuilder.setSpan(new Listener(), count - 5, count, Spanned.SPAN_MARK_MARK);
        //添加前景色为红色
        textSpannedBuilder.setSpan(foregroundColorSpan, count - 5, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return textSpannedBuilder;
    }

    public void displayText() {
        string = textView.getText().toString();
        //设置textView可点击
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        //注册一个需要隐藏文本的Text的观察者来监听视图树，防止报错
        ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
    /*interface ViewTreeObserver.OnGlobalLayoutListener
   当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时，所要调用的回调函数的接口类  */
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //注册一个需要隐藏文本的Text的观察者来监听视图树，防止报错
                ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();

                //移除监听，防止后续继续监听
                viewTreeObserver.removeOnGlobalLayoutListener(this);

                //得到最后一个字符串的位置
                int lineEnd2 = textView.getLayout().getLineEnd(textView.getLineCount() - 1);

                //在原字符串后面加上“隐藏”
                displayString = string.substring(0, lineEnd2) + "隐藏";
                textView.setText(getClickableSpan2(lineEnd2));
            }
        });
    }

    private SpannableStringBuilder getClickableSpan2(int count) {

        //指定部分监听
        class Listener extends ClickableSpan implements View.OnClickListener {

            //点击事件
            @Override
            public void onClick(@NonNull View widget) {

                textView.setText(string);
                //调用隐藏文本的方法
                hiddenText();
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }

        //新建一个spannableString,把拼接好的字符串传进去（设置字体颜色，监听点击，字体大小）
        SpannableStringBuilder textSpannedBuilder = new SpannableStringBuilder(displayString);
        int left = 0;
        int right = 0;
        while (displayString.indexOf("#",right+1) != -1){
            left = displayString.indexOf("#",right)+1;
            right = displayString.indexOf("#",left)+1;
            textSpannedBuilder.setSpan(new ForegroundColorSpan(Color.BLUE), left-1, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textSpannedBuilder.setSpan(new Listener(), left-1, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //设置前景色为红色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ff0000"));
        textSpannedBuilder.setSpan(new RelativeSizeSpan(1.1f), count, count + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textSpannedBuilder.setSpan(new Listener(), count, count + 2, Spanned.SPAN_MARK_MARK);

        //添加前景色为红色
        textSpannedBuilder.setSpan(foregroundColorSpan, count, count + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //返回 spannableString
        return textSpannedBuilder;

    }

}