# CountTimer

适用 Android 开发，用于发送验证码、开屏广告等场景的计时或计数。

## 1. 特性

* 支持自定义总的计数和计数间隔时间。
* 支持由大到小和由小到大两种计数方式。
* 支持计数中途暂停与恢复以及停止。
* 支持计数状态的监听，含三种状态：CountTimer.STATE_STOP, CountTimer.STATE_PAUSE, CountTimer.STATE_GOING
* 支持计数监听。
* 支持自定义根据计数更新 View 的状态等。

## 2. 文件列表

| 文件           | 功能描述                                                     |
| -------------- | ------------------------------------------------------------ |
| CountTimer     | 基础计数类，可监听状态与计数变化。                           |
| ViewCountTimer | 与 View 关联，可根据计数的变化来更新 View，需设置 ViewBinder. |
| TextCountTimer | 适用 TextView 及其子类，如 Button 等，计数开始自动禁用 View 并设置计数，计数停止自动恢复 View 的原有状态。 |



## 3. 用法举例

```java
        Button countDown = findViewById(R.id.btn_count_down);
        CountTimer timer = new TextCountTimer<>(countDown);
        timer.start();
```

## 4. 使用方法

(1) 在项目的 build.gradle 中配置仓库地址：

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

(2) 添加项目依赖：

```groovy
	dependencies {
	        implementation 'com.github.ccolorcat:CountTimer:v1.0.0'
	}
```

## 5. 版本历史

v1.0.0

> 首次发布