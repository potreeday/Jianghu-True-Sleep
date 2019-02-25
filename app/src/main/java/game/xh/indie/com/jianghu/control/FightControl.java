package game.xh.indie.com.jianghu.control;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import game.xh.indie.com.jianghu.dao.Enemy;
import game.xh.indie.com.jianghu.dao.MessageEvent;
import game.xh.indie.com.jianghu.dao.Role;

public class FightControl {
    public static Timer timer;

    public static void fightAuto(Role role, Enemy enemy) {
        final Role player1 = role;
        final Enemy player2 = enemy;
        timer = new Timer();
        //主角攻击
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player1.attack(player2);
                if (judgeComplete(player1, player2)) timer.cancel();
            }
        }, 0, (int) (player1.getAttackSpeed() * 1000));
        //怪物攻击
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player2.attack(player1);
                if (judgeComplete(player1, player2)) timer.cancel();
            }
        }, 0, (int) (enemy.getAttackSpeed() * 1000));
    }

    public static boolean judgeComplete(Role role, Enemy enemy) {
        Boolean ifComplete = false;
        //如果最终是主角获胜，结算奖励
        if (!enemy.isIfLive()) {
            role.fightRewardCal(enemy);
            role.setPlayerStatus(Role.PLAYER_RESTING);
            ifComplete = true;
        } else if (!role.isIfLive()) {
            String msg = role.getName() + " 被 " + enemy.getName() + " 击败了。";
            //Log.d("FightInfo", msg);
            EventBus.getDefault().post(new MessageEvent(msg));
            role.setPlayerStatus(Role.PLAYER_TREATING);
            EventBus.getDefault().post(new MessageEvent(role.getName() + "决定找了个僻静的地方去疗伤"));
            ifComplete = true;
        }
        //战斗双方任一方血量<=0,战斗结束，主角状态重置
        if (!role.isIfLive() || !enemy.isIfLive()) {
            //timer.cancel();
            role.setHealth(role.getMaxHealth());
            role.setIfLive(true);
        }
        return ifComplete;
    }

    /**
     * 强制结束战斗
     */
    public static void stopAction(Role role) {
        if (timer != null) {
            timer.cancel();
        }
        role.setPlayerStatus(Role.PLAYER_RESTING);
        role.setHealth(role.getMaxHealth());
    }
}
