package cat.saramtzalabart.tfg.myclientandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import cat.saramtzalabart.tfg.clientandroid.fragments.SignUpFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SignUpFragment startFragment = SignUpFragment.newInstance();
        ft.add(R.id.fragment_sign_up, startFragment);
        ft.commit();

        //textViewResult.setText(apiRESThapi.getPatient("32222222S").toString());

    }
}
