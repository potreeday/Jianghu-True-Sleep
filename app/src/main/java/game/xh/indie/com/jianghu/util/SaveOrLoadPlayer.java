package game.xh.indie.com.jianghu.util;

import android.app.Activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import game.xh.indie.com.jianghu.dao.Role;

import static android.content.Context.MODE_PRIVATE;


public class SaveOrLoadPlayer {
    /**
     * 将要存档的人物数据保存到文件中
     * @param role 要存档的角色对象
     */
    public static void savePlayer(Role role, Activity activity) throws IOException {
        //获取要存档的人物数据
        String str="name="+ role.getName()+"&atk="+ role.getAtk()+"&def="+ role.getDef()+"&maxHealth="+ role.getMaxHealth()+"&maxEnergy="+ role.getMaxEnergy()+
                "&criticalHitRate="+ role.getCriticalHitRate()+"&dodgeRate="+ role.getDodgeRate()+"&attackSpeed="+ role.getAttackSpeed()+"&fsNow="+ role.getFsNow() + "&level="+role.getLevel()+"&exp="+role.getExpNow();
        //加密数据
        String strEncrypt=MyEncrypt.getEncryptStr(str);
        //将加密后的数据写入存档文件中
        FileOutputStream fos = activity.openFileOutput("SaveFile.dat", MODE_PRIVATE);
        byte[]  bytes = strEncrypt.getBytes();
        fos.write(bytes);
        fos.close();
        loadPlayer(activity);
    }

    /**
     * 从存档文件中读取人物数据
     */
    public static Role loadPlayer(Activity activity) throws IOException{
        //读取存档的人物数据
        FileInputStream fis = activity.openFileInput("SaveFile.dat");
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();
        //解密数据
        String strOld = new String(bytes);
        //System.out.println("读档数据为："+MyEncrypt.getDecryptStr(strOld));
        String[] datas=MyEncrypt.getDecryptStr(strOld).split("&");
        //从解密出来的字符串中，获取主角的各项属性及状态
        Role role = new Role();
        role.setName(datas[0].split("=")[1]);
        role.setAtk(Integer.parseInt(datas[1].split("=")[1]));
        role.setDef(Integer.parseInt(datas[2].split("=")[1]));
        role.setMaxHealth(Integer.parseInt(datas[3].split("=")[1]));
        role.setMaxEnergy(Integer.parseInt(datas[4].split("=")[1]));
        role.setCriticalHitRate(Integer.parseInt(datas[5].split("=")[1]));
        role.setDodgeRate(Integer.parseInt(datas[6].split("=")[1]));
        role.setAttackSpeed(Integer.parseInt(datas[7].split("=")[1]));
        role.setFsNow(Integer.parseInt(datas[8].split("=")[1]));
        role.setLevel(Integer.parseInt(datas[9].split("=")[1]));
        role.setExpNow(Integer.parseInt(datas[10].split("=")[1]));
        return role;
    }

}