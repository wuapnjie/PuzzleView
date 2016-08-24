# PuzzleView
Android Jigsaw puzzle support，idea from layout for instagram

[中文README](https://github.com/wuapnjie/PuzzleView/blob/master/README_CN.md)

the apk file is here--->[http://fir.im/puzzle0607](http://fir.im/puzzle0607)

### Screenshots
Due to the gif is too big, if it can not load well, you can see the demo in youtube.

[Click here to see video demo](https://www.youtube.com/watch?v=jfOJCh-uDIo)
</br>
</br>
![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/gif-demo1.gif)
![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/gif-demo2.gif)
![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/screen1.png)

### Usage
in build.gradle
```gradle
compile 'com.flying.xiaopo:puzzle:1.0.0'
```

in xml layout
```xml
 <com.xiaopo.flying.puzzle.PuzzleView
        android:id="@+id/puzzle"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
        
 <com.xiaopo.flying.puzzle.SquarePuzzleView
        android:id="@+id/square_puzzle"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

in the java code, you can change some attributes of puzzle view
```java
mPuzzleView.setPuzzleLayout(mPuzzleLayout);
mPuzzleView.setMoveLineEnable(true);
mPuzzleView.setNeedDrawBorder(false);
mPuzzleView.setNeedDrawOuterBorder(false);
mPuzzleView.setExtraSize(100);
mPuzzleView.setBorderWidth(4);
mPuzzleView.setBorderColor(Color.WHITE);
mPuzzleView.setSelectedBorderColor(Color.parseColor("#99BBFB"));
```
and handle some actions
```java
mPuzzleView.rotate(90f);
mPuzzleView.flipHorizontally();
mPuzzleView.flipVertically();
mPuzzleView.setNeedDrawBorder(!mPuzzleView.isNeedDrawBorder());
mPuzzleView.replace(bitmap);
```

Also you can see the source code of demo, it will tell you how to do.

### Layout
The PuzzleView depends on the PuzzleLayout. Of course you can custom it.
</br>
if you don't like to custom its layout, you can use the build-in layout.
</br>
in this demo's PlaygroundActivity, it shows all the build-in layouts. You can click to see the effect.
![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/screen2.png)

### Custom Layout
The custom layout also is easy. Just extends PuzzleLayout and write layout() method.
</br>
In the PuzlleLayout.class, it provides some methods to help you custom.Also you can see the source code in build-in layout to study.
```java
public class CustomLayout extends PuzzleLayout {

    @Override
    public void layout() {
        addLine(getOuterBorder(), Line.Direction.HORIZONTAL, 1f / 2);
        addLine(getBorder(0), Line.Direction.VERTICAL, 1f / 2);
    }
}
```

![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/puzzle.png)

### Finally
see some photo by puzzle
[Click here](http://weibo.com/5350471787/E54jjxzlI)

### License

    Copyright 2016 wuapnjie

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
