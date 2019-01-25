package game.xh.indie.com.jianghu.dao;

public class Enemy extends FightUnit {
    private int id;    //怪物编号
    private int exp;    //怪物提供的经验
    private int sceneBelong;
    private String dropItem;    //怪物掉落
    private int isBoss;     //是否首领怪
    private int skill;      //怪物使用的招式

    public Enemy() {
        super();
        this.setSide(-1);
    }

    public Enemy(int id, String name, int health, int energy, int atk, int def, int criticalHitRate, int dodgeRate, int attackSpeed,int exp,int counterattackRate) {
        super(name, health, energy, atk, def, criticalHitRate, dodgeRate, attackSpeed,counterattackRate);
        this.id=id;
        this.exp=exp;
        this.setSide(-1);
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSceneBelong() {
        return sceneBelong;
    }

    public void setSceneBelong(int sceneBelong) {
        this.sceneBelong = sceneBelong;
    }

    public String getDropItem() {
        return dropItem;
    }

    public void setDropItem(String dropItem) {
        this.dropItem = dropItem;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int getSkill() {
        return skill;
    }

    public int getIsBoss() {
        return isBoss;
    }

    public void setIsBoss(int isBoss) {
        this.isBoss = isBoss;
    }
}
