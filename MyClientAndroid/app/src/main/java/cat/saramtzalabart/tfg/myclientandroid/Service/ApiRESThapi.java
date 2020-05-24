package cat.saramtzalabart.tfg.myclientandroid.Service;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.FEMALE;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.MALE;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.OTHER;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.UNKNOWN;

public class ApiRESThapi {
    private IGenericClient client;

    public ApiRESThapi() {
        this.client = MyContextFhir.getCtx().newRestfulGenericClient(Constants.serverBase);
    }

    public IGenericClient getClient() {
        return client;
    }

    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    //TODO Call this methode frome Fragment Sign UP
    public IdType createPatient(String name, String surname, String gender, String birthDate, String dni){
        //String resourceType = "Patient";
        String code = "DNI";
        String text = "Document Nacional d'Identitat";
        Enumerations.AdministrativeGender ag= UNKNOWN;
        if (gender.equals("female")){ ag =  FEMALE; }
        else if (gender.equals("male")){ ag = MALE; }
        else { ag = OTHER; }
        Date bd;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            bd = format.parse(birthDate);
        } catch (ParseException e) {
            return null;
        }

        Coding coding = new Coding().setCode(code);

        CodeableConcept codeableConcept = new CodeableConcept()
                .addCoding(coding)
                .setText(text);

        Patient patient = new Patient();
        patient.addIdentifier().setType(codeableConcept);
        patient.addIdentifier().setValue(dni);
        patient.addName().setFamily(surname).addGiven(name);
        patient.setGender(ag);
        patient.setBirthDate(bd);

        MethodOutcome outcome = client.create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();

        IdType patientId = (IdType) outcome.getId();
        return patientId;
    }

    /*public IBaseBundle getPatient(String dni){
        IBaseBundle response = client.search()
                .forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly().code(dni))
                .returnBundle(IBaseBundle.class)
                .execute();
        return response;
    }*/
}
