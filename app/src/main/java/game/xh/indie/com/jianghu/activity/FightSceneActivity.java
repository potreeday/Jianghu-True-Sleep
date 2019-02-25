package game.xh.indie.com.jianghu.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import game.xh.indie.com.jianghu.R;
import game.xh.indie.com.jianghu.activity.UI.FightHomeFragment;
import game.xh.indie.com.jianghu.activity.UI.RoleFragment;


public class FightSceneActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_fight);

        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("战斗"));
        mTabLayout.addTab(mTabLayout.newTab().setText("角色"));
        mTabLayout.addTab(mTabLayout.newTab().setText("任务"));
        mTabLayout.addTab(mTabLayout.newTab().setText("系统"));

        mFragments = new ArrayList<Fragment>();
        mFragments.add(new FightHomeFragment());
        mFragments.add(new RoleFragment());

        getSupportFragmentManager().beginTransaction().replace(R.id.home_container, mFragments.get(0)).commit();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //切换到与tab关联的fragment
                try {
                    Fragment fragment = mFragments.get(tab.getPosition());
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.e("RunError", "功能即将开放，敬请期待");
                    return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
