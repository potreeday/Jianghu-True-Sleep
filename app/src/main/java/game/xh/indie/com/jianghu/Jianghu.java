package game.xh.indie.com.jianghu;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import game.xh.indie.com.jianghu.dao.Enemy;
import game.xh.indie.com.jianghu.dao.FightScene;
import game.xh.indie.com.jianghu.dao.Item;
import game.xh.indie.com.jianghu.dao.Role;
import game.xh.indie.com.jianghu.dao.Skill;

public class Jianghu extends Application {
    final public static int[] levelNeed = {100, 250, 450, 700, 1050, 1500, 2050};      //主角升级所需经验

    private static List<FightScene> lFscene;    //存储游戏场景配置
    private static List<List<Enemy>> lEnemy;    //存储怪物模板配置
    private static List<List<Enemy>> lBossEnemy;    //存储BOSS怪物模板配置
    private static List<Item> lItem;            //存储道具模板配置
    private static List<Skill> lSkill;          //存储技能模板配置

    private static Role role;

    //初始化配置，活动场景、怪物、道具
    public void init(){
        AssetManager assetManager = getAssets();
        XmlPullParser xmlParser= Xml.newPullParser();
        InputStream is = null;
        //从XML配置中读取游戏场景
        lFscene=new ArrayList<FightScene>();
        getSceneData(is,assetManager,xmlParser,"fight_scene.xml");
        //给每个游戏场景，创建专门的怪物和BOSS怪物列表
        lEnemy = new ArrayList<List<Enemy>>();
        lBossEnemy = new ArrayList<List<Enemy>>();
        for (int i=0;i<lFscene.size();i++){
            lEnemy.add(new ArrayList<Enemy>());
            lBossEnemy.add(new ArrayList<Enemy>());
        }
        getEnemyData(is,assetManager,xmlParser,"enemy.xml");
        //创建物品的模板列表
        lItem = new ArrayList<Item>();
        getItemData(is,assetManager,xmlParser,"item.xml");
    }

    /**
     * 从xml文件中读取道具数据，并添加到道具模板列表中
     * @param fileName 文件名称
     */
    private void getItemData(InputStream inputStream, AssetManager am, XmlPullParser xpp, String fileName) {
        try {
            //打开游戏场景配置文件
            inputStream = am.open(fileName);
            xpp.setInput(inputStream,"utf-8");
            int eventType=xpp.getEventType();
            //定义即将添加的道具模板
            Item item=null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //从io流中读取到道具模板开始的标签
                        if(xpp.getName().equals("item")) {
                            //初始化道具模板
                            item = new Item();
                        }else if(xpp.getName().equals("id")){
                            item.setId(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("name")){
                            item.setName(xpp.nextText());
                        }else if(xpp.getName().equals("type")){
                            item.setType(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("price")){
                            item.setPrice(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("weight")){
                            item.setWeight(Float.parseFloat(xpp.nextText()));
                        }else if(xpp.getName().equals("dmg")){
                            item.setDmg(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("def")){
                            item.setDef(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("skillCanLearn")){
                            item.setSkillCanLearn(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("healthCanAdd")){
                            item.setHealthCanAdd(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("energyCanAdd")){
                            item.setEnergyCanAdd(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("pic")){
                            item.setPic(xpp.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //从io流中读取到道具模板结束的标签，将赋值后的道具模板添加到列表中
                        if(xpp.getName().equals("item")){
                            lItem.add(item);
                        }
                        break;
                }
                eventType=xpp.next();
            }
            inputStream.close();
        }catch(IOException e){
            Log.i("InitError","读取道具配置失败⊙﹏⊙∥∣°");
        } catch (XmlPullParserException e) {
            Log.i("InitError","解析道具配置失败⊙﹏⊙∥∣°");
        }catch (Exception e){
            Log.i("InitError","读取数据失败，请检查道具配置⊙﹏⊙∥∣°");
        }
    }

    /**
     * 从xml文件中读取游戏场景数据，并添加到游戏场景列表中
     * @param fileName 文件名称
     */
    private void getSceneData(InputStream inputStream,AssetManager am,XmlPullParser xpp,String fileName){
        try {
            //打开游戏场景配置文件
            inputStream = am.open(fileName);
            xpp.setInput(inputStream,"utf-8");
            int eventType=xpp.getEventType();
            //定义即将添加到游戏场景列表中的游戏场景
            FightScene fightScene=null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //从io流中读取到游戏场景开始的标签
                        if(xpp.getName().equals("fight_scene")) {
                            //初始化游戏场景
                            fightScene = new FightScene();
                        }else if(xpp.getName().equals("id")){
                            fightScene.setId(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("name")){
                            fightScene.setName(xpp.nextText());
                        }else if(xpp.getName().equals("remark")){
                            fightScene.setRemark(xpp.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //从io流中读取到游戏场景结束的标签，将赋值后的游戏场景添加到列表中
                        if(xpp.getName().equals("fight_scene")){
                            lFscene.add(fightScene);
                        }
                        break;
                }
                eventType=xpp.next();
            }
            inputStream.close();
        }catch(IOException e){
            Log.i("InitError","读取场景配置失败⊙﹏⊙∥∣°");
        } catch (XmlPullParserException e) {
            Log.i("InitError","解析场景配置失败⊙﹏⊙∥∣°");
        }catch (Exception e){
            Log.i("InitError","读取数据失败，请检查场景配置⊙﹏⊙∥∣°");
        }
    }

    /**
     * 从xml文件中读取场景中的怪物数据，并添加到各场景的怪物列表中
     * @param fileName 文件名称
     */
    private void getEnemyData(InputStream inputStream,AssetManager am,XmlPullParser xpp,String fileName) {
        try {
            //打开怪物配置文件
            inputStream = am.open(fileName);
            xpp.setInput(inputStream,"utf-8");
            int eventType=xpp.getEventType();
            //定义即将添加到怪物列表中的怪物
            Enemy enemy=null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //从io流中读取到活动场景开始的标签
                        if(xpp.getName().equals("enemy")) {
                            //初始化怪物
                            enemy = new Enemy();
                            //fightScene.setId(Integer.parseInt(xpp.getAttributeValue(0)));
                        }else if(xpp.getName().equals("id")){
                            enemy.setId(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("sceneBelong")){
                            enemy.setSceneBelong(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("name")){
                            enemy.setName(xpp.nextText());
                        }else if(xpp.getName().equals("health")){
                            enemy.setHealth(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("energy")){
                            enemy.setEnergy(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("atk")){
                            enemy.setAtk(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("def")){
                            enemy.setDef(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("criticalHitRate")){
                            enemy.setCriticalHitRate(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("dodgeRate")){
                            enemy.setDodgeRate(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("attackSpeed")){
                            enemy.setAttackSpeed(Float.parseFloat(xpp.nextText()));
                        }else if(xpp.getName().equals("counterattackRate")){
                            enemy.setCounterattackRate(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("exp")){
                            enemy.setExp(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("dropItem")){
                            enemy.setDropItem(xpp.nextText());
                        }else if(xpp.getName().equals("isBoss")){
                            enemy.setIsBoss(Integer.parseInt(xpp.nextText()));
                        }else if(xpp.getName().equals("skill")){
                            enemy.setSkill(Integer.parseInt(xpp.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //从io流中读取到怪物结束的标签，将赋值后的怪物添加到对应场景的怪物列表中
                        if(xpp.getName().equals("enemy")){
                            if(enemy.getIsBoss() == 1){
                                lBossEnemy.get(enemy.getSceneBelong()-1).add(enemy);
                            }else{
                                lEnemy.get(enemy.getSceneBelong()-1).add(enemy);
                            }
                        }
                        break;
                }
                eventType=xpp.next();
            }
            inputStream.close();
        }catch(IOException e){
            Log.i("InitError","读取怪物配置失败⊙﹏⊙∥∣°");
        } catch (XmlPullParserException e) {
            Log.i("InitError","解析怪物配置失败⊙﹏⊙∥∣°");
        }catch (Exception e){
            Log.i("InitError","读取数据失败，请检查怪物配置⊙﹏⊙∥∣°");
        }
    }

    public static List<FightScene> getlFscene() {
        return lFscene;
    }

    public static List<List<Enemy>> getlEnemy() {
        return lEnemy;
    }

    public static List<List<Enemy>> getlBossEnemy() {
        return lBossEnemy;
    }

    public static List<Item> getlItem() {
        return lItem;
    }

    public static List<Skill> getlSkill() {
        return lSkill;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static Role getRole() {
        return role;
    }
}
