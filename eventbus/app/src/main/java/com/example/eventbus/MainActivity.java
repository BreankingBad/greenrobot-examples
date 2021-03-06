package com.example.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.eventbus.library.BlankBaseLibraryFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.containerFragment, new BlankFragment());
            ft.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(BlankBaseLibraryFragment.SampleEvent event) {
        String className = this.getClass().getSimpleName();
        String message = "#handleEvent: called for " + event.getClass().getSimpleName();
        Toast.makeText(this, className + message, Toast.LENGTH_SHORT).show();
        Log.d(className, message);

        // prevent event from re-delivering, like when leaving and coming back to app
        EventBus.getDefault().removeStickyEvent(event);
    }

    public void onPostButtonClick(View view) {
        EventBus.getDefault().post(new BlankBaseLibraryFragment.SampleEvent());
    }

    public void onLaunchButtonClick(View view) {
        startActivity(new Intent(this, EmptyActivity.class));
    }


}
