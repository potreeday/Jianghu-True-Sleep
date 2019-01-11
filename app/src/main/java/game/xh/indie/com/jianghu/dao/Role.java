package game.xh.indie.com.jianghu.dao;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Role extends FightUnit {
    public static final int PLAYER_RESTING=0;     //主角状态：空闲中
    public static final int PLAYER_FIGHTING=1;    //主角状态：行侠仗义中
    public static final int PLAYER_LEARNING_SKILL=2;  //主角状态：练习技能中

    private int maxHealth;    //定义主角的最大生命值
    private int maxEnergy;    //定义主角的最大内力值
    private int fsNowID;        //主角所在场景名称
    private int playerStatus;    //主角的行动状态

    private int level;            //主角等级
    private int expNow;            //主角当前经验
    final private int[] levelNeed = {100,250,450,700,1050,1500,2050};      //主角升级所需经验

    //新建游戏角色
    public Role(){
        //主角的初始行为
        this.playerStatus=PLAYER_RESTING;
        //主角的初始生命和内力
        this.maxHealth=20;
        this.maxEnergy=10;
        this.fsNowID=1;
        this.setAtk(3);
        this.setDef(1);
        this.setHealth(this.maxHealth);
        this.setEnergy(this.maxEnergy);
        this.setAttackSpeed(2);
        this.setDodgeRate(0);
        this.setCriticalHitRate(0);
        this.setName("你");
        this.setCounterattackRate(0);
        this.setExpNow(0);
        this.setLevel(0);
    }

    public void actionSelect(){
        //当主角空闲中时，开始做某事
        if(this.playerStatus!=PLAYER_RESTING){
            return;
        }else {
            Log.d("FightInfo", "你决定去行侠仗义");
            this.playerStatus=PLAYER_FIGHTING;
            actLikeHero();
        }
    }

    //行侠仗义
    public void actLikeHero(){
        final Enemy enemy = new Enemy(1,"怪物",30,0,2,1,15,15,2,15,15);
        final Timer timer = new Timer();
        //主角攻击
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Role.this.attack(enemy);
                judgeWin(timer,Role.this,enemy);
            }
        },0,this.getAttackSpeed()*1000);

        //怪物攻击
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                enemy.attack(Role.this);
                judgeWin(timer,Role.this,enemy);
            }
        },0,enemy.getAttackSpeed()*1000);
    }

    //胜负判断
    public void judgeWin(Timer timer , Role role, Enemy enemy){
        //如果最终是主角获胜，结算奖励
        if (!enemy.isIfLive()){
            role.fightRewardCal(enemy);
        }else if(!role.isIfLive()){
            Log.d("FightInfo", role.getName()+" 被 " + enemy.getName() +" 击败了。");
        };
        //战斗双方任一方血量<=0,战斗结束，主角状态重置
        if(!role.isIfLive() || !enemy.isIfLive() ){
            timer.cancel();
            role.setPlayerStatus(PLAYER_RESTING);
            role.setHealth(role.maxHealth);
            role.setIfLive(true);
        }
    }

    //奖励结算
    public void  fightRewardCal (Enemy enemy){
        //获取经验奖励
        this.expNow = expNow + enemy.getExp();
        Log.d("FightInfo",  this.getName() + " 取得了胜利,获得了 "+ enemy.getExp()+" 点经验奖励。");
        //等级未满时，判断是否可以升级
        if(this.level< this.levelNeed.length){
            if(this.expNow> levelNeed[this.level]){
                this.level++;
                this.setAtk(this.getAtk()+1);
                this.maxHealth=this.maxHealth+8;
                this.maxEnergy=this.maxEnergy+5;
                Log.d("FightInfo", this.getName()+" 升到了 "+this.level+" 级了。");
            }
        }
    }

    //运行内功
    public void updateRoleByNeigong(){

    }

    //使用招式
    public void updateRoleBySkill(){

    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public int getFsNow() {
        return fsNowID;
    }

    public void setFsNow(int fsNowID) {
        this.fsNowID = fsNowID;
    }

    public int getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(int playerStatus) {
        this.playerStatus = playerStatus;
    }

    public int getFsNowID() {
        return fsNowID;
    }

    public void setFsNowID(int fsNowID) {
        this.fsNowID = fsNowID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExpNow() {
        return expNow;
    }

    public void setExpNow(int expNow) {
        this.expNow = expNow;
    }

    public void setLevelNeed(String fileName){

    }
}