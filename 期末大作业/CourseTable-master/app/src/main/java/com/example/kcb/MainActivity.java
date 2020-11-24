package com.example.kcb;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //星期几
    private RelativeLayout day;

    //SQLite Helper类
    private DatabaseHelper databaseHelper = new DatabaseHelper
            (this, "database.db", null, 1);

    //被点击的View
    View ClickedView;
    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //工具条
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //从数据库读取数据
        loadData();
    }

    //从数据库加载数据
    private void loadData() {
        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end"))));
            } while(cursor.moveToNext());
        }
        cursor.close();

        //使用从数据库读取出来的课程信息来加载课程表视图
        for (Course course : coursesList) {
            createLeftView(course);
            createItemCourseView(course);
        }
    }

    //保存数据到数据库
    private void saveData(Course course) {
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("insert into courses(course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?)",
                new String[] {course.getCourseName(),
                        course.getTeacher(),
                        course.getClassRoom(),
                        course.getDay()+"",
                        course.getStart()+"",
                        course.getEnd()+""}
                );
    }

    //创建"第几节数"视图
    private void createLeftView(Course course) {
        int endNumber = course.getEnd();
        if (endNumber > maxCoursesNumber) {
            for (int i = 0; i < endNumber-maxCoursesNumber; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
                view.setLayoutParams(params);

                TextView text = view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));

                LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
                leftViewLayout.addView(view);
            }
            maxCoursesNumber = endNumber;
        }
    }

    //获得控件里面的星期几控件
    private RelativeLayout getViewDay(int day){
        int dayId = 0;
        switch (day) {
            case 1: dayId = R.id.monday; break;
            case 2: dayId = R.id.tuesday; break;
            case 3: dayId = R.id.wednesday; break;
            case 4: dayId = R.id.thursday; break;
            case 5: dayId = R.id.friday; break;
            case 6: dayId = R.id.saturday; break;
            case 7: dayId = R.id.weekday; break;
        }
        return findViewById(dayId);
    }

    //创建单个课程视图
    private void createItemCourseView(final Course course) {
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd())
            Toast.makeText(this, "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
        else {

            day = getViewDay(getDay);

            int height = 180;
            final View v = LayoutInflater.from(this).inflate(R.layout.course_card, null); //加载单个课程布局
            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getEnd()-course.getStart()+1)*height - 8); //设置布局高度,即跨多少节课

            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName() + "\n" + course.getTeacher() + "\n" + course.getClassRoom()); //显示课程名
            day.addView(v);

            //查看课程
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClickedView=view;
                    Intent intent = new Intent(MainActivity.this, SeeCourseActivity.class);
                    intent.putExtra("seeCourse", course);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_courses:
                Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.menu_about:
                Intent intent1 = new Intent(this, AboutActivity.class);
                startActivity(intent1);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            //创建课程表左边视图(节数)
            createLeftView(course);
            //创建课程表视图
            createItemCourseView(course);
            //存储数据到数据库
            saveData(course);
        }

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Course PreCourse = (Course) data.getSerializableExtra("PreCourse");
            boolean isDelete = data.getBooleanExtra("isDelete",true);


            if(isDelete){
                ClickedView.setVisibility(View.GONE);//先隐藏
                day=getViewDay(PreCourse.getDay());
                day.removeView(ClickedView);//再移除课程视图
                SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
                sqLiteDatabase.execSQL("delete from courses where course_name = ? and day =? and class_start=? and class_end=?",
                        new String[] {PreCourse.getCourseName(),
                                String.valueOf(PreCourse.getDay()),
                                String.valueOf(PreCourse.getStart()),
                                String.valueOf(PreCourse.getEnd())});
                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
                intent.putExtra("ReviseCourse", PreCourse);
                intent.putExtra("isRevise",true);
                startActivityForResult(intent,2);
            }

        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            Course PreCourse = (Course) data.getSerializableExtra("PreCourse");
            Course newCourse = (Course) data.getSerializableExtra("newCourse");

            ClickedView.setVisibility(View.GONE);//先隐藏
            day=getViewDay(PreCourse.getDay());
            day.removeView(ClickedView);//再移除课程视图

            //创建课程表左边视图(节数)
            createLeftView(newCourse);
            //创建课程表视图
            createItemCourseView(newCourse);

            SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
            sqLiteDatabase.execSQL("update courses set " +
                            "course_name = ?,teacher = ?,class_room=? ,day=? ,class_start=? ,class_end =?" +
                            "where course_name = ? and day =? and class_start=? and class_end=?",
                    new String[] {newCourse.getCourseName(),
                            newCourse.getTeacher(),
                            newCourse.getClassRoom(),
                            String.valueOf(newCourse.getDay()),
                            String.valueOf(newCourse.getStart()),
                            String.valueOf(newCourse.getEnd()),
                            PreCourse.getCourseName(),
                            String.valueOf(PreCourse.getDay()),
                            String.valueOf(PreCourse.getStart()),
                            String.valueOf(PreCourse.getEnd())});
        }

    }

}