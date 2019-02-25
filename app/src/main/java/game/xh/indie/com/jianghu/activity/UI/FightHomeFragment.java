package game.xh.indie.com.jianghu.activity.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import game.xh.indie.com.jianghu.Jianghu;
import game.xh.indie.com.jianghu.R;
import game.xh.indie.com.jianghu.control.FightControl;
import game.xh.indie.com.jianghu.control.SaveOrLoadPlayer;
import game.xh.indie.com.jianghu.dao.FightScene;
import game.xh.indie.com.jianghu.dao.MessageEvent;
import game.xh.indie.com.jianghu.dao.Role;

public class FightHomeFragment extends Fragment {
    private Jianghu jh;
    private List<FightScene> lFscene;
    private Role role;
    private Timer timer;
    private LinearLayout fightSceneLayout;
    private TextView t_fight_log;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_fight,container,false);

        //获取场景列表,生成页面各元素
        jh=(Jianghu) getActivity().getApplication();
        lFscene=Jianghu.getlFscene();
        fightSceneLayout=(LinearLayout)view.findViewById(R.id.layout_scene);
        //fightSceneLayout.addView(rowsLayout(4));
        fightSceneLayout.addView(sceneLayout(lFscene,4 ,getContext()));
        t_fight_log=(TextView)view.findViewById(R.id.t_fight_log);
        t_fight_log.setMovementMethod(ScrollingMovementMethod.getInstance());

        //注册EventBus
        EventBus.getDefault().register(this);

        //获取主角
        role =jh.getRole();
        int fsNow= role.getFsNow();
        ((Button)view.findViewById(fsNow)).performClick();

        return view;
    }

    /**
     * @param lFscene 活动场景列表
     * @return 返回自定义的场景布局
     */
    private LinearLayout sceneLayout(List<FightScene> lFscene, int clomuns, Context context){
        int sceneNums=lFscene.size();
        LinearLayout sly=new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                sly.addView(rowsLayout(lfs_rows,context));
            }
        }else{
            //如果不能够被整除
            int i;
            for ( i=0 ; i < rows; i++) {
                List<FightScene> lfs_rows = new ArrayList<FightScene>();
                for(int j=0; j<clomuns; j++){
                    lfs_rows.add( lFscene.get( i * clomuns + j  ));
                }
                sly.addView(rowsLayout(lfs_rows,context));
            }
            //创建最后剩下的，不足一行的布局
            List<FightScene> lfs_surplus = new ArrayList<FightScene>();
            for(int k =0;k<surplus;k++){
                lfs_surplus.add( lFscene.get( i * clomuns + k));
            }
            sly.addView(rowsLayout(lfs_surplus,context));
        }
        return  sly;
    }

    /**
     * @param clomunsFscene 单行显示的场景
     * @return 返回单行显示的LinearLayout
     */
    private LinearLayout rowsLayout(List<FightScene> clomunsFscene,Context context){
        int clomuns= clomunsFscene.size();
        LinearLayout rl= new LinearLayout(context);
        //设置LayoutParams
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置为水平布局
        rl.setOrientation(LinearLayout.HORIZONTAL);
        rl.setLayoutParams(lp);
        //循环添加场景button
        for (int j = 0; j < clomuns; j++) {
            Button b_scene = new Button(context);
            //设置场景按钮的id和名称
            // 给场景按钮绑定点击事件
            b_scene.setText(clomunsFscene.get(j).getName());
            b_scene.setId(clomunsFscene.get(j).getId());
            b_scene.setText(clomunsFscene.get(j).getName());
            b_scene.setOnClickListener(clickListener);
            //添加到创建的线性布局中
            rl.addView(b_scene);
        }
        return rl;
    }

    /**
     * 场景控件的点击事件，更换场景
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            t_fight_log.append(Html.fromHtml("你来到了<strong><font color=\"blue\">"+((TextView)v).getText()+"</font></Strong><br>"));
            //设置t_fight_log自动滚动到最后一行
            int offset=t_fight_log.getLineCount()*t_fight_log.getLineHeight();
            if(offset>t_fight_log.getHeight()){
                t_fight_log.scrollTo(0,offset-t_fight_log.getHeight());
            }

            //更换场景的时候，如果角色正在忙，那么停止当前工作
            if (role.getPlayerStatus() !=0) FightControl.stopAction(role);
            role.setFsNow(((Button)v).getId());

            try {
                SaveOrLoadPlayer.savePlayer(role,getContext());
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
                        SaveOrLoadPlayer.savePlayer(role,getContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },0,900000);
        }
    };

    /**
     * 使用EventBus更新textview
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        t_fight_log.append(Html.fromHtml(messageEvent.getMessage()+"<br>"));
        //设置t_fight_log自动滚动到最后一行
        int offset=t_fight_log.getLineCount()*t_fight_log.getLineHeight();
        if(offset>t_fight_log.getHeight()){
            t_fight_log.scrollTo(0,offset-t_fight_log.getHeight());
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        //停止角色的全部行动
        timer.cancel();
        FightControl.stopAction(role);
        //role.stopAction();
        EventBus.getDefault().unregister(this);
        try {
            SaveOrLoadPlayer.savePlayer(role,getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
