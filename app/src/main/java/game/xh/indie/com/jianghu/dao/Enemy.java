package game.xh.indie.com.jianghu.dao;

public class Enemy extends FightUnit {
    private int id;    //怪物编号
    private int exp;    //怪物提供的经验

    public Enemy(int id, String name, int health, int energy, int atk, int def, int criticalHitRate, int dodgeRate, int attackSpeed,int exp,int counterattackRate) {
        super(name, health, energy, atk, def, criticalHitRate, dodgeRate, attackSpeed,counterattackRate);
        this.id=id;
        this.exp=exp;
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
}
