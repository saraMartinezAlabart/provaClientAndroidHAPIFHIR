package cat.saramtzalabart.tfg.clientandroid.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;

import org.hl7.fhir.dstu3.model.Patient;

import cat.saramtzalabart.tfg.myclientandroid.R;
import cat.saramtzalabart.tfg.myclientandroid.Service.ApiRESThapi;

public class SignUpFragment extends Fragment {

    private EditText addName;
    private EditText addSurname;
    private Spinner spinnerGender;
    private EditText addBirthDate;
    private EditText addDNI;
    private Button btnFinish;
    private ApiRESThapi apiRESThapi;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        apiRESThapi = new ApiRESThapi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        addName = view.findViewById(R.id.addPatientName);
        addSurname = view.findViewById(R.id.addPatientSurname);
        spinnerGender = view.findViewById(R.id.addGender);
        addBirthDate = view.findViewById(R.id.addBirthDate);
        addDNI = view.findViewById(R.id.addDNI);

        //SPINNERS
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(
                getContext(),
                R.array.gender_array,
                android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);


        btnFinish= view.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameValue = addName.getText().toString();
                String surnameValue = addSurname.getText().toString();
                String genderValue = spinnerGender.getSelectedItem().toString();
                String birthDateValue = addBirthDate.getText().toString();
                String dniValue = addDNI.getText().toString();


                if (nameValue.equals("") || surnameValue.equals("") || birthDateValue.equals("") || dniValue.equals("") || genderValue.equals("") ||
                        birthDateValue.equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpFragment.this.getContext());
                    builder.setMessage("Some empty text or not correct!");
                    builder.create().show();

                } else {
                   // TODO Create the patient resource in the FHIR server
                   String result = apiRESThapi.createPatient(nameValue, surnameValue, genderValue, birthDateValue, dniValue).getValue();
                    Log.println(Log.INFO, "S11", "ID patient created: " + result);
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpFragment.this.getContext());
                    builder.setMessage("ID patient created:" + result);
                    builder.create().show();

                    /*Patient patient = new Patient();
                    patient = apiRESThapi.getMyPatient("2456789S");
                    name = patient.ge*/
                    /*org.hl7.fhir.dstu3.model.Bundle bundle = new org.hl7.fhir.dstu3.model.Bundle();
                    bundle =  apiRESThapi.getMyBundle("2456789S");
                    bundle.getE*/
                }
            }
        });
        return view;
    }
}

