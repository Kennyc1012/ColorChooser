# ColorChooser
Simple color picker for Android


#Using ColorChooser
To use the ColorChooser dialog, build one through the Builder Class.
```java
int[] colors = new int[]{Color.RED, Color.YELLOW, Color.GREEN};

 new ColorChooserDialog.Builder(getActivity())
  .colors(colors)
  .listener(myListener)
  .positiveButton("Okay")
  .negativeButton("Cancel")
  .title("Select Color")
  .positiveButtonColor(Color.BLUE)
  .build()
  .show(getFragmentManager(), "tag");
```

You can also use a string array for colors. These colors <b>MUST</b> be formatted as a Hexadecimal color.
```xml
  <string-array name="colors">
    <item>#F44336</item>
    <item>#E91E63</item>
    <item>#9C27B0</item>
    <item>#673AB7</item>
    <item>#3F51B5</item>
    <item>#03A9F4</item>
    <item>#00BCD4</item>
    <item>#009688</item>
    <item>#4CAF50</item>
  </string-array>
```
```java
 new ColorChooserDialog.Builder(getActivity())
  .colors(R.array.colors)
  .listener(myListener)
  .positiveButton("Okay")
  .negativeButton("Cancel")
  .title("Select Color")
  .positiveButtonColor(Color.BLUE)
  .build()
  .show(getFragmentManager(), "tag");
```
![screenshot](https://github.com/Kennyc1012/ColorChooser/blob/master/screen_shots/ss1.png)

#Preferences
You can also use the dialog as a preference
```xml
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <com.kennyc.colorchooser.ColorChooserPreference
        android:key="color"
        android:title="Color"
        android:positiveButtonText="Okay"
        android:negativeButtonText="Cancel"
        app:cc_colors="@array/colors"
        app:cc_colorNames="@array/color_names"/>

</PreferenceScreen>
```
![screenshot](https://github.com/Kennyc1012/ColorChooser/blob/master/screen_shots/ss2.png)
The ```cc_colors``` attribute should be an array of colors in Hexadecimal format and ```cc_colorNames``` are optional names of the colors
that will display in the summary if supplied.


#Compatability
The library contains two different flavors depending on compatability with FragmentManger. If using the standard FragmentManager provided by the
Android SDK, use the stanard flavor. If using the SupportFragmentManager from the AppCompat library, use the compat flavor. 

#Including in your project
To include in your project, make the following changes to your build.gradle file

## Add repository 
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```
## Add dependency
```groovy
dependencies {
    // For standard flavor
    compile 'com.github.Kennyc1012:ColorChooser:0.2.1:standardRelease@aar'
    // For compat flavor
    compile 'com.github.Kennyc1012:ColorChooser:0.2.1:compatRelease@aar'
}
```

#Contribution
Pull requests are welcomed and encouraged. If you experience any bugs, please file an [issue](https://github.com/Kennyc1012/ColorChooser/issues)

License
=======

    Copyright 2016 Kenny Campagna

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

