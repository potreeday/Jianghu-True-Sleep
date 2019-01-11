package game.xh.indie.com.jianghu.dao;

import android.util.Log;

public class FightUnit {
    private String name;
    private int health;    //当前生命值
    private int energy;    //当前内力值
    private int atk;    //攻击值
    private int def;    //防御值
    private int criticalHitRate;    //会心一击率
    private int dodgeRate;    //闪避率
    private int attackSpeed;    //攻击速度
    private int counterattackRate; //反击率

    private boolean ifLive = true;  //存活状态

    public  FightUnit(){

    }

    public FightUnit(String name, int health, int energy, int atk, int def, int criticalHitRate, int dodgeRate, int attackSpeed,int counterattackRate) {
        this.name = name;
        this.health = health;
        this.energy = energy;
        this.atk = atk;
        this.def = def;
        this.criticalHitRate = criticalHitRate;
        this.dodgeRate = dodgeRate;
        this.attackSpeed = attackSpeed;
        this.counterattackRate = counterattackRate;
    }

    //攻击
    public void attack(FightUnit enemy){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<font color=\"green\">"+this.name+"</font> 对 <font color=\"red\">"+enemy.name+"</font> 发起攻击，");
        double doubleDmg=1;
        int dmgNum=0;
        //计算攻击倍率
        if ((int)(Math.random()*100+1) <= this.criticalHitRate) {
            doubleDmg = doubleDmg * 2;
            buffer.append("<font color=\"green\">"+this.name+"</font> 使出了全身力气，");
        }
        //判断敌方是否成功闪避
        if((int)(Math.random()*100+1) > enemy.dodgeRate) {
            dmgNum=(int)(this.atk * doubleDmg -enemy.def);
        }else{
            buffer.append("但 <font color=\"red\">"+enemy.name+"</font> 躲开了攻击，");
        }
        //根据造成的伤害，扣除敌方生命
        if(dmgNum<0) dmgNum=0;
        enemy.setHealth(enemy.health-dmgNum);
        Log.d("FightInfo", buffer + " 造成了 <font color=\"red\">"+dmgNum+"</font> 点伤害");
        //判断战斗是否胜利
        if(enemy.health> 0){
            //判断敌方是否敌方反击
            if ((int)(Math.random()*100+1) <= enemy.counterattackRate) enemy.counterattack(this);
        }else{
            enemy.ifLive = false;
        }
    }

    //反击造成60%伤害
    public void  counterattack(FightUnit enemy){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<font color=\"green\">"+this.name+"</font> 找到 <font color=\"red\">"+enemy.name+"</font> 的破绽，反手一击");
        double doubleDmg= 0.6;
        int dmgNum=0;
        //计算攻击倍率
        if ((int)(Math.random()*100+1) <= this.criticalHitRate) {
            doubleDmg = doubleDmg * 2;
            buffer.append("<font color=\"green\">"+this.name+"</font> 使出了全身力气，");
        }
        //判断敌方是否成功闪避
        if((int)(Math.random()*100+1) > enemy.dodgeRate) {
            dmgNum = (int)(this.atk * doubleDmg -enemy.def);
        }else{
            buffer.append("但 <font color=\"red\">"+enemy.name+"</font> 躲开了攻击，");
        }
        //根据造成的伤害，扣除敌方生命
        if(dmgNum<0) dmgNum=0;
        enemy.setHealth(enemy.health-dmgNum);
        Log.d("FightInfo", buffer + " 造成了 <font color=\"red\">"+dmgNum+"</font> 点伤害");
        if (enemy.health<= 0) enemy.ifLive= false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getCriticalHitRate() {
        return criticalHitRate;
    }

    public void setCriticalHitRate(int criticalHitRate) {
        this.criticalHitRate = criticalHitRate;
    }

    public int getDodgeRate() {
        return dodgeRate;
    }

    public void setDodgeRate(int dodgeRate) {
        this.dodgeRate = dodgeRate;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getCounterattackRate() {
        return counterattackRate;
    }

    public void setCounterattackRate(int counterattackRate) {
        this.counterattackRate = counterattackRate;
    }

    public boolean isIfLive() {
        return ifLive;
    }

    public void setIfLive(boolean ifLive) {
        this.ifLive = ifLive;
    }
}
