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

import game.xh.indie.com.jianghu.dao.FightScene;
import game.xh.indie.com.jianghu.dao.Role;
import game.xh.indie.com.jianghu.dao.Skill;

public class Jianghu extends Application {
    private List<FightScene> lFscene;
    private List<Skill> lSkill;
    private List lvl_exp;
    private Role role;

    //初始化配置，活动场景、技能
    public void init(){
        AssetManager assetManager = this.getAssets();
        XmlPullParser xmlParser= Xml.newPullParser();
        InputStream is = null;
        //从XML配置中读取活动场景
        //初始化活动场景列表
        lFscene=new ArrayList<FightScene>();
        getSceneData(is,assetManager,xmlParser,"fight_scene.xml");

    }

    //从配置文件中读取活动场景数据
    public void getSceneData(InputStream is,AssetManager am,XmlPullParser xpp,String fileName){
        try {
            //打开活动场景配置文件
            is = am.open(fileName);
            xpp.setInput(is,"utf-8");
            int eventType=xpp.getEventType();
            //定义即将添加到活动场景列表中的活动场景
            FightScene fightScene=null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //从io流中读取到活动场景开始的标签
                        if(xpp.getName().equals("fight_scene")){
                            //初始化活动场景
                            fightScene=new FightScene();
                            fightScene.setId(Integer.parseInt(xpp.getAttributeValue(0)));
                        }else if(xpp.getName().equals("name")){
                            fightScene.setName(xpp.nextText());
                        }else if(xpp.getName().equals("remark")){
                            fightScene.setRemark(xpp.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //从io流中读取到活动场景结束的标签，将赋值后的活动场景添加到列表中
                        if(xpp.getName().equals("fight_scene")){
                            lFscene.add(fightScene);
                        }
                        break;
                }
                eventType=xpp.next();
            }
            is.close();
        }catch(IOException e){
            Log.i("InitError","读取场景文件失败⊙﹏⊙∥∣°");
        } catch (XmlPullParserException e) {
            Log.i("InitError","解析场景文件失败⊙﹏⊙∥∣°");
        }catch (Exception e){
            Log.i("InitError","读取数据失败，请检查场景配置⊙﹏⊙∥∣°");
        }
    }

    public List<FightScene> getlFscene() {
        return lFscene;
    }

    public List<Skill> getlSkill() {
        return lSkill;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
