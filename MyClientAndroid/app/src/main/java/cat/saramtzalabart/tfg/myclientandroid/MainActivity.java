package cat.saramtzalabart.tfg.myclientandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.util.Date;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.FEMALE;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.MALE;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.OTHER;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.UNKNOWN;

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

        //textViewResult.setText(getPatient(client, "32222222S").toString());
        textViewResult.setText("Got ID: " + createPatient(client, "Sara", "Martínez Alabart", "female", "1998-10-11", "32222222S").getValue());
    }

    private IBaseBundle getPatient(IGenericClient client, String dni){
        IBaseBundle response = client.search()
                .forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly().code(dni))
                .returnBundle(IBaseBundle.class)
                .execute();
        return response;
    }

    private IdType createPatient(IGenericClient client, String name, String surname, String gender, String birthDate, String dni){
        //String resourceType = "Patient";
        String code = "DNI";
        String text = "Document Nacional d'Identitat";
        Enumerations.AdministrativeGender ag= UNKNOWN;
        if (gender.equals("female")){ ag =  FEMALE; }
        else if (gender.equals("male")){ ag = MALE; }
        else { ag = OTHER; }

        Coding coding = new Coding().setCode(code);

        CodeableConcept codeableConcept = new CodeableConcept()
                .addCoding(coding)
                .setText(text);

        Patient patient = new Patient();
        patient.addIdentifier().setType(codeableConcept);
        patient.addIdentifier().setValue(dni);
        patient.addName().setFamily(surname).addGiven(name);
        patient.setGender(ag);
        //TODO Comprovar que la versió d'api que estic utilitzant sigui la correcta
        patient.setBirthDate(new Date(birthDate));

        MethodOutcome outcome = client.create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();

        IdType patientId = (IdType) outcome.getId();
        return patientId;
    }
}
