# Intent练习
## 实验目的
了解并掌握显式Intent和隐式Intent

## 实验内容
### 显式Intent
修改FirstActivity中按钮的点击事件
...

	button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });  

点击FirstActivity的按钮进入SecondActivity活动  
<img src="/thirdHomework/img/firstActivity.jpg" width="50%" height="50%">  
<img src="/thirdHomework/img/secondActivity.jpg" width="50%" height="50%">  

### 隐式Intent
修改FirstActivity中按钮点击事件的代码
...
	
	button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent (Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });

在FirstActivity界面点击按钮就可以看到打开了系统浏览器  
<img src="/thirdHomework/img/baidu.jpg" width="50%" height="50%"> 

在ThirdActivity的<intent-filter>中配置了当前活动能够响应的action是Intent.ACTION_VIEW的常量值，而category则毫无疑问指定了默认的category值，另外在<data>标签中我们通过android:scheme指定了数据的协议必须是http协议。运行程序如下：  
<img src="/thirdHomework/img/brower.jpg" width="50%" height="50%">   

下面的代码展示了如何在我们的程序中调用系统拨号界面：  
...
	
	button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent (Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });

运行程序如下：  
<img src="/thirdHomework/img/firstActivity.jpg" width="50%" height="50%">  
我们还也可以在Intent启动活动的时候传递数据，将想要传递的数据暂存在Intent中，启动了另一个活动后，只需要把这些数
据再从Intent中取出,从而实现向下一个活动传递数据

<img src="/thirdHomework/img/HelloSecond.jpg" width="50%" height="50%">   
当然我们也可以将数据返回给上一个活动  
<img src="/thirdHomework/img/HelloFirst.jpg" width="50%" height="50%">   
