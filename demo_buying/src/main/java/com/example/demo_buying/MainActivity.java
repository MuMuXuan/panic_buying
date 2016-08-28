package com.example.demo_buying;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> mTabContents = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<String> mDatas = Arrays.asList("抢购1", "抢购2", "抢购3", "抢购4",
            "抢购5", "抢购6", "抢购7", "抢购8", "抢购9");
    private List<String> mBuyingDatas = Arrays.asList("22:00#已开抢", "0:00#已开抢",
            "02:00#抢购进行中", "04:00#即将开场","05:00#即将开场", "07:00#即将开场",
            "09:00#即将开场", "11:00#即将开场", "12:00#即将开场");
    private BuyingPagerIndicator mBuyingPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        mViewPager.setAdapter(mAdapter);
        //设置关联的ViewPager
        mBuyingPagerIndicator.setViewPager(mViewPager);
        mBuyingPagerIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this, "设置监听器：onPageSelected: "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //Test  如果直接设置viewPager去setCurrentItem，会导致显示错误，因为该控件内部还没来得及布局完毕
        //所以需要添加layout监听，layout完毕后再来设置
        mBuyingPagerIndicator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT>16){
                    mBuyingPagerIndicator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mBuyingPagerIndicator.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mViewPager.setCurrentItem(3);
            }
        });
    }

    private void initDatas()
    {

        for (String data : mDatas)
        {
            SimpleFragment fragment = SimpleFragment.newInstance(data);
            mTabContents.add(fragment);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTabContents.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mBuyingDatas.get(position);
            }
        };
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_vp);
        mBuyingPagerIndicator = (BuyingPagerIndicator) findViewById(R.id.buying_indicator);
    }
}
