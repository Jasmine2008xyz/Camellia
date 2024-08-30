package com.luoyu.camellia.ui.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.Button;
@Deprecated
public class EasyButton {
    private static final String TAG = "EasyButton";
    private Context act;

    private Button button;

    /*
     * android.widget.Button extends from android.widget.TextView
     */

    /*
     * An easy class implements simpler and more exquisite buttons.
     */

    /*
     * Two ways to create
     * {@link #EasyButton(Context)}
     * {@link #EasyButton create(Context)}
     */

    public EasyButton(Context context) {
        button = new Button(context);
        setAct(context);
        this.button.setClickable(true);
        GradientDrawable background = new GradientDrawable();
       // background.setPadding(25, 25, 25, 25);
        background.setCornerRadius(25);
        background.setColor(Color.parseColor("#0099FF"));
        background.setShape(GradientDrawable.RING);

        button.setBackground(background);
    }

    public EasyButton(Button button) {
        this.button = button;
        setAct(button.getContext());
        this.button.setClickable(true);
        GradientDrawable background = new GradientDrawable();
       // background.setPadding(25, 25, 25, 25);
        background.setCornerRadius(25);
        background.setColor(Color.parseColor("#0099FF"));
        background.setShape(GradientDrawable.RING);
        button.setBackground(background);
    }

    public static EasyButton create(Context act) {
        return new EasyButton(act);
    }

    public static EasyButton create(Button button) {
        return new EasyButton(button);
    }

    public EasyButton setTextColor(int color) {
        button.setTextColor(color);
        return this;
    }

    /*
     * setter/getter for {@link #Context act}
     */

    private Context getAct() {
        return this.act;
    }

    private void setAct(Context act) {
        this.act = act;
    }

    public Button getButton() {
        return this.button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
