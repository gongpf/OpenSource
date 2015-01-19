package com.org.source.widget.menudrawer;

import com.org.source.widget.menudrawer.MenuDrawer.Type;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        textView1.setText("xxxxx");
        textView2.setText("xxxxfjdksjfkdjx");
        textView1.setTextColor(Color.RED);
        textView2.setTextColor(Color.RED);
        MenuDrawer drawer = MenuDrawer.createMenuDrawer(this, Position.LEFT, Type.BEHIND);
        drawer.setMenuView(textView1);
        drawer.setContentView(textView2);
        drawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        setContentView(drawer);
    }

}
