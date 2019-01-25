package game.xh.indie.com.jianghu.dao;

public class Item {
    private int id;
    private String name;
    private int type;  //物品类型：0 默认 1武器 2防具 3秘籍 4丹药
    //private int priceBuy;
    private int price;
    private float weight;

    private int dmg;    //武器的攻击力
    private int def;    //防具的防御力
    private int skillCanLearn;  //可修炼的招式
    private int healthCanAdd;  //服用丹药能增加的生命值上限
    private int energyCanAdd;  //服用丹药能增加的内力上限

    public Item(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int priceSell) {
        this.price = price;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }


    public int getSkillCanLearn() {
        return skillCanLearn;
    }

    public void setSkillCanLearn(int skillCanLearn) {
        this.skillCanLearn = skillCanLearn;
    }

    public int getHealthCanAdd() {
        return healthCanAdd;
    }

    public void setHealthCanAdd(int healthCanAdd) {
        this.healthCanAdd = healthCanAdd;
    }

    public int getEnergyCanAdd() {
        return energyCanAdd;
    }

    public void setEnergyCanAdd(int energyCanAdd) {
        this.energyCanAdd = energyCanAdd;
    }
}
