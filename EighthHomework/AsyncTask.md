# 实验目的
异步任务的隐含子线程程序框架
## 实验内容
AsyncTask定义了三种泛型类型 Params，Progress和Result。

* Params 启动任务执行的输入参数，比如HTTP请求的URL。
* Progress 后台任务执行的百分比。
* Result 后台执行任务最终返回的结果，比如String。  

我们需要去重写AsyncTask中的几个方法才能完成对任务的定制。经常需要去重写的方法有以下4个。  
 
*  onPreExecute()        这里是最终用户调用Excute时的接口，当任务执行之前开始调用此方法，可以在这里显示进度对话框。
	
	    @Override
        protected void onPreExecute() {
            text.setText("load...");
            progressBar.setProgress(0);
        }

* doInBackground(Params…) 后台执行，比较耗时的操作都可以放在这里。注意这里不能直接操作UI。此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。在执行过程中可以调用publicProgress(Progress…)来更新任务的进度。
	
		@Override
        protected Boolean doInBackground(Void ... parms) {
            Log.i("MainActivity", "当前线程id: "+Thread.currentThread().getId());
            Log.i("MainActivity", "主线程id: "+getMainLooper().getThread().getId());
            try {
                int count = 0;
                int length = 1;
                while (count<100) {

                    count += length;
                    // 可调用publishProgress（）显示进度, 之后将执行onProgressUpdate（）
                    publishProgress(count);
                    // 模拟耗时任务
                    Thread.sleep(50);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

* onProgressUpdate(Progress…)   可以使用进度条增加用户体验度。 此方法在主线程执行，用于显示任务执行的进度。
	    
		@Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            text.setText("loading..."+values[0]+"%");
        }

* onPostExecute(Result)  相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。 此方法在主线程执行，任务执行的结果作为此方法的参数返回
	 
		@Override
        protected void onPostExecute(Boolean aBoolean) {    
            if(aBoolean){
				text.setText("Download success!");   
            }else{
				text.setText("Download fail!"); 
			}
        }

运行程序，如下：  
<img src="/EighthHomework/img/mainface.jpg" width="50%" height="50%">   
点击download按钮，如下：  
<img src="/EighthHomework/img/download.jpg" width="50%" height="50%">   
点击cancel按钮，下载取消：  
<img src="/EighthHomework/img/cancel.jpg" width="50%" height="50%">   
点击add按钮，最下面的textView发生变化，说明在下载的同时也能进行其他操作：  
<img src="/EighthHomework/img/add.jpg" width="50%" height="50%"> 
