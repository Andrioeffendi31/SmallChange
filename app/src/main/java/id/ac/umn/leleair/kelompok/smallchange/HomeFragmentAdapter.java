package id.ac.umn.leleair.kelompok.smallchange;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeFragmentAdapter extends FragmentStateAdapter {
    public HomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Income();
            case 1:
                return new UserPanel();
            case 3:
                return new History();
            case 4:
                return new Outcome();
        }
        return new Dashboard();
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
