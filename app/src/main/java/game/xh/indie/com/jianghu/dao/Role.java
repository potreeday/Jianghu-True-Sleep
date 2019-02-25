package game.xh.indie.com.jianghu.dao;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import game.xh.indie.com.jianghu.Jianghu;
import game.xh.indie.com.jianghu.control.FightControl;

public class Role extends FightUnit {
    public static final int PLAYER_RESTING = 0;     //主角状态：空闲中
    public static final int PLAYER_FIGHTING = 1;    //主角状态：行侠仗义中
    public static final int PLAYER_LEARNING_SKILL = 2;  //主角状态：修炼中
    public static final int PLAYER_TREATING = 3;    //主角状态：疗伤中

    private int timerCal = 0;         //疗伤5秒后更新主角状态

    private int maxHealth;    //主角的最大生命值
    private int maxEnergy;    //主角的最大内力值
    private int maxWeight;      //主角的最大负重值
    private int fsNowID;        //主角所在场景名称
    private int playerStatus;    //主角的行动状态

    private int level;            //主角等级
    private int expNow;            //主角当前经验
    private int money;              //主角当前银两
    private float weightNow;          //主角当前负重（计算装备的）

    private List<Item> lItem;       //主角携带的道具
    private Item weaponWear;      //主角穿戴的武器
    private Item armorWear;        //主角穿戴的防具
    //private SkillEnergy skillEnergyUsing;       //主角正在运行的内功心法
    //private List<Book> bookLearning;      //主角正在修炼的武功秘籍
    //private Skill skillUsing;       //主角正在使用的招式

    //新建游戏角色
    public Role() {
        //主角的初始行为
        this.playerStatus = PLAYER_RESTING;
        //主角的初始生命和内力
        this.maxHealth = 30;
        this.maxEnergy = 15;
        this.fsNowID = 1;
        this.setAtk(5);
        this.setDef(3);
        this.setHealth(this.maxHealth);
        this.setEnergy(this.maxEnergy);
        this.setAttackSpeed(2);
        this.setDodgeRate(0);
        this.setCriticalHitRate(0);
        this.setName("你");
        this.setCounterattackRate(0);
        this.setExpNow(0);
        this.setLevel(0);
        this.setSide(1);
        this.money = 100;
        this.maxWeight = 60;
        this.weightNow = 0;
        this.lItem = new ArrayList<Item>();
    }

    /**
     * 角色行动的逻辑
     */
    public void actionSelect() {
        //当主角空闲中时，开始做某事
        switch (this.playerStatus) {
            case Role.PLAYER_TREATING:           //战败后，需要疗伤5秒
                this.timerCal++;
                EventBus.getDefault().post(new MessageEvent(this.getName()+"疗伤中..."));
                if (this.timerCal == 5) {
                    this.playerStatus = PLAYER_RESTING;
                    this.timerCal = 0;
                }
                break;
            case PLAYER_RESTING:            //主角休息时，概率性的去练功或者行侠仗义
                int rate = (int)( Math.random()*100);
                if(rate <20 ){
                    this.playerStatus = PLAYER_LEARNING_SKILL;
                    String msg = "<font color=\"green\">" + this.getName() + "</font>心中突有所感，决定去修炼武功......";
                    Log.d("FightInfo", msg);
                    //利用EventBus传递消息
                    EventBus.getDefault().post(new MessageEvent(msg));
                }else{
                    String msg = "<font color=\"green\">" + this.getName() + "</font>决定去行侠仗义......";
                    Log.d("FightInfo", msg);
                    //利用EventBus传递消息
                    EventBus.getDefault().post(new MessageEvent(msg));
                    this.setPlayerStatus(Role.PLAYER_FIGHTING);
                    this.actLikeHero();
                }
                break;
            case PLAYER_LEARNING_SKILL:
                this.updateRoleBySkill();
                break;
            default:
                break;
        }
    }


    /**
     * 角色行动：行侠仗义（打怪）
     */
    public void actLikeHero() {
        //刷新怪物，判断是否要刷新BOSS
        List<Enemy> enemyList = null;
        if (Math.random() * 100 < 10 && Jianghu.getlBossEnemy().get(this.getFsNowID() - 1).size() != 0) {
            enemyList = Jianghu.getlBossEnemy().get(this.getFsNowID() - 1);
        } else {
            enemyList = Jianghu.getlEnemy().get(this.getFsNowID() - 1);
        }
        Enemy enemyModel = enemyList.get((int) (Math.random() * enemyList.size()));
        //根据获取到的怪物模板，生成怪物对象
        final Enemy enemy = new Enemy();
        enemy.setName(enemyModel.getName());
        enemy.setHealth(enemyModel.getHealth());
        enemy.setEnergy(enemyModel.getEnergy());
        enemy.setAtk(enemyModel.getAtk());
        enemy.setDef(enemyModel.getDef());
        enemy.setCriticalHitRate(enemyModel.getCriticalHitRate());
        enemy.setAttackSpeed(enemyModel.getAttackSpeed());
        enemy.setCounterattackRate(enemyModel.getCounterattackRate());
        enemy.setExp(enemyModel.getExp());
        enemy.setDodgeRate(enemyModel.getDodgeRate());
        enemy.setDropItem(enemyModel.getDropItem());
        enemy.setIsBoss(enemyModel.getIsBoss());
        enemy.setSkill(enemyModel.getSkill());
        FightControl.fightAuto(this, enemy);
    }

    /**
     * 杀死怪物的掉落奖励
     *
     * @param enemy 怪物对象
     */
    public void fightRewardCal(Enemy enemy) {
        //获取经验奖励
        this.expNow = expNow + enemy.getExp();
        StringBuffer msg = new StringBuffer();
        msg.append(this.getName() + " 取得了胜利,获得了 " + enemy.getExp() + " 点经验奖励。\n");
        //获取掉落列表
        String[] droplist = enemy.getDropItem().split("\\|");
        //判断掉落，最多掉落2件道具
        for (int i = 0; i < 2; i++) {
            int dice = (int) (Math.random() * 100 + 1);
            int itemDropRate = 0;
            for (int j = 0; j < droplist.length; j++) {
                int tempRate = itemDropRate + Integer.parseInt(droplist[j].split(",")[1]);
                if (dice > itemDropRate && dice <= tempRate) {
                    int itemId = Integer.parseInt(droplist[j].split(",")[0]);
                    for (Item itemd : Jianghu.getlItem()) {
                        if (itemd.getId() == itemId){
                            //未超过最大负重时，掉落有效
                            if (this.weightNow + itemd.getWeight() > this.maxWeight) break;
                            //根据物品模板生成掉落的物品对象
                            Item willdrop = new Item();
                            willdrop.setId(itemd.getId());
                            willdrop.setDef(itemd.getDef());
                            willdrop.setDmg(itemd.getDmg());
                            willdrop.setEnergyCanAdd(itemd.getEnergyCanAdd());
                            willdrop.setHealthCanAdd(itemd.getHealthCanAdd());
                            willdrop.setName(itemd.getName());
                            willdrop.setPrice(itemd.getPrice());
                            willdrop.setSkillCanLearn(itemd.getSkillCanLearn());
                            willdrop.setType(itemd.getType());
                            willdrop.setWeight(itemd.getWeight());
                            willdrop.setPic(itemd.getPic());
                            this.lItem.add(willdrop);
                            msg.append("获得了道具：<font color=\"blue\">" + willdrop.getName() + "</font>");
                        }
                    }
                    itemDropRate = tempRate;
                } else {
                    itemDropRate = tempRate;
                }
            }
        }
        Log.d("FightInfo", msg + "");
        EventBus.getDefault().post(new MessageEvent(msg + ""));
        //判断是否可升级
        levelUp();
    }


    /**
     * 角色升级判断的逻辑
     */
    public void levelUp() {
        //等级未满时，判断是否可以升级
        if (this.level < Jianghu.levelNeed.length) {
            if (this.expNow > Jianghu.levelNeed[this.level]) {
                this.level++;
                this.setAtk(this.getAtk() + 1);
                this.maxHealth = this.maxHealth + 8;
                this.maxEnergy = this.maxEnergy + 5;
                String msg = this.getName() + " 升到了 " + this.level + " 级了。";
                Log.d("FightInfo", msg);
                EventBus.getDefault().post(new MessageEvent(msg));
            }
        }
    }

    /**
     * 角色修炼的逻辑
     */
    public void updateRoleBySkill() {
        this.timerCal++;
        EventBus.getDefault().post(new MessageEvent("努力修炼中..."));
        if (this.timerCal == 5) {
            this.playerStatus = PLAYER_RESTING;
            EventBus.getDefault().post(new MessageEvent(this.getName() + "结束了修炼。但是并没有学到什么东西..."));
            completeUpdateSkill();
            this.timerCal = 0;
        }
    }

    /**
     * 判断角色修炼的成果
     */
    public void completeUpdateSkill(){

    }

    /**
     * 穿戴装备
     */
    public void wearEquip(int equipIndex){
        Item item = this.getlItem().get(equipIndex);
        if(item.getType()==1){
            this.getlItem().remove(equipIndex);
            if(this.getWeaponWear()!=null){
                this.getlItem().add(this.getWeaponWear());
                this.setAtk(this.getAtk()-this.getWeaponWear().getDmg());
                this.setWeightNow(this.getWeightNow()+this.getWeaponWear().getWeight());
            }
            this.setWeaponWear(item);
            this.setAtk(this.getAtk()+item.getDmg());
            this.setWeightNow(this.getWeightNow()-item.getWeight());
        }else if(item.getType()==2){
            this.getlItem().remove(equipIndex);
            if(this.getArmorWear()!=null){
                this.getlItem().add(this.getArmorWear());
                this.setDef(this.getDef()-this.getArmorWear().getDef());
                this.setWeightNow(this.getWeightNow()+this.getArmorWear().getWeight());
            }
            this.setArmorWear(item);
            this.setDef(this.getDef()+item.getDef());
            this.setWeightNow(this.getWeightNow()-item.getWeight());
        }
    }

    /**
     * 取消穿戴装备
     */
    public void unWearEquip(Item equip){
        this.getlItem().add(equip);
        if(equip.getType()==1) {
            this.setWeaponWear(null);
            this.setAtk(this.getAtk() - equip.getDmg());
        }
        if(equip.getType()==2){
            this.setArmorWear(null);
            this.setDef(this.getDef()-equip.getDef());
        }
        this.setWeightNow(this.getWeightNow()+equip.getWeight());
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

    public void setLevelNeed(String fileName) {

    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public float getWeightNow() {
        return weightNow;
    }

    public void setWeightNow(float weightNow) {
        this.weightNow = weightNow;
    }

    public List<Item> getlItem() {
        return lItem;
    }

    public void setlItem(List<Item> lItem) {
        this.lItem = lItem;
    }

    public Item getWeaponWear() {
        return weaponWear;
    }

    public void setWeaponWear(Item weaponWear) {
        this.weaponWear = weaponWear;
    }

    public Item getArmorWear() {
        return armorWear;
    }

    public void setArmorWear(Item armorWear) {
        this.armorWear = armorWear;
    }
}
