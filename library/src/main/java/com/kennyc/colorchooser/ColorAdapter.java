package com.kennyc.colorchooser;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by kcampagna on 2/8/16.
 */
class ColorAdapter extends BaseAdapter {
    private int[] colors;

    private LayoutInflater mInflater;

    private final int size;

    private int borderSize;

    private boolean hasBorder = false;

    private boolean animateChange = true;

    private int selectedPosition = AdapterView.INVALID_POSITION;

    public ColorAdapter(Context context, int[] colors, boolean hasBorder, boolean animateChange) {
        Resources res = context.getResources();
        mInflater = LayoutInflater.from(context);
        this.colors = colors;
        size = res.getDimensionPixelSize(R.dimen.color_chooser_view_size);
        this.hasBorder = hasBorder;
        this.animateChange = animateChange;
        if (hasBorder) borderSize = res.getDimensionPixelSize(R.dimen.color_chooser_border_width);
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Returns the selected color. If no color is selected, {@link AdapterView#INVALID_POSITION} will be return
     *
     * @return
     */
    public int getSelectedColor() {
        if (selectedPosition < 0) return AdapterView.INVALID_POSITION;
        return colors[selectedPosition];
    }

    /**
     * Sets the adapter position to be selected
     *
     * @param position
     */
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    /**
     * Sets which color will be selected.
     *
     * @param color
     */
    public void setSelectedColor(int color) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == color) {
                selectedPosition = i;
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.color_chooser_item, parent, false);
            vh = new ViewHolder(convertView);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        boolean isSelected = position == selectedPosition;
        OvalColorDrawable dr = new OvalColorDrawable(size, borderSize, colors[position]);

        if (isSelected) {
            dr.drawBorder(hasBorder);
            vh.check.setVisibility(View.VISIBLE);
            animateChange(convertView);
        } else {
            dr.drawBorder(false);
            vh.check.setVisibility(View.GONE);
        }

        vh.color.setImageDrawable(dr);
        return convertView;
    }

    private void animateChange(final View view) {
        if (!animateChange) return;

        ScaleAnimation anim = new ScaleAnimation(1.0f, .5f, 1.0f, .5f, size / 2, size / 2);
        anim.setDuration(350);
        view.setAnimation(anim);
        anim.start();

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ScaleAnimation anim = new ScaleAnimation(.5f, 1.0f, .5f, 1.0f, size / 2, size / 2);
                view.setAnimation(anim);
                anim.setDuration(350);
                anim.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    static class ViewHolder {
        ImageView color;

        ImageView check;

        public ViewHolder(View view) {
            color = (ImageView) view.findViewById(R.id.color);
            check = (ImageView) view.findViewById(R.id.check);
            view.setTag(this);
        }
    }
}
