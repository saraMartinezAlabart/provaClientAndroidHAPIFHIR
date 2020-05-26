package cat.saramtzalabart.tfg.myclientandroid.Service;

import android.util.Log;

import org.hl7.fhir.dstu3.model.Bundle;
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

    //private String DNI;
    //private IdType id;

    public ApiRESThapi() {
        //this.client = MyContextFhir.getCtx().newRestfulGenericClient(Constants.serverBase);
    }

    /*public IGenericClient getClient() {
        return client;
    }

    public IdType getId() {
        return id;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }*/
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
        //patient.setId(dni);
        patient.addIdentifier().setType(codeableConcept);
        patient.addIdentifier().setValue(dni);
        patient.addName().setFamily(surname).addGiven(name);
        patient.setGender(ag);
        patient.setBirthDate(bd);

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(patient)
                .prettyPrint()
                .encodedJson()
                .execute();

        IdType patientId = (IdType) outcome.getId();
        return patientId;
    }

    //TODO Call this methode frome Fragment Sign IN
   /* public boolean patientExist(String dni){
        IBaseBundle response = client.search()
                .forResource(Patient.class)
                //TODO comprovar que code és correcte per obtenir el identificador del DNI del patient
                .where(Patient.IDENTIFIER.exactly().code(dni))
                .returnBundle(IBaseBundle.class)
                .execute();

        Log.println(Log.INFO, "S11", "This patient already exist: " + response.isEmpty());
        if (response.isEmpty()){return false;}
        else{this.DNI = dni; return true;}
    }

    //TODO Call this methode frome Fragment Settings
    public Patient getMyPatient (String dni){

           Patient patient = client.read()
                .resource(Patient.class)
                .withUrl(Constants.serverBase + "Patient?identifier=" + dni)
                .execute();

        return patient;
    }

    public Bundle getMyBundle (String dni){
        Bundle patient = client.search()
                .forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly().code(dni))
                .returnBundle(Bundle.class)
                .execute();
        return patient;
    }*/






}
