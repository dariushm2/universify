package com.dariushm2.universify.view.image;

import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dariushm2.universify.App;
import com.dariushm2.universify.R;
import com.dariushm2.universify.repository.GalleryPresenter;

public class ImageActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ImageViewPagerAdapter(getSupportFragmentManager()));

        if (getIntent().getExtras() != null) {
            int position = getIntent().getExtras().getInt("position");
            viewPager.setCurrentItem(position);
        }

    }


    private class ImageViewPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        ImageViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }

        @Override
        public int getCount() {
            if (GalleryPresenter.getInstance().getGalleryModels() != null)
                return GalleryPresenter.getInstance().getGalleryModels().size();
            return 1000;
        }

        @Override
        @NonNull
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(position);
        }

        @Override
        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

    }

}
