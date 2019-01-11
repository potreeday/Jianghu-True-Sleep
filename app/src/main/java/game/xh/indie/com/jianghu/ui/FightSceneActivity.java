package game.xh.indie.com.jianghu.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import game.xh.indie.com.jianghu.Jianghu;
import game.xh.indie.com.jianghu.R;
import game.xh.indie.com.jianghu.dao.FightScene;
import game.xh.indie.com.jianghu.dao.Role;
import game.xh.indie.com.jianghu.util.SaveOrLoadPlayer;

public class FightSceneActivity extends AppCompatActivity {
    private Jianghu jh;
    private List<FightScene> lFscene;
    private Role role;
    private Timer timer;
    private LinearLayout fightSceneLayout;
    private TextView t_fight_log;
    private int clomuns=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_fight);

        jh=(Jianghu)getApplication();
        //获取场景列表,生成页面各元素
        lFscene=jh.getlFscene();
        fightSceneLayout=(LinearLayout)this.findViewById(R.id.layout_scene);
        //fightSceneLayout.addView(rowsLayout(4));
        fightSceneLayout.addView(sceneLayout(lFscene));
        t_fight_log=(TextView) findViewById(R.id.t_fight_log);

        //获取主角
        role =jh.getRole();
        int fsNow= role.getFsNow();
        ((Button)findViewById(fsNow)).performClick();
    }

    /**
     * @param lFscene 活动场景列表
     * @return 返回自定义的场景布局
     */
    private LinearLayout sceneLayout(List<FightScene> lFscene){
        int sceneNums=lFscene.size();
        LinearLayout sly=new LinearLayout(this);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //设置为垂直布局
        sly.setOrientation(LinearLayout.VERTICAL);
        sly.setLayoutParams(lp);

        int rows=sceneNums/clomuns;
        int surplus=sceneNums%clomuns;
        if (surplus==0) {
            //如果能够被整除
            for (int i = 0; i < rows; i++) {
                List<FightScene> lfs_rows = new ArrayList<FightScene>();
                for(int j=0; j<clomuns; j++){
                    lfs_rows.add( lFscene.get( i * clomuns + j - 1  ));
                }
                sly.addView(rowsLayout(lfs_rows));
            }
        }else{
            //如果不能够被整除
            int i;
            for ( i=0 ; i < rows; i++) {
                List<FightScene> lfs_rows = new ArrayList<FightScene>();
                for(int j=0; j<clomuns; j++){
                    lfs_rows.add( lFscene.get( i * clomuns + j  ));
                }
                sly.addView(rowsLayout(lfs_rows));
            }
            //创建最后剩下的，不足一行的布局
            List<FightScene> lfs_surplus = new ArrayList<FightScene>();
            for(int k =0;k<surplus;k++){
                lfs_surplus.add( lFscene.get( i * clomuns + k - 1  ));
            }
            sly.addView(rowsLayout(lfs_surplus));
        }
        return  sly;
    }

    /**
     * @param clomunsFscene 单行显示的场景
     * @return 返回单行显示的LinearLayout
     */
    private LinearLayout rowsLayout(List<FightScene> clomunsFscene){
        int clomuns= clomunsFscene.size();
        LinearLayout rl= new LinearLayout(this);
        //设置LayoutParams
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //设置为水平布局
        rl.setOrientation(LinearLayout.HORIZONTAL);
        rl.setLayoutParams(lp);
        //循环添加场景button
        for (int j = 0; j < clomuns; j++) {
            Button b_scene = new Button(this);
            //设置场景按钮的id和名称
            b_scene.setText(clomunsFscene.get(j).getName());
            b_scene.setId(clomunsFscene.get(j).getId());
            //绑定点击事件
            b_scene.setOnClickListener(clickListener);
            //添加到创建的线性布局中
            rl.addView(b_scene);
        }
        //添加到显示的父线性布局中
        return rl;
    }

    /**
     * 点击事件
     */
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            t_fight_log.append("你来到了"+((TextView)v).getText()+"\n");

            role.setFsNow(((Button)v).getId());
            try {
                SaveOrLoadPlayer.savePlayer(role,FightSceneActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(timer!=null){
                timer.cancel();
            }
            timer=new Timer();
            //定时器，定期判定主角的行为模式
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    role.actionSelect();
                }
            },0,1000);
            //定时器，每15分钟存储主角数据到存档文件
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        SaveOrLoadPlayer.savePlayer(role,FightSceneActivity.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },0,900000);
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        //结算自动挂机任务
        timer.cancel();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
