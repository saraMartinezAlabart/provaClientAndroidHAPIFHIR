package cat.saramtzalabart.tfg.myclientandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        FhirContext ctx = FhirContext.forDstu3();
        String serverBase = "http://vws15459.dinaserver.com:8087/baseDstu3/";

        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

        IBaseBundle bundle = client.search().forResource(Patient.class)
                .prettyPrint()
                .encodedJson()
                .execute();

        textViewResult.append(bundle.toString());
    }
}
