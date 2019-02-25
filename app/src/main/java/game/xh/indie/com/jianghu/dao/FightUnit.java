package game.xh.indie.com.jianghu.dao;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class FightUnit {
    private String name;
    private int health;    //当前生命值
    private int energy;    //当前内力值
    private int atk;    //攻击值
    private int def;    //防御值
    private int criticalHitRate;    //会心一击率
    private int dodgeRate;    //闪避率
    private float attackSpeed;    //攻击速度
    private int counterattackRate; //反击率
    private int side;

    private boolean ifLive = true;  //存活状态

    public  FightUnit(){

    }

    public FightUnit(String name, int health, int energy, int atk, int def, int criticalHitRate, int dodgeRate, float attackSpeed,int counterattackRate) {
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

    /**
     * 攻击敌方单位
     * @param defUnit 被攻击的对象
     */
    public void attack(FightUnit defUnit){
        StringBuffer buffer = new StringBuffer();
        //判断攻击方是主角还是怪物，主角用绿色名字，怪物用红色名字
        String atkUnitName,defUnitName;
        if(defUnit.side==-1){
            atkUnitName = "<font color=\"green\">"+this.name+"</font>";
            defUnitName = "<font color=\"red\">"+defUnit.name+"</font>";
        }else{
            atkUnitName = "<font color=\"red\">"+this.name+"</font>";
            defUnitName = "<font color=\"green\">"+defUnit.name+"</font>";
        }
        buffer.append(atkUnitName+" 对 "+defUnitName+" 发起攻击，");

        double doubleDmg=1;
        int dmgNum=0;
        //计算攻击倍率
        if ((int)(Math.random()*100+1) <= this.criticalHitRate) {
            doubleDmg = doubleDmg * 2;
            buffer.append(atkUnitName+" 使出了全身力气，");
        }
        //判断敌方是否成功闪避
        if((int)(Math.random()*100+1) > defUnit.dodgeRate) {
            dmgNum=(int)(this.atk * doubleDmg -defUnit.def);
        }else{
            buffer.append("但 "+ defUnitName +"</font> 躲开了攻击，");
        }
        //根据造成的伤害，扣除敌方生命
        if(dmgNum<0) dmgNum=0;
        defUnit.setHealth(defUnit.health-dmgNum);
        String msg = buffer + " 造成了 <font color=\"red\">"+dmgNum+"</font> 点伤害";
        Log.d("FightInfo", msg);
        EventBus.getDefault().post(new MessageEvent(msg));
        //判断战斗是否胜利,敌方血量为0,则战斗结束
        if(defUnit.health> 0){
            //判断敌方是否敌方反击
            if ((int)(Math.random()*100+1) <= defUnit.counterattackRate) defUnit.counterattack(this);
        }else{
            defUnit.ifLive = false;
        }
    }

    /**
     * 对敌方的攻击进行反击
     * @param defUnit 攻击方
     */
    public void  counterattack(FightUnit defUnit){
        StringBuffer buffer = new StringBuffer();
        //判断攻击方是主角还是怪物，主角用绿色名字，怪物用红色名字
        String atkUnitName,defUnitName;
        if(defUnit.side==-1){
            atkUnitName = "<font color=\"green\">"+this.name+"</font>";
            defUnitName = "<font color=\"red\">"+defUnit.name+"</font>";
        }else{
            atkUnitName = "<font color=\"red\">"+this.name+"</font>";
            defUnitName = "<font color=\"green\">"+defUnit.name+"</font>";
        }
        buffer.append(atkUnitName+" 找到 "+defUnitName+" 的破绽，反手一击");
        double doubleDmg= 0.6;
        int dmgNum=0;
        //计算攻击倍率
        if ((int)(Math.random()*100+1) <= this.criticalHitRate) {
            doubleDmg = doubleDmg * 2;
            buffer.append(atkUnitName+" 使出了全身力气，");
        }
        //判断敌方是否成功闪避
        if((int)(Math.random()*100+1) > defUnit.dodgeRate) {
            dmgNum = (int)(this.atk * doubleDmg -defUnit.def);
        }else{
            buffer.append(defUnitName+" 躲开了攻击，");
        }
        //根据造成的伤害，扣除敌方生命
        if(dmgNum<0) dmgNum=0;
        defUnit.setHealth(defUnit.health-dmgNum);
        String msg = buffer + " 造成了 <font color=\"red\">"+dmgNum+"</font> 点伤害";
        Log.d("FightInfo", msg);
        EventBus.getDefault().post(new MessageEvent(msg));
        if (defUnit.health<= 0) defUnit.ifLive= false;
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

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getCounterattackRate() {
        return counterattackRate;
    }

    public void setCounterattackRate(int counterattackRate) {
        this.counterattackRate = counterattackRate;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public boolean isIfLive() {
        return ifLive;
    }

    public void setIfLive(boolean ifLive) {
        this.ifLive = ifLive;
    }
}
