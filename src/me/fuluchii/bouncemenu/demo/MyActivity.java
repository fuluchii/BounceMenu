package me.fuluchii.bouncemenu.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import me.fuluchii.bouncemenu.R;
import me.fuluchii.bouncemenu.widget.BounceMenu;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {

    private BounceMenu menu;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        menu = (BounceMenu)findViewById(R.id.bounceView);
        LayoutInflater layoutInflater
                = (LayoutInflater)getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popview = layoutInflater.inflate(R.layout.pop,null);
        View popview2 = layoutInflater.inflate(R.layout.pop,null);
        View popview3 = layoutInflater.inflate(R.layout.pop,null);
        View popview4 = layoutInflater.inflate(R.layout.pop,null);


        final PopupWindow pop = new PopupWindow(popview,getWindowManager().getDefaultDisplay().getWidth(),400);
        final PopupWindow pop2 = new PopupWindow(popview2,getWindowManager().getDefaultDisplay().getWidth(),500);
        final PopupWindow pop3 = new PopupWindow(popview3,getWindowManager().getDefaultDisplay().getWidth(),300);
        final PopupWindow pop4 = new PopupWindow(popview4,getWindowManager().getDefaultDisplay().getWidth(),500);
        final Button dismiss = (Button)popview.findViewById(R.id.dismiss);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        final Button dismiss2 = (Button)popview2.findViewById(R.id.dismiss);

        dismiss2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop2.dismiss();
            }
        });

        final Button dismiss3 = (Button)popview3.findViewById(R.id.dismiss);

        dismiss3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop3.dismiss();
            }
        });

        final Button dismiss4 = (Button)popview4.findViewById(R.id.dismiss);

        dismiss4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop4.dismiss();
            }
        });

        List<PopupWindow> windowList = new ArrayList<PopupWindow>();
        windowList.add(pop);
        windowList.add(pop2);
        windowList.add(pop3);
        windowList.add(pop4);

        menu.setPopupWindowList(windowList);
        menu.bounce();

    }


}
