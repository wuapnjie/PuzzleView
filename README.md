# PuzzleView
Android Jigsaw puzzle support，inspired by Instagram's layout.

Release to 2.0, now support slant mode and round cornors.

[中文README](https://github.com/wuapnjie/PuzzleView/blob/master/README_CN.md)

the apk file is here--->[http://fir.im/puzzle0607](http://fir.im/puzzle0607)

### Screenshots
Due the size of the gif file, the example below may not render well, in that case you can view the demo video on youtube.

[Click here to see video demo](https://www.youtube.com/watch?v=jfOJCh-uDIo)
</br>
</br>
![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/screenshot1.png)
![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/screenshot2.png)

**Now support slant mode and round cornors**
</br>
![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/screenshot4.png)

### Usage
in build.gradle
```gradle
compile 'com.flying.xiaopo:puzzle:2.0.0-alpha'
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

in the java code, you can change some attributes of the puzzle view
```java
puzzleView.setPuzzleLayout(puzzleLayout);
puzzleView.setTouchEnable(true);
puzzleView.setNeedDrawLine(false);
puzzleView.setNeedDrawOuterLine(false);
puzzleView.setLineSize(4);
puzzleView.setLineColor(Color.BLACK);
puzzleView.setSelectedLineColor(Color.BLACK);
puzzleView.setHandleBarColor(Color.BLACK);
puzzleView.setAnimateDuration(300);
```
and handle some actions
```java
puzzleView.rotate(90f);
puzzleView.flipHorizontally();
puzzleView.flipVertically();
puzzleView.setLineSize(currentDegrees);
puzzleView.setPieceRadian(currentDegrees);
puzzleView.setNeedDrawBorder(!mPuzzleView.isNeedDrawBorder());
puzzleView.replace(bitmap);
```

Also you can check the source code of the demo for examples of how to use these attributes.

### Layout
The `PuzzleView` depends on the `PuzzleLayout`. Of course you can customize it.
</br>
If you don't want to customize it's layout, you can just use the default layout.
</br>
All the built-in layouts can be found in the the demo's `PlaygroundActivity`. You can click to see the effect.
![](https://github.com/wuapnjie/PuzzleView/blob/master/screenshots/screenshot3.png)

### Custom Layout
To create a custom layout is also easy. Just extend `PuzzleLayout` and override the `layout` method.
</br>
The `PuzzleLayout` class provides some methods for customization.
Also you can check the source code of the in-built layouts for guidance / examples.
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
to see some photos by puzzle
[Click here](http://weibo.com/5350471787/E54jjxzlI)

### Update
* **2017/08/29**  2.0.0-alpha add slant mode puzzle, and to support round cornors
* **2017/04/15**  1.1.1 add piece select listener
* **2017/02/15**  1.1.0 add piece padding function 

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


