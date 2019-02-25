package game.xh.indie.com.jianghu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import game.xh.indie.com.jianghu.Jianghu;
import game.xh.indie.com.jianghu.R;
import game.xh.indie.com.jianghu.dao.Role;
import game.xh.indie.com.jianghu.control.SaveOrLoadPlayer;

public class MainActivity extends AppCompatActivity {
    private Jianghu jianghuApp;
    private Button b_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化游戏配置
        jianghuApp = ((Jianghu)getApplication());
        jianghuApp.init();

        //开始游戏
        b_start=(Button)this.findViewById(R.id.b_start);
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化主角
                try {
                    jianghuApp.setRole(SaveOrLoadPlayer.loadPlayer(MainActivity.this));
                }catch(IOException e){
                    jianghuApp.setRole(new Role());
                }

                Intent intent=new Intent(MainActivity.this,FightSceneActivity.class);
                startActivity(intent);
            }
        });
    }
}
