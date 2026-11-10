package pt.demo.notif;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(BackgroundTaskPlugin.class);
        super.onCreate(savedInstanceState);
    }
}
