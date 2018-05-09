package ihm.si3.polytech.projetnote;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivated extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity2);
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
    }
}
