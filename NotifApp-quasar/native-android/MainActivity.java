package pt.demo.notif;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Os plugins locais têm de ser registados antes do super.onCreate
        registerPlugin(BackgroundTaskPlugin.class);
        super.onCreate(savedInstanceState);
    }
}
