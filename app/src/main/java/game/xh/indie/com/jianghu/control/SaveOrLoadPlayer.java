package game.xh.indie.com.jianghu.control;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.xh.indie.com.jianghu.Jianghu;
import game.xh.indie.com.jianghu.dao.Item;
import game.xh.indie.com.jianghu.dao.Role;
import game.xh.indie.com.jianghu.util.MyEncrypt;

import static android.content.Context.MODE_PRIVATE;


public class SaveOrLoadPlayer {
    /**
     * 将要存档的人物数据保存到文件中
     *
     * @param role 要存档的角色对象
     */
    public static void savePlayer(Role role, Context context) throws IOException {
        //获取主角携带的道具列表：private List<Item> lItem
        List<Item> lItemRole = role.getlItem();
        StringBuffer lItemStr = new StringBuffer();
        for (int i = 1; i <= lItemRole.size(); i++) {
            if (i == lItemRole.size()) {
                lItemStr.append(lItemRole.get(i - 1).getId());
            } else {
                lItemStr.append(lItemRole.get(i - 1).getId() + "|");
            }
        }
        Log.d("TNGInfo", "角色道具列表： " + lItemStr);
        //获取主角装备的武器和防具
        String weaponWear = "", armorWear = "";
        if (role.getWeaponWear() != null) weaponWear = "" + role.getWeaponWear().getId();
        if (role.getArmorWear() != null) armorWear = "" + role.getArmorWear().getId();
        //获取要存档的人物数据
        String str = "name=" + role.getName()         // private String name;
                + "&atk=" + role.getAtk()         //private int atk;    //攻击值
                + "&def=" + role.getDef()         //private int def;    //防御值
                + "&maxHealth=" + role.getMaxHealth()         //private int maxHealth;    //主角的最大生命值
                + "&maxEnergy=" + role.getMaxEnergy()         //private int maxEnergy;    //主角的最大内力值
                + "&criticalHitRate=" + role.getCriticalHitRate()         //private int criticalHitRate;    //会心一击率
                + "&dodgeRate=" + role.getDodgeRate()         //private int dodgeRate;    //闪避率
                + "&attackSpeed=" + role.getAttackSpeed()         //private float attackSpeed;    //攻击速度
                + "&fsNow=" + role.getFsNow()        //private int fsNowID;        //主角所在场景名称
                + "&level=" + role.getLevel()
                + "&exp=" + role.getExpNow()
                + "&counterattackRate=" + role.getCounterattackRate()        //private int counterattackRate; //反击率
                + "&maxWeight=" + role.getMaxWeight()        //private int maxWeight;      //主角的最大负重值
                + "&weightNow=" + role.getWeightNow()        //private int weightNow;          //主角当前负重（计算装备的）
                + "&money=" + role.getMoney()        //private int money;              //主角当前银两
                + "&itemPackage=" + lItemStr
                + "&weaponWear=" + weaponWear        //private Item weaponWear;      //主角穿戴的武器
                + "&armorWear=" + armorWear;        //private Item armorWear;        //主角穿戴的防具
        /*
        //private SkillEnergy skillEnergyUsing;       //主角正在运行的内功心法
        //private List<Book> bookLearning;      //主角正在修炼的武功秘籍
        //private Skill skillUsing;       //主角正在使用的招式*/

        //加密数据
        String strEncrypt = MyEncrypt.getEncryptStr(str);
        //将加密后的数据写入存档文件中
        FileOutputStream fos = context.openFileOutput("SaveFile.dat", MODE_PRIVATE);
        byte[] bytes = strEncrypt.getBytes();
        fos.write(bytes);
        fos.close();

        //以下为调试内容
        Log.d("TNGInfo", "存档数据为：" + str);
        loadPlayer(context);
    }

    /**
     * 从存档文件中读取人物数据
     */
    public static Role loadPlayer(Context context) throws IOException {
        //读取存档的人物数据
        FileInputStream fis = context.openFileInput("SaveFile.dat");
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();
        //解密数据
        String strOld = new String(bytes);
        Log.d("TNGInfo", "读档数据为：" + MyEncrypt.getDecryptStr(strOld));
        String[] datas = MyEncrypt.getDecryptStr(strOld).split("&");
        //从解密出来的字符串中，获取主角的各项属性及状态
        Role role = new Role();
        try {
            //读取道具栏的道具列表
            String[] itemList = datas[15].split("=")[1].split("\\|");
            List<Item> lItemList = new ArrayList<>();
            for (String str : itemList) {
                int itemId = Integer.parseInt(str);
                for(Item item : Jianghu.getlItem()){
                    if(item.getId()== itemId){
                        Item willdrop = new Item();
                        willdrop.setId(item.getId());
                        willdrop.setDef(item.getDef());
                        willdrop.setDmg(item.getDmg());
                        willdrop.setEnergyCanAdd(item.getEnergyCanAdd());
                        willdrop.setHealthCanAdd(item.getHealthCanAdd());
                        willdrop.setName(item.getName());
                        willdrop.setPrice(item.getPrice());
                        willdrop.setSkillCanLearn(item.getSkillCanLearn());
                        willdrop.setType(item.getType());
                        willdrop.setWeight(item.getWeight());
                        willdrop.setPic(item.getPic());
                        lItemList.add(willdrop);
                        break;
                    }
                }
            }
            role.setlItem(lItemList);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("error", "读取角色道具列表失败");
        }
        //读取其他属性
        role.setName(datas[0].split("=")[1]);
        role.setAtk(Integer.parseInt(datas[1].split("=")[1]));
        role.setDef(Integer.parseInt(datas[2].split("=")[1]));
        role.setMaxHealth(Integer.parseInt(datas[3].split("=")[1]));
        role.setMaxEnergy(Integer.parseInt(datas[4].split("=")[1]));
        role.setCriticalHitRate(Integer.parseInt(datas[5].split("=")[1]));
        role.setDodgeRate(Integer.parseInt(datas[6].split("=")[1]));
        role.setAttackSpeed(Float.parseFloat(datas[7].split("=")[1]));
        role.setFsNow(Integer.parseInt(datas[8].split("=")[1]));
        role.setLevel(Integer.parseInt(datas[9].split("=")[1]));
        role.setExpNow(Integer.parseInt(datas[10].split("=")[1]));
        role.setCounterattackRate(Integer.parseInt(datas[11].split("=")[1]));
        role.setMaxWeight(Integer.parseInt(datas[12].split("=")[1]));
        role.setWeightNow(Float.parseFloat(datas[13].split("=")[1]));
        role.setMoney(Integer.parseInt(datas[14].split("=")[1]));
        //读取穿戴的装备
        Item weaponWear = new Item();
        Item armorWear = new Item();
        try {
            for (Item weapon : Jianghu.getlItem()) {
                if (weapon.getId() == (Integer.parseInt(datas[16].split("=")[1]))) {
                    weaponWear.setId(weapon.getId());
                    weaponWear.setName(weapon.getName());
                    weaponWear.setType(weapon.getType());
                    weaponWear.setPrice(weapon.getPrice());
                    weaponWear.setWeight(weapon.getWeight());
                    weaponWear.setDmg(weapon.getDmg());
                    weaponWear.setDef(weapon.getDef());
                    weaponWear.setPic(weapon.getPic());
                    //weaponWear.setSkillCanLearn(weapon.getSkillCanLearn());
                    //weaponWear.setHealthCanAdd(weapon.getHealthCanAdd());
                    //weaponWear.setEnergyCanAdd(weapon.getEnergyCanAdd());
                    role.setWeaponWear(weaponWear);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e1) {
            Log.e("error", "读取角色武器失败");
        }
        try {
            for (Item armor : Jianghu.getlItem()) {
                if (armor.getId() == (Integer.parseInt(datas[17].split("=")[1]))) {
                    armorWear.setId(armor.getId());
                    armorWear.setName(armor.getName());
                    armorWear.setType(armor.getType());
                    armorWear.setPrice(armor.getPrice());
                    armorWear.setWeight(armor.getWeight());
                    armorWear.setDmg(armor.getDmg());
                    armorWear.setDef(armor.getDef());
                    armorWear.setPic(armor.getPic());
                    //armorWear.setSkillCanLearn(armor.getSkillCanLearn());
                    //armorWear.setHealthCanAdd(armor.getHealthCanAdd());
                    //armorWear.setEnergyCanAdd(armor.getEnergyCanAdd());
                    role.setArmorWear(armorWear);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e2) {
            Log.e("error", "读取角色防具失败");
        }
        return role;
    }

}