package game.xh.indie.com.jianghu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import game.xh.indie.com.jianghu.Jianghu;
import game.xh.indie.com.jianghu.R;
import game.xh.indie.com.jianghu.dao.Role;
import game.xh.indie.com.jianghu.util.SaveOrLoadPlayer;

public class MainActivity extends AppCompatActivity {
    private Jianghu jh;
    private Button b_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化游戏配置
        jh=(Jianghu)getApplication();
        jh.init();

        //开始游戏
        b_start=(Button)this.findViewById(R.id.b_start);
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化主角
                try {
                    jh.setRole(SaveOrLoadPlayer.loadPlayer(MainActivity.this));
                    System.out.println("主角现在在："+jh.getRole().getFsNow());
                }catch(IOException e){
                    jh.setRole(new Role());
                }

                Intent intent=new Intent(MainActivity.this,FightSceneActivity.class);
                startActivity(intent);
            }
        });
    }
}
