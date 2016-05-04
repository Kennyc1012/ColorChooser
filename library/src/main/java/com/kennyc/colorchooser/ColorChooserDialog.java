package com.kennyc.colorchooser;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by kcampagna on 2/8/16.
 */
public class ColorChooserDialog extends BaseDialog implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String KEY_BUILDER = "ColorChooserDialog#Builder";

    private GridView grid;

    private Button positiveBtn;

    private ColorListener listener;

    private static ColorChooserDialog newInstance(Builder builder) {
        ColorChooserDialog dialog = new ColorChooserDialog();
        Bundle args = new Bundle(1);
        args.putParcelable(KEY_BUILDER, builder);
        dialog.setArguments(args);
        dialog.listener = builder.listener;
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.color_chooser_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        if (args == null || args.getParcelable(KEY_BUILDER) == null) {
            throw new IllegalStateException("Did not receive the needed parameters to create dialog");
        }

        Builder b = args.getParcelable(KEY_BUILDER);
        TextView title = (TextView) view.findViewById(R.id.color_chooser_title);
        Button negativeBtn = (Button) view.findViewById(R.id.color_chooser_neg);
        negativeBtn.setOnClickListener(this);
        positiveBtn = (Button) view.findViewById(R.id.color_chooser_pos);
        positiveBtn.setOnClickListener(this);
        grid = (GridView) view.findViewById(R.id.color_chooser_grid);
        grid.setNumColumns(b.numColumns);
        grid.setOnItemClickListener(this);
        ColorAdapter adapter = new ColorAdapter(getActivity(), b.colors, b.showSelectedBorder);
        if (b.selectedColor != Color.TRANSPARENT) adapter.setSelectedColor(b.selectedColor);
        grid.setAdapter(adapter);

        if (!TextUtils.isEmpty(b.title)) {
            title.setText(b.title);
        } else {
            title.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(b.negativeButton)) {
            negativeBtn.setText(b.negativeButton);
            if (b.negativeBtnColor != Color.TRANSPARENT) negativeBtn.setTextColor(b.negativeBtnColor);
        } else {
            negativeBtn.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(b.positiveButton)) {
            positiveBtn.setText(b.positiveButton);
            if (b.positiveBtnColor != Color.TRANSPARENT) positiveBtn.setTextColor(b.positiveBtnColor);
        } else {
            positiveBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        listener = null;
        grid = null;
        positiveBtn = null;
        super.onDestroyView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ColorAdapter adapter = (ColorAdapter) parent.getAdapter();
        adapter.setSelectedPosition(position);
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            dismiss();
            return;
        }

        if (v == positiveBtn) {
            int color = ((ColorAdapter) grid.getAdapter()).getSelectedColor();
            listener.onColorSelect(color);
        }

        dismiss();
    }

    public interface ColorListener {
        /**
         * Callback when a color is selected from the dialog
         *
         * @param color
         */
        void onColorSelect(int color);
    }

    public static class Builder implements Parcelable {

        private Resources resources;

        private int[] colors;

        private String title;

        private String negativeButton;

        private String positiveButton;

        private int selectedColor = Color.TRANSPARENT;

        private int negativeBtnColor = Color.TRANSPARENT;

        private int positiveBtnColor = Color.TRANSPARENT;

        private int numColumns;

        private boolean showSelectedBorder = true;

        private ColorListener listener;

        /**
         * Create a builder factory for a {@link ColorChooserDialog}
         *
         * @param context
         */
        public Builder(Context context) {
            this.resources = context.getResources();
            numColumns = resources.getInteger(R.integer.color_chooser_column_count);
        }

        private Builder(Parcel in) {
            title = in.readString();
            negativeButton = in.readString();
            positiveButton = in.readString();
            colors = in.createIntArray();
            showSelectedBorder = in.readInt() == 1;
            selectedColor = in.readInt();
            positiveBtnColor = in.readInt();
            negativeBtnColor = in.readInt();
            numColumns = in.readInt();
        }

        /**
         * Sets the colors to be used in the picker
         *
         * @param colorArrayResource A string array from resources. The colors should be defined as such:<p/>
         *                           &lt;string-array&gt;<br/>
         *                           &lt;item&gt;#ffffff&lt;/item&gt;<br/>
         *                           &lt;item&gt;#000000&lt;/item&gt;<br/>
         *                           ...<br/>
         *                           ...<br/>
         *                           &lt;/string-array&gt;
         * @return
         */
        public Builder colors(int colorArrayResource) {
            String[] array = resources.getStringArray(colorArrayResource);
            int length = array.length;
            int[] parsedColors = new int[length];

            for (int i = 0; i < length; i++) {
                parsedColors[i] = Color.parseColor(array[i]);
            }

            return colors(parsedColors);
        }

        /**
         * Sets the colors to be used in the picker
         *
         * @param colors An array of color ints
         * @return
         */
        public Builder colors(int[] colors) {
            if (colors == null || colors.length <= 0) {
                throw new IllegalArgumentException("colors == null or <= 0");
            }

            this.colors = colors;
            return this;
        }

        /**
         * Sets which color should be pre-selected when first showing
         *
         * @param color
         * @return
         */
        public Builder selectedColor(int color) {
            this.selectedColor = color;
            return this;
        }

        public Builder selectedColor(String color) {
            if (TextUtils.isEmpty(color)) throw new IllegalArgumentException("No color supplied");
            return selectedColor(Color.parseColor(color));
        }

        /**
         * Sets the title of the dialog
         *
         * @param title The string resource to use for the title
         * @return
         */
        public Builder title(int title) {
            return title(resources.getString(title));
        }

        /**
         * Sets the title of the dialog
         *
         * @param title Title to be used for the dialog
         * @return
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Whether or not the circle icons will have a border around them when selected
         *
         * @param border
         * @return
         */
        public Builder hasBorder(boolean border) {
            showSelectedBorder = border;
            return this;
        }

        /**
         * Sets the text for the negative Button
         *
         * @param negativeButton The string resource to use for the negative button
         * @return
         */
        public Builder negativeButton(int negativeButton) {
            return negativeButton(resources.getString(negativeButton));
        }

        /**
         * Sets the text for the negative Button
         *
         * @param negativeButton The string to use for the negative button
         * @return
         */
        public Builder negativeButton(String negativeButton) {
            this.negativeButton = negativeButton;
            return this;
        }

        /**
         * Sets the text for the positive Button
         *
         * @param positiveButton The string resource to use for the negative button
         * @return
         */
        public Builder positiveButton(int positiveButton) {
            return positiveButton(resources.getString(positiveButton));
        }

        /**
         * Sets the text for the positive Button
         *
         * @param positiveButton The string to use for the negative button
         * @return
         */
        public Builder positiveButton(String positiveButton) {
            this.positiveButton = positiveButton;
            return this;
        }

        /**
         * Sets the {@link ColorListener} for the dialog
         *
         * @param listener Optional {@link ColorListener} to receive callbacks
         * @return
         */
        public Builder listener(ColorListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * Sets the positive button color for the dialog
         *
         * @param color Color to set the positive button to
         * @return
         */
        public Builder positiveButtonColor(int color) {
            this.positiveBtnColor = color;
            return this;
        }

        /**
         * Sets the positive button color resource for the dialog
         *
         * @param colorResource Color resource to set the positive button to
         * @return
         */
        public Builder positiveButtonColorRes(int colorResource) {
            return positiveButton(resources.getColor(colorResource));
        }

        /**
         * Sets the negative button color resource for the dialog
         *
         * @param color Color resource to set the negative button to
         * @return
         */
        public Builder negativeButtonColor(int color) {
            this.negativeBtnColor = color;
            return this;
        }

        /**
         * Sets the negative button color resource for the dialog
         *
         * @param colorResource Color resource to set the negative button to
         * @return
         */
        public Builder negativeButtonColorRes(int colorResource) {
            return negativeButtonColor(resources.getColor(colorResource));
        }

        public Builder columns(int columns) {
            numColumns = columns <= 0 ? 3 : columns;
            return this;
        }

        public Builder columnsResource(int columnsResource) {
            return columns(resources.getInteger(columnsResource));
        }

        public ColorChooserDialog build() {
            return newInstance(this);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(negativeButton);
            dest.writeString(positiveButton);
            dest.writeIntArray(colors);
            dest.writeInt(showSelectedBorder ? 1 : 0);
            dest.writeInt(selectedColor);
            dest.writeInt(positiveBtnColor);
            dest.writeInt(negativeBtnColor);
            dest.writeInt(numColumns);
        }

        public static final Parcelable.Creator<Builder> CREATOR = new Parcelable.Creator<Builder>() {
            public Builder createFromParcel(Parcel in) {
                return new Builder(in);
            }

            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };
    }
}
