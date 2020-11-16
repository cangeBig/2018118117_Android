# 广播机制
## 实验目的
了解广播的注册方式和自定义广播
## 实验内容
### 注册方式
注册广播的方式一般有两种，在代码中注册和在AndroidManifest.xml中注册，其中前者也称为动态注册，后者也称为静态注册。
#### 动态注册
广播接收器--新建一个类，让它继承BroadReceive，并重写父类的onReceive()方法，
动态注册的广播接收器一定要取消注册，在onDestroy()方法中通过调用unregisterReceive()方法来实现
...

	@Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isAvailable()){

                Toast.makeText(context,"network is available",
                        Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"network is unavailable",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
运行程序，然后按下Home键-Settings-Data usage,进入到数据使用详情界面，关闭Cellular data 会弹出无网络可用的提示，然后重新打开Cellular data 又会弹出网络可用的提示。  
<img src="/SixthHomework/img/netAvailable.jpg" width="50%" height="50%">  
<img src="/SixthHomework/img/netNotavailable.jpg" width="50%" height="50%">  
#### 静态注册
静态的广播接收器一定要在AndroidManifest.xml文件中完成，我们在<intent-filter>标签里添加了相应的action。另外，监听系统开机广播也是需要声明权限的。
...

	<receiver
        android:name=".BootCompleteReceiver"
        android:enabled="true"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.BOOT_COMPLETED" />
        </intent-filter>
     </receiver>


### 发送自定义广播
#### 标准广播
是一种完全异步执行的广播，在广播发出之后，所有的广播接收器几乎都会在同一时刻接收到这条广播消息，因此他们之间没有任何先后顺序可言。
在AndroidManifest.xml对这个广播接收器进行修改：
...

	   <receiver
            android:name=".AnotherBroadcastReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="com.example.intenttest.MY_BROADCAST"></action>
            </intent-filter>
        </receiver>

运行程序，点击按钮如下：    
<img src="/SixthHomework/img/MyBroadCast.jpg" width="50%" height="50%">   
新建一个项目，并用静态注册一个广播接收器，接收的同样是上面那条广播。运行这个项目，再返回到BroadcastTest项目的主界面，点击按钮，就会分别弹出两次提示消息  
<img src="/SixthHomework/img/MyBroadCast.jpg" width="50%" height="50%">  
<img src="/SixthHomework/img/AnotherBroadCast.jpg" width="50%" height="50%">   
#### 有序广播
有序广播是一种同步执行的广播，在广播发出后，同一时刻只会有一个广播接收器能收到这条广播消息
发送有序广播只需改动一行代码，将sendBroadcast()方法改成sebdOrderBroadcast()方法，再将设置广播接收器的先后顺序
...
	 
	 <receiver
            android:name=".AnotherBroadcastReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter android:priority="100">
                <action android:name="com.example.intenttest.MY_BROADCAST"/>
            </intent-filter>
        </receiver>
在BroadCastTest中的按钮监听器改一下：
...

	sendOrderedBroadcast(intent,null);

运行程序，点击一下Send Broadcast按钮，发现只有MyBroadcastReceiver中的Toast信息能够弹出，说明这条广播经过MyBroadcastReceiver之后确实是终止传递了。  
<img src="/SixthHomework/img/MyBroadCast.jpg" width="50%" height="50%"> 

#### 本地广播
为了能够简单地解决广播的安全性问题，Android引入了一套本地广播机制，使用这个机制发出的广播只能够在应用程序的内部进行传递。  
...

	 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                 new Intent("com.example.intenttest.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.intenttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }
    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"received local broadcast",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }
运行程序，可以看到LocalReceiver成功接收到了这条本地广播，并通过Toast提示了出来。  
<img src="/SixthHomework/img/localBroadCast.jpg" width="50%" height="50%"> 

	
      

