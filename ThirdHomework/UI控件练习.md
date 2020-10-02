# android的UI练习
## 实验目的
练习和掌握android的几种常用的控件：TextView、Button、EditText、ImageView、AlertDialog，了解和熟悉几种布局。

## 实验内容
![](/ThirdHomework/img/main.jpg)

### 布局情况： 
整体采用垂直布局   
上面是一个TextView,居中对齐，内容为“This is my TextView”，  
接下来是一个水平布局，里面有一个EditText和一个Button，  
然后是一个imageView，
最后一个用来切换图片的Button

### EditText与Button结合
使用EditText与Button来完成一些功能，比如通过点击按钮来获取EditText中输入的内容。
![](/ThirdHomework/img/editText.jpg)  
在按钮的点击事件里面调用EditText的getText()方法获取到输入的内容，再调用toString()方法转换成字符串，最后还是老方法使用Toast将输入的内容显示出来。  
如上图：EditText里面输入“女神”，通过按钮用Toast把内容显示出来。  

### imageView使用
imageView是用于在界面上展示图片的一个控件，它可以让我们的程序界面变得更加丰富多彩。我们在res目录下新建一个drawable-xhdpi目录，然后将事先准备好的两张图片one.jpg和two.jpg复制到该目录当中。
如下，当前imageView中放着one.jpg
![](/ThirdHomework/img/main.jpg)  

在按钮的点击事件里，通过调用ImageView的setImageResource()方法将显示的图片改变为two.jpg，再点一下变成one.jpg,可以反复转换
![](/ThirdHomework/img/change.jpg)

### AlertDialog使用
AlertDialog可以在当前的界面弹出一个对话框，这个对话框是置顶与所有界面元素之上的，能够屏蔽其他控件的交互能力，因此AlertDialog一般都是用于提示一些非常重要的内容或者警告信息。  
运行程序，当要退出程序时
![](/ThirdHomework/img/AlertDialog.jpg)
点是时，即退出程序
![](/ThirdHomework/img/exit.jpg)
