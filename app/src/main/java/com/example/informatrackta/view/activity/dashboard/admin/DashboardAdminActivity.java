package com.example.informatrackta.view.activity.dashboard.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.informatrackta.R;
import com.example.informatrackta.view.activity.dashboard.admin.fragments.ActivityFragment;
import com.example.informatrackta.view.activity.dashboard.admin.fragments.DashboardFragment;
import com.example.informatrackta.view.activity.dashboard.admin.fragments.ScheduleFragment;

public class DashboardAdminActivity extends AppCompatActivity {
    LinearLayout navDashboard, navActivity, navSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        navDashboard = findViewById(R.id.nav_dashboard);
        navActivity = findViewById(R.id.nav_activity);
        navSchedule = findViewById(R.id.nav_schedule);

        // Event klik navigasi
        navDashboard.setOnClickListener(v -> {
            resetBottomNav();
            ((ImageView) v.findViewById(R.id.icon_dashboard)).setImageResource(R.drawable.ic_home_filled);
            ((TextView) v.findViewById(R.id.text_dashboard)).setTextColor(ContextCompat.getColor(this, R.color.dark_blue));
            ((TextView) v.findViewById(R.id.text_dashboard)).setTypeface(ResourcesCompat.getFont(this, R.font.helvetica_bold));
            loadFragment(new DashboardFragment());
        });

        navActivity.setOnClickListener(v -> {
            resetBottomNav();
            ((ImageView) v.findViewById(R.id.icon_activity)).setImageResource(R.drawable.ic_activity_filled);
            ((TextView) v.findViewById(R.id.text_activity)).setTextColor(ContextCompat.getColor(this, R.color.dark_blue));
            ((TextView) v.findViewById(R.id.text_activity)).setTypeface(ResourcesCompat.getFont(this, R.font.helvetica_bold));
            loadFragment(new ActivityFragment());
        });

        navSchedule.setOnClickListener(v -> {
            resetBottomNav();
            ((ImageView) v.findViewById(R.id.icon_schedule)).setImageResource(R.drawable.ic_schedule_filled);
            ((TextView) v.findViewById(R.id.text_schedule)).setTextColor(ContextCompat.getColor(this, R.color.dark_blue));
            ((TextView) v.findViewById(R.id.text_schedule)).setTypeface(ResourcesCompat.getFont(this, R.font.helvetica_bold));
            loadFragment(new ScheduleFragment());
        });

        // Tampilkan dashboard saat pertama kali
        navDashboard.performClick();
    }

    private void loadFragment(Fragment fragment) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void resetBottomNav() {
        ((ImageView) findViewById(R.id.nav_dashboard).findViewById(R.id.icon_dashboard)).setImageResource(R.drawable.ic_home);
        ((TextView) findViewById(R.id.nav_dashboard).findViewById(R.id.text_dashboard)).setTextColor(ContextCompat.getColor(this, R.color.sec_blue));
        ((TextView) findViewById(R.id.nav_dashboard).findViewById(R.id.text_dashboard)).setTypeface(ResourcesCompat.getFont(this, R.font.helvetica));

        ((ImageView) findViewById(R.id.nav_activity).findViewById(R.id.icon_activity)).setImageResource(R.drawable.ic_activity);
        ((TextView) findViewById(R.id.nav_activity).findViewById(R.id.text_activity)).setTextColor(ContextCompat.getColor(this, R.color.sec_blue));
        ((TextView) findViewById(R.id.nav_activity).findViewById(R.id.text_activity)).setTypeface(ResourcesCompat.getFont(this, R.font.helvetica));

        ((ImageView) findViewById(R.id.nav_schedule).findViewById(R.id.icon_schedule)).setImageResource(R.drawable.ic_schedule);
        ((TextView) findViewById(R.id.nav_schedule).findViewById(R.id.text_schedule)).setTextColor(ContextCompat.getColor(this, R.color.sec_blue));
        ((TextView) findViewById(R.id.nav_schedule).findViewById(R.id.text_schedule)).setTypeface(ResourcesCompat.getFont(this, R.font.helvetica));
    }
}