package cat.saramtzalabart.tfg.myclientandroid.Service;


import android.util.Log;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import ca.uhn.fhir.rest.api.MethodOutcome;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.FEMALE;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.MALE;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.OTHER;
import static org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender.UNKNOWN;

public class ApiRESThapi {

    private UserProvider user;
    /*private boolean resultat;
    private IdType idPatient;*/

    public ApiRESThapi() {
        user = new UserProvider();
        /*resultat = false;
        idPatient = new IdType();*/
    }

    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    /*private void setResultat (boolean value){
        user.setResourceCreated(value);
        resultat = value;
    }
    private void setIdPatient(IdType value){
        user.setPatientId(value);
        idPatient = value;
    }*/
    /*
    *
    *
    * @CREATE METHODS PATIENT
    *
    *
    */
    //TODO Call this methode frome Fragment Sign UP
    public void createPatient(String name, String surname, String gender, String birthDate, String dni, final boolean update){
        //String resourceType = "Patient";
        String code = "DNI";
        String text = "Document Nacional d'Identitat";
        Enumerations.AdministrativeGender ag= UNKNOWN;
        if (gender.equals("female")){ ag =  FEMALE; }
        else if (gender.equals("male")){ ag = MALE; }
        else { ag = OTHER; }
        Date bd = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            bd = format.parse(birthDate);
        } catch (ParseException e) {

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

        MethodOutcome[] outcome = new MethodOutcome[1];
        Thread validateThread = new Thread() {
            @Override
            public void run() {
                boolean r = false;
                IdType id = new IdType();
                if (update) {
                    patient.setId("Patient/" + user.getPatientId());
                    outcome[0] = MyContextFhir.getClient().update()
                            .resource(patient)
                            .prettyPrint()
                            .encodedJson()
                            .execute();

                    //user.setResourceCreated(outcome[0].getCreated());
                    r = outcome[0].getCreated();
                    //setResultat(r);
                    //Log.println(Log.INFO, "S11", "UPDATED: " + String.valueOf(resultat));
                    //IdType patientId = (IdType) outcome[0].getId();
                    id = (IdType) outcome[0].getId();
                    //setIdPatient(id);
                    //user.setPatientId(patientId);
                    //Log.println(Log.INFO, "S11", "Patient ID updated: " + String.valueOf(idPatient));
                } else {
                    outcome[0] = MyContextFhir.getClient().create()
                            .resource(patient)
                            .prettyPrint()
                            .encodedJson()
                            .execute();
                    //user.setResourceCreated(outcome[0].getCreated());
                    r = outcome[0].getCreated();
                    //setResultat(r);
                    Log.println(Log.INFO, "S11", "CREATED: " + String.valueOf(r/*resultat*/));
                    //IdType patientId = (IdType) outcome[0].getId();
                    id = (IdType) outcome[0].getId();
                    //setIdPatient(id);
                    //user.setPatientId(patientId);
                    Log.println(Log.INFO, "S11", "Patient ID: " + String.valueOf(id/*idPatient*/));
                }
            }
        };
        //user.setPatientId(idPatient);
        //Log.println(Log.INFO, "S11", "Patient ID user: " + String.valueOf(user.getPatientId()));
        //Log.println(Log.INFO, "S11", "Patient created out of thread: " + String.valueOf(resultat));
        validateThread.start();
        //return resultat;
        //return  outcome[0].getCreated();
        /*if (outcome[0].getResource().getIdElement() != null) {return true;}
        else{return false;}*/
        //return user.getResourceCreated();
        //TODO Comprovar que tots els idPatient de api és posen a USER
       /* user.setPatientId(idPatient);
        Log.println(Log.INFO, "S11", "Patient ID user: " + String.valueOf(user.getPatientId()));
        Log.println(Log.INFO, "S11", "Patient created out of thread: " + String.valueOf(resultat));
        return resultat;*/
    }
}
