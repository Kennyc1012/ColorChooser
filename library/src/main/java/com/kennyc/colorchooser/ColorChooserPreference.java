package com.kennyc.colorchooser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class ColorChooserPreference extends DialogPreference implements AbsListView.OnItemClickListener {

    private GridView grid;

    private ImageView image;

    // The colors to be displayed.
    private int[] colors;

    // The optional names of the colors, if supplied, the length MUST be the same as the colors array and the indexes should correspond
    private CharSequence[] colorNames;

    // The currently selected color. A color of TRANSPARENT indicates nothing has been selected
    private int selectedColor = Color.TRANSPARENT;

    // The selected color name. May be null if no names are supplied
    private CharSequence selectedColorName = null;

    private int numColumns = 3;

    private boolean animateChanges = true;

    public ColorChooserPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorChooserPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, android.R.attr.dialogPreferenceStyle);
        setDialogLayoutResource(R.layout.color_chooser_layout);
        setWidgetLayoutResource(R.layout.color_choose_pref_widget);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorChooser);
        CharSequence[] unParsedColors = a.getTextArray(R.styleable.ColorChooser_cc_colors);
        colorNames = a.getTextArray(R.styleable.ColorChooser_cc_colorNames);
        selectedColorName = a.getString(R.styleable.ColorChooser_cc_defaultColorName);
        numColumns = a.getInt(R.styleable.ColorChooser_cc_columnCount, getContext().getResources().getInteger(R.integer.color_chooser_column_count));
        animateChanges = a.getBoolean(R.styleable.ColorChooser_cc_animateChanges, true);
        if (numColumns <= 0) numColumns = 3;
        setSummary(selectedColorName);

        if (unParsedColors != null && unParsedColors.length > 0) {
            int length = unParsedColors.length;
            colors = new int[length];

            for (int i = 0; i < length; i++) {
                int parsedColor = Color.parseColor(unParsedColors[i].toString());
                colors[i] = parsedColor;
            }
        }

        a.recycle();
    }

    @Override
    protected void onBindView(View view) {
        image = (ImageView) view.findViewById(R.id.image);
        selectedColor = getPersistedInt(Color.TRANSPARENT);

        // Set the preference layout summary and color if we have one
        if (colorNames != null && selectedColor != Color.TRANSPARENT) {
            int i = 0;

            for (int color : colors) {
                // Make sure out selected color exists in the given array
                if (selectedColor == color) {
                    selectedColorName = colorNames[i];
                    updatePreferenceDrawable();
                    setSummary(selectedColorName);
                    view.findViewById(android.R.id.summary).setVisibility(View.VISIBLE);
                }

                i++;
            }
        }

        super.onBindView(view);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        grid = (GridView) view.findViewById(R.id.color_chooser_grid);
        grid.setNumColumns(numColumns);
        grid.setOnItemClickListener(this);
        ColorAdapter adapter = new ColorAdapter(getContext(), colors, true, animateChanges);
        grid.setAdapter(adapter);
        adapter.setSelectedColor(selectedColor);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ColorAdapter adapter = (ColorAdapter) parent.getAdapter();
        adapter.setSelectedPosition(position);
        selectedColor = colors[position];
        if (colorNames != null) selectedColorName = colorNames[position];
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (selectedColor != Color.TRANSPARENT) {
                    if (persistInt(selectedColor)) {
                        callChangeListener(selectedColor);
                    }
                }

                updatePreferenceDrawable();
                setSummary(selectedColorName);
                break;
        }
    }

    /**
     * Sets the selected color. This will update the preference layout with the new color and the summary
     *
     * @param color     The color that is selected
     * @param colorName Option color name, may be null
     */
    public void setSelectedColor(int color, String colorName) {
        selectedColor = color;
        selectedColorName = colorName;
        setSummary(selectedColorName);
        updatePreferenceDrawable();
    }

    /**
     * Sets the colors to be used for the Preference
     *
     * @param colors            An array of colors
     * @param selectedIndex     The index of which color is pre selected, pass any number < 0 to ignore
     * @param selectedColorName The selection color name, may be null
     */
    public void setColors(int colors[], int selectedIndex, String selectedColorName) {
        if (colors == null || colors.length <= 0)
            throw new IllegalArgumentException("No colors were supplied to preference");
        if (selectedIndex >= colors.length)
            throw new IndexOutOfBoundsException("Selected index > number of items in array");
        this.colors = colors;
        if (selectedIndex >= 0) selectedColor = colors[selectedIndex];
        this.selectedColorName = selectedColorName;
    }

    /**
     * Sets the colors to be used for the Preference
     *
     * @param colors An array of colors
     */
    public void setColors(int colors[]) {
        setColors(colors, -1, null);
    }

    /**
     * Updates the drawable in the preference layout to show the newly selected color
     */
    private void updatePreferenceDrawable() {
        if (image != null) {
            OvalColorDrawable dr = new OvalColorDrawable(getContext().getResources().getDimensionPixelSize(R.dimen.color_chooser_view_size_pref), 0, selectedColor);
            image.setImageDrawable(dr);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return Color.parseColor(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            selectedColor = getPersistedInt(Color.TRANSPARENT);
        } else {
            // Set default state from the XML attribute
            selectedColor = (Integer) defaultValue;
            persistInt(selectedColor);
        }
    }
}
