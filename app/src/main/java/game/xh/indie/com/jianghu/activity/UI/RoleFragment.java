package game.xh.indie.com.jianghu.activity.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import game.xh.indie.com.jianghu.Jianghu;
import game.xh.indie.com.jianghu.R;
import game.xh.indie.com.jianghu.activity.widget.ImageViewWithIndex;
import game.xh.indie.com.jianghu.dao.Item;
import game.xh.indie.com.jianghu.dao.Role;

public class RoleFragment extends Fragment {
    private Role role;
    private int itemCloumns = 4;
    private View viewFragment;
    private ImageView weaponView,armorView,skillView;
    private LinearLayout itemLayout;
    private LinearLayout itemLayoutView;
    private List<Item> roleItemList;
    private TextView rolename, exp, health, energy, atk, def, atkspeed, dodge, critical, counter, money, weight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.fragment_role, container, false);

        //显示当前人物属性
        role = Jianghu.getRole();
        rolename = (TextView) (viewFragment.findViewById(R.id.t_rolename));
        exp = (TextView) (viewFragment.findViewById(R.id.t_exp_now));
        health = (TextView) (viewFragment.findViewById(R.id.t_health));
        energy = (TextView) (viewFragment.findViewById(R.id.t_energy));
        atk = (TextView) (viewFragment.findViewById(R.id.t_atk));
        def = (TextView) (viewFragment.findViewById(R.id.t_def));
        atkspeed = (TextView) (viewFragment.findViewById(R.id.t_attack_speed));
        dodge = (TextView) (viewFragment.findViewById(R.id.t_dodge));
        critical = (TextView) (viewFragment.findViewById(R.id.t_critical));
        counter = (TextView) (viewFragment.findViewById(R.id.t_counter));
        money = (TextView) (viewFragment.findViewById(R.id.t_money));
        weight = (TextView) (viewFragment.findViewById(R.id.t_weight));

        rolename.setText(role.getName() + "    Lv " + role.getLevel());
        exp.setText(role.getExpNow() + " / " + Jianghu.levelNeed[role.getLevel()]);
        health.setText(role.getMaxHealth() + "");
        energy.setText(role.getMaxEnergy() + "");
        atk.setText(role.getAtk() + "");
        def.setText(role.getDef() + "");
        atkspeed.setText(role.getAttackSpeed() + "");
        dodge.setText(role.getDodgeRate() + "");
        critical.setText(role.getCriticalHitRate() + "");
        counter.setText(role.getCounterattackRate() + "");
        money.setText(role.getMoney() + "");
        weight.setText(role.getWeightNow() + " / " + role.getMaxWeight());

        roleItemList = role.getlItem();
        itemLayout = (LinearLayout) viewFragment.findViewById(R.id.layout_item_list);
        itemLayoutView= itemListLayout(roleItemList, itemCloumns, getContext());
        if(itemLayoutView != null) itemLayout.addView(itemLayoutView);

        weaponView=(ImageView)viewFragment.findViewById(R.id.iv_weapon);
        weaponView.setOnClickListener(unWearEquipClickListener);
        if(role.getWeaponWear()!=null){
            weaponView.setImageResource(getResources().getIdentifier(role.getWeaponWear().getPic(), "drawable", getContext().getPackageName()));
        }
        armorView = (ImageView) viewFragment.findViewById(R.id.iv_armor);
        armorView.setOnClickListener(unWearEquipClickListener);
        if(role.getArmorWear()!=null) {
            armorView.setImageResource(getResources().getIdentifier(role.getArmorWear().getPic(), "drawable", getContext().getPackageName()));
        }
        //skillView=(ImageView)viewFragment.findViewById(R.id.iv_skill);

        return viewFragment;
    }

    /**
     * @param itemList 道具列表
     * @return 返回自定义的场景布局
     */
    private LinearLayout itemListLayout(List<Item> itemList, int clomuns, Context context) {
        int sceneNums = itemList.size();
        if(itemList.size()==0)  return null;
        LinearLayout sly = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置为垂直布局
        sly.setOrientation(LinearLayout.VERTICAL);
        sly.setLayoutParams(lp);

        int rows = sceneNums / clomuns;
        int surplus = sceneNums % clomuns;
        int i;
        for (i = 0; i < rows; i++) {
            List<Item> lfs_rows = new ArrayList<Item>();
            for (int j = 0; j < clomuns; j++) {
                lfs_rows.add(itemList.get(i * clomuns + j));
            }
            sly.addView(rowsLayout(lfs_rows, i, context));
        }
        if (surplus != 0) {
            //创建最后剩下的，不足一行的布局
            List<Item> lfs_surplus = new ArrayList<Item>();
            for (int k = 0; k < surplus; k++) {
                lfs_surplus.add(itemList.get(i * clomuns + k));
            }
            sly.addView(rowsLayout(lfs_surplus, i , context));
        }
        return sly;
    }

    /**
     * @param clomunsItem 单行显示的道具
     * @return 返回单行显示的LinearLayout
     */
    private LinearLayout rowsLayout(List<Item> clomunsItem, int xIndex, Context context) {
        int clomuns = clomunsItem.size();
        LinearLayout rl = new LinearLayout(context);
        //设置LayoutParams
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置为水平布局
        rl.setOrientation(LinearLayout.HORIZONTAL);
        rl.setLayoutParams(lp);
        //循环添加道具布局
        for (int j = 0; j < clomuns; j++) {
            LinearLayout itemLinear = new LinearLayout(context);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //设置为垂直布局
            itemLinear.setOrientation(LinearLayout.VERTICAL);
            itemLinear.setLayoutParams(lp2);

            ImageViewWithIndex imageView = new ImageViewWithIndex(context);
            TextView textView = new TextView(context);
            int drawableId = getResources().getIdentifier(clomunsItem.get(j).getPic(), "drawable", context.getPackageName());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80,
                    80);//两个400分别为添加图片的大小
            imageView.setLayoutParams(params);
            imageView.setImageResource(drawableId);
            imageView.setxIndex(xIndex);
            imageView.setyIndex(j);
            imageView.setOnClickListener(itemClickListener);
            textView.setText(clomunsItem.get(j).getName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(12);
            itemLinear.addView(imageView);
            itemLinear.addView(textView);
            rl.addView(itemLinear);
        }
        return rl;
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int index = ((ImageViewWithIndex) v).getxIndex() * itemCloumns + ((ImageViewWithIndex) v).getyIndex();
            final Item itemClicked = role.getlItem().get(index);
            AlertDialog.Builder normalDialog =  new AlertDialog.Builder(getContext());
            normalDialog.setTitle(itemClicked.getName());
            normalDialog.setMessage("您想要做什么呢?"+itemClicked.getName());
            String buttonName;
            if(itemClicked.getType()==1||itemClicked.getType()==2 ){
                buttonName="装备";
            }else if(itemClicked.getType()==3){
                buttonName="修炼";
            }else{
                buttonName="确定";
            }
            normalDialog.setPositiveButton(buttonName,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int itemImgId = getResources().getIdentifier(itemClicked.getPic(), "drawable", getContext().getPackageName());
                            switch(itemClicked.getType()){
                                case 1:             //武器
                                    role.wearEquip(index);
                                    itemLayout.removeView(itemLayoutView);
                                    itemLayoutView= itemListLayout(roleItemList, itemCloumns, getContext());
                                    if(itemLayoutView != null) itemLayout.addView(itemLayoutView);
                                    weaponView.setImageResource(itemImgId);
                                    atk.setText(role.getAtk() + "");
                                    weight.setText(role.getWeightNow()+"");
                                    break;
                                case 2:             //防具
                                    role.wearEquip(index);
                                    itemLayout.removeView(itemLayoutView);
                                    itemLayoutView= itemListLayout(roleItemList, itemCloumns, getContext());
                                    if(itemLayoutView != null) itemLayout.addView(itemLayoutView);
                                    armorView.setImageResource(itemImgId);
                                    def.setText(role.getDef() + "");
                                    weight.setText(role.getWeightNow()+"");
                                    break;
                                case 3: //秘籍
                                    break;
                            }
                        }
                    });
            normalDialog.setNegativeButton("关闭",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            // 显示
            normalDialog.show();
        }
    };

    private View.OnClickListener unWearEquipClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Item unWearItem = null;
            if(v.getId()==R.id.iv_weapon){
                unWearItem=role.getWeaponWear();
                if(unWearItem == null) return;
                weaponView.setImageResource(R.drawable.role_weapon);
            }else if (v.getId()==R.id.iv_armor){
                unWearItem=role.getArmorWear();
                if(unWearItem == null) return;
                armorView.setImageResource(R.drawable.role_armor);
            }
            role.unWearEquip(unWearItem);
            itemLayout.removeView(itemLayoutView);
            itemLayoutView= itemListLayout(roleItemList, itemCloumns, getContext());
            if(itemLayoutView != null) itemLayout.addView(itemLayoutView);
            atk.setText(role.getAtk() + "");
            def.setText(role.getDef() + "");
            weight.setText(role.getWeightNow()+"");
        }
    };
}
