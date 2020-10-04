# RecyclerView练习
## 实验目的
掌握RecyclerView的使用和点击事件


## 实验内容
我们定义了一个ImageView用于显示水果的图片，又定义了一个TextView用于显示水果的名称，存在fruit_item.xml

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    >
    
    <ImageView
        android:id="@+id/fruit_image"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
       />

    <TextView
        android:id="@+id/fruit_name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="left"
        android:layout_marginTop="10dp" />
	</LinearLayout>

我们在activity_recycle_view.xml中放了一个recycleView,并在它所属的活动RecycleViewActivity中的onCreate方法中启动瀑布流布局，把布局分为3列，把布局纵向排列。

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        FruitAdapter adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
    }


启动程序，结果如下：  
<img src="/FourHomework/img/firstPage.jpg" width="50%" height="50%">  
往下翻如下：  
<img src="/FourHomework/img/nextPage.jpg" width="50%" height="50%">  


## RecylerView点击事件
我们在FruitAdapter中给ImageView的fruitImage和TextView的fruitName注册点击事件，结果都使用Toast弹出水果的名称。

	 @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fruit_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(view.getContext(),"you clicked image"+fruit.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        holder.fruitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(view.getContext(),"you clicked text"+fruit.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return  holder;
    }

运行程序，点击右上角的橙子图片，结果如下：  
显示了“you clicked image”+水果名称    
<img src="/FourHomework/img/clickImage.jpg" width="50%" height="50%">  

点击右上角的橙子图片，结果如下：  
显示了“you clicked Text”+水果名称  
<img src="/FourHomework/img/clickText.jpg" width="50%" height="50%">  

## 实验总结
通过这次实验，更加深入的掌握RecyclerView的使用和点击事件，以后会更加努力练习，达到熟悉的程度。
