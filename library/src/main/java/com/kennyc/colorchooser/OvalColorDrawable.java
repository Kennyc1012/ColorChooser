package com.kennyc.colorchooser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**
 * Created by kcampagna on 2/8/16.
 */
class OvalColorDrawable extends ShapeDrawable {
    private final int size;

    private final int color;

    private final int borderThickness;

    private boolean drawBorder = false;

    private Paint borderPaint;

    public OvalColorDrawable(int size, int borderThickness, int color) {
        super(new OvalShape());
        this.size = size;
        this.color = color;
        this.borderThickness = borderThickness;
        getPaint().setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (drawBorder) {
            RectF rect = new RectF(getBounds());
            rect.inset(borderThickness / 2, borderThickness / 2);
            canvas.drawOval(rect, borderPaint);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return size;
    }

    @Override
    public int getIntrinsicHeight() {
        return size;
    }

    public void drawBorder(boolean draw) {
        this.drawBorder = draw;

        if (draw) {
            if (borderPaint == null) {
                borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                borderPaint.setColor(darkenColor());
                borderPaint.setStyle(Paint.Style.STROKE);
                borderPaint.setStrokeWidth(borderThickness);
            }
        }

        invalidateSelf();
    }

    private int darkenColor() {
        return Color.rgb((int) (0.5f * Color.red(color)),
                (int) (0.5f * Color.green(color)),
                (int) (0.5f * Color.blue(color)));
    }
}
