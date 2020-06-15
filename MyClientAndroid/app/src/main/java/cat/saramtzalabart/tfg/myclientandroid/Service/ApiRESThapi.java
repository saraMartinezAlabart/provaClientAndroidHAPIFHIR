package cat.saramtzalabart.tfg.myclientandroid.Service;


import android.util.Log;
import android.widget.Toast;

import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Range;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseBundle;
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
    private boolean resultat;
    private IdType idPatient;

    public ApiRESThapi() {
        user = new UserProvider();
        resultat = false;
        idPatient = new IdType();
        //this.client = MyContextFhir.getCtx().newRestfulGenericClient(Constants.serverBase);
    }

    /*public IGenericClient getClient() {
        return client;
    }*/
    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private void setResultat (boolean value){
        user.setResourceCreated(value);
        resultat = value;
    }
    private void setIdPatient(IdType value){
        user.setPatientId(value);
        idPatient = value;
    }
    /*
    *
    *
    * @CREATE METHODS PATIENT
    *
    *
    */
    //TODO Call this methode frome Fragment Sign UP
    public boolean createPatient(String name, String surname, String gender, String birthDate, String dni, final boolean update){
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
            return false;
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
                    setResultat(r);
                    Log.println(Log.INFO, "S11", "UPDATED: " + String.valueOf(resultat));
                    //IdType patientId = (IdType) outcome[0].getId();
                    id = (IdType) outcome[0].getId();
                    setIdPatient(id);
                    //user.setPatientId(patientId);
                    Log.println(Log.INFO, "S11", "Patient ID updated: " + String.valueOf(idPatient));
                } else {
                    outcome[0] = MyContextFhir.getClient().create()
                            .resource(patient)
                            .prettyPrint()
                            .encodedJson()
                            .execute();
                    //user.setResourceCreated(outcome[0].getCreated());
                    r = outcome[0].getCreated();
                    setResultat(r);
                    Log.println(Log.INFO, "S11", "CREATED: " + String.valueOf(resultat));
                    //IdType patientId = (IdType) outcome[0].getId();
                    id = (IdType) outcome[0].getId();
                    setIdPatient(id);
                    //user.setPatientId(patientId);
                    Log.println(Log.INFO, "S11", "Patient ID: " + String.valueOf(idPatient));
                }
            }
        };
        //user.setPatientId(idPatient);
        Log.println(Log.INFO, "S11", "Patient ID user: " + String.valueOf(user.getPatientId()));
        Log.println(Log.INFO, "S11", "Patient created out of thread: " + String.valueOf(resultat));
        validateThread.start();
        return resultat;

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
    public boolean createDiagnosticReport (String typeDiabetes){

        //Get different system, code, and display depending of the typeValue.
        String typeSystem;
        String typeCode;
        String typeDisplay;
        if (typeDiabetes.equals("Tipus 1")){
            typeSystem = "http://snomed.info/sct";
            typeCode = "46635009";
            typeDisplay = "diabetes mellitus tipus 1";
        }else{
            typeSystem = "http://snomed.info/sct";
            typeCode = "44054006";
            typeDisplay = "diabetes mellitus tipus 2";
        }
        String patient = "Patient/" + user.getPatientId();


        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
        Coding coding = new Coding()
                .setCode(typeCode)
                .setSystem(typeSystem)
                .setDisplay(typeDisplay);
        CodeableConcept codeableConcept = new CodeableConcept()
                .addCoding(coding);
        diagnosticReport.setCode(codeableConcept);
        Reference reference = new Reference()
                .setReference(patient);
        diagnosticReport.setSubject(reference);


        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(diagnosticReport)
                .prettyPrint()
                .encodedJson()
                .execute();


        IdType diagnosticReportId = (IdType) outcome.getId();
        user.setDiagnosticReportId(diagnosticReportId);

        return  outcome.getCreated();
    }

    public boolean createQuestionnaireResponseInsulin (BooleanType injectInsulin){
        String patient = "Patient/" + user.getPatientId();
        String linkId = "1";
        String text = "Do you inject insulin?";

        QuestionnaireResponse questionnaireResponseInsulin = new QuestionnaireResponse();
        questionnaireResponseInsulin.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
        Reference reference = new Reference()
                .setReference(patient);
        questionnaireResponseInsulin.setSource(reference);
        QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent answerComponent = new QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent()
                .setValue(injectInsulin);
        /*QuestionnaireResponse.QuestionnaireResponseItemComponent itemComponent = new QuestionnaireResponse.QuestionnaireResponseItemComponent()
                .setLinkId(linkId)
                .setText(text)
                //TODO create a list answerComponent
                .setAnswer(answerComponent);
        //TODO create a list itemComponent
        questionnaireResponseInsulin.setItem(itemComponent);*/


        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(questionnaireResponseInsulin)
                .prettyPrint()
                .encodedJson()
                .execute();


        IdType questionnaireResponseInsulinId = (IdType) outcome.getId();
        user.setQuestionnaireResponseInsulinId(questionnaireResponseInsulinId);

        return  outcome.getCreated();
    }
    public boolean createQuestionnaireResponsePills (BooleanType takePills){

        String patient = "Patient/" + user.getPatientId();
        String linkId = "2";
        String text = "Do you take pills?";

        QuestionnaireResponse questionnaireResponsePills = new QuestionnaireResponse();
        questionnaireResponsePills.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
        Reference reference = new Reference()
                .setReference(patient);
        questionnaireResponsePills.setSource(reference);
        QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent answerComponent = new QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent()
                .setValue(takePills);
        /*QuestionnaireResponse.QuestionnaireResponseItemComponent itemComponent = new QuestionnaireResponse.QuestionnaireResponseItemComponent()
                .setLinkId(linkId)
                .setText(text)
                //TODO create a list answerComponent
                .setAnswer(answerComponent);
        //TODO create a list itemComponent
        questionnaireResponsePills.setItem(itemComponent);*/

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(questionnaireResponsePills)
                .prettyPrint()
                .encodedJson()
                .execute();


        IdType questionnaireResponsePillsId = (IdType) outcome.getId();
        user.setQuestionnaireResponsePillsId(questionnaireResponsePillsId);

        return  outcome.getCreated();
    }

    public boolean createConditionHipo (double highValueHipo){

        String patient = "Patient/" + user.getPatientId();
        double lowValue = 0.00;
        String unit = "mg/dL";

        Condition conditionHipo = new Condition();
        Reference reference = new Reference()
                .setReference(patient);
        conditionHipo.setSubject(reference);
        Quantity quantityLow = new Quantity()
                .setValue(lowValue)
                .setUnit(unit);
        Quantity quantityHigh = new Quantity()
                .setValue(highValueHipo)
                .setUnit(unit);
        Range range = new Range()
                .setLow((SimpleQuantity) quantityLow)
                .setHigh((SimpleQuantity) quantityHigh);
        conditionHipo.setOnset(range);

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(conditionHipo)
                .prettyPrint()
                .encodedJson()
                .execute();


        IdType conditionHipoId = (IdType) outcome.getId();
        user.setConditionHipoId(conditionHipoId);

        return  outcome.getCreated();
    }
    public boolean createConditionHyper (double highValueHyper){

        String patient = "Patient/" + user.getPatientId();
        double lowValue = 160.00;
        String unit = "mg/dL";

        Condition conditionHyper = new Condition();
        Reference reference = new Reference()
                .setReference(patient);
        conditionHyper.setSubject(reference);
        Quantity quantityLow = new Quantity()
                .setValue(lowValue)
                .setUnit(unit);
        Quantity quantityHigh = new Quantity()
                .setValue(highValueHyper)
                .setUnit(unit);
        Range range = new Range()
                .setLow((SimpleQuantity) quantityLow)
                .setHigh((SimpleQuantity) quantityHigh);
        conditionHyper.setOnset(range);

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(conditionHyper)
                .prettyPrint()
                .encodedJson()
                .execute();


        IdType conditionHyperId = (IdType) outcome.getId();
        user.setConditionHyperId(conditionHyperId);

        return  outcome.getCreated();
    }

    public boolean createDevice (String glucometerName){

        String patient = "Patient/" + user.getPatientId();

        Device device = new Device();
        Device.DeviceUdiComponent udiComponent = new Device.DeviceUdiComponent()
                .setName(glucometerName);
        device.setUdi(udiComponent);
        Reference reference = new Reference()
                .setReference(patient);
        device.setPatient(reference);

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(device)
                .prettyPrint()
                .encodedJson()
                .execute();


        IdType deviceId = (IdType) outcome.getId();
        user.setDeviceId(deviceId);

        return  outcome.getCreated();
    }


    /*
     *
     *
     * @CREATE METHODS OBSERVATION
     *
     *
     */
    //TODO Call this methode frome Fragment New Log
    public boolean createObservationBloodSugar (float bloodSugar, DateTimeType dateTime){
        String code = "33747003";
        String text = "medición de glucosa en sangre";
        String unit = "mg/dL";
        String patient = "Patient/" + user.getPatientId();

        Observation observation = new Observation();
        observation.setStatus(Observation.ObservationStatus.FINAL);
        Coding coding = new Coding().setCode(code);
        CodeableConcept codeableConcept = new CodeableConcept()
                .addCoding(coding)
                .setText(text);
        observation.setCode(codeableConcept);
        Reference reference = new Reference()
                .setReference(patient);
        observation.setSubject(reference);
        observation.setEffective(dateTime);
        Quantity quantity = new Quantity()
                .setValue(bloodSugar)
                .setUnit(unit);
        observation.setValue(quantity);

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(observation)
                .prettyPrint()
                .encodedJson()
                .execute();

        IdType observationBloodSugarId = (IdType) outcome.getId();
        user.setObservationBloodSugarId(observationBloodSugarId);

        return  outcome.getCreated();
    }
    public boolean createObservationCarbs (float carbs, DateTimeType dateTime){
        String code = "2331003";
        String text = "carbohidrato";
        String unit = "g";
        String patient = "Patient/" + user.getPatientId();

        Observation observation = new Observation();
        observation.setStatus(Observation.ObservationStatus.FINAL);
        Coding coding = new Coding().setCode(code);
        CodeableConcept codeableConcept = new CodeableConcept()
                .addCoding(coding)
                .setText(text);
        observation.setCode(codeableConcept);
        Reference reference = new Reference()
                .setReference(patient);
        observation.setSubject(reference);
        observation.setEffective(dateTime);
        Quantity quantity = new Quantity()
                .setValue(carbs)
                .setUnit(unit);
        observation.setValue(quantity);

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(observation)
                .prettyPrint()
                .encodedJson()
                .execute();

        IdType observationCarbsId = (IdType) outcome.getId();
        user.setObservationCarbsId(observationCarbsId);

        return  outcome.getCreated();
    }
    public boolean createObservationMeal (String meal, DateTimeType dateTime, String notes){
        String code = "255620007";
        String text = "alimento";
        String patient = "Patient/" + user.getPatientId();

        Observation observation = new Observation();
        observation.setStatus(Observation.ObservationStatus.FINAL);
        Coding coding = new Coding().setCode(code);
        CodeableConcept codeableConcept = new CodeableConcept()
                .addCoding(coding)
                .setText(text);
        observation.setCode(codeableConcept);
        Reference reference = new Reference()
                .setReference(patient);
        observation.setSubject(reference);
        observation.setEffective(dateTime);
        StringType stringType = new StringType(meal);
        observation.setValue(stringType);
        observation.setComment(notes);

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(observation)
                .prettyPrint()
                .encodedJson()
                .execute();

        IdType observationMealId = (IdType) outcome.getId();
        user.setObservationMealId(observationMealId);

        return  outcome.getCreated();
    }
    public boolean createMedicationStatement (float insulinUnits, DateTimeType dateTime){
        String system = "http://snomed.info/sct";
        String code = "67866001";
        String display = "insulina";
        String patient = "Patient/" + user.getPatientId();
        String units = "Units";

        MedicationStatement medicationStatement = new MedicationStatement();
        medicationStatement.setStatus(MedicationStatement.MedicationStatementStatus.ACTIVE);
        Coding coding = new Coding()
                .setCode(code)
                .setSystem(system)
                .setDisplay(display);
        CodeableConcept codeableConcept = new CodeableConcept()
                .addCoding(coding);
        medicationStatement.setMedication(codeableConcept);
        medicationStatement.setEffective(dateTime);
        Reference reference = new Reference()
                .setReference(patient);
        medicationStatement.setSubject(reference);
        medicationStatement.setTaken(MedicationStatement.MedicationStatementTaken.Y);
        Quantity quantity = new Quantity()
                .setValue(insulinUnits)
                .setUnit(units);
        Dosage dosage = new Dosage()
                .setDose(quantity);
        //TODO mirar com crear una sola dosage
        //medicationStatement.setDosage(dosage);

        MethodOutcome outcome = MyContextFhir.getClient().create()
                .resource(medicationStatement)
                .prettyPrint()
                .encodedJson()
                .execute();

        IdType medicationStatementId = (IdType) outcome.getId();
        user.setMedicationStatementId(medicationStatementId);

        return  outcome.getCreated();
    }


    /*
     *
     *
     * @EXIST USER?
     *
     *
     */
    //Call this methode frome Fragment Sign IN
   public boolean patientExist(String dni){
        IBaseBundle response = MyContextFhir.getClient().search()
                .forResource(Patient.class)
                //TODO comprovar que code és correcte per obtenir el identificador del DNI del patient
                .where(Patient.IDENTIFIER.exactly().code(dni))
                .returnBundle(IBaseBundle.class)
                .execute();

        Log.println(Log.INFO, "S11", "This patient already exist: " + response.isEmpty());
        if (response.isEmpty()){return false;}
        else{return true;}
    }

    /*
    *
    *
    * @GET METHODS PATIENT
    *
    *
    */
    //TODO Call this methode frome Fragment Settings
    public Patient getMyPatient (){

        Patient patient = MyContextFhir.getClient().read()
                .resource(Patient.class)
                .withId(user.getPatientId())
                .execute();

        return patient;
    }

    public DiagnosticReport getMyDiagnosticReport (){

        DiagnosticReport diagnosticReport = MyContextFhir.getClient().read()
                .resource(DiagnosticReport.class)
                .withId(user.getDiagnosticReportId())
                .execute();

        return diagnosticReport;
    }

    public QuestionnaireResponse getMyQuestionnaireResponseInsulin (){

        QuestionnaireResponse questionnaireResponseInsulin = MyContextFhir.getClient().read()
                .resource(QuestionnaireResponse.class)
                .withId(user.getQuestionnaireResponseInsulinId())
                .execute();

        return questionnaireResponseInsulin;
    }

    public QuestionnaireResponse getMyQuestionnaireResponsePills (){

        QuestionnaireResponse questionnaireResponsePills = MyContextFhir.getClient().read()
                .resource(QuestionnaireResponse.class)
                .withId(user.getQuestionnaireResponsePillsId())
                .execute();

        return questionnaireResponsePills;
    }

    public Condition getMyConditionHipo (){

        Condition conditionHipo = MyContextFhir.getClient().read()
                .resource(Condition.class)
                .withId(user.getConditionHipoId())
                .execute();

        return conditionHipo;
    }

    public Condition getMyConditionHyper (){

        Condition conditionHyper = MyContextFhir.getClient().read()
                .resource(Condition.class)
                .withId(user.getConditionHyperId())
                .execute();

        return conditionHyper;
    }

    public Device getMyDevice (){

        Device device = MyContextFhir.getClient().read()
                .resource(Device.class)
                .withId(user.getDeviceId())
                .execute();

        return device;
    }

    /*
     *
     *
     * @GET METHODS OBSERVATION
     *
     *
     */
    //TODO Call this methode frome Fragment Observation

    public Observation getMyObservationBloodSugar (){

        Observation observationBloodSugar = MyContextFhir.getClient().read()
                .resource(Observation.class)
                .withId(user.getObservationBloodSugarId())
                .execute();

        return observationBloodSugar;
    }

    public Observation getMyObservationCarbs (){

        Observation observationCarbs = MyContextFhir.getClient().read()
                .resource(Observation.class)
                .withId(user.getObservationCarbsId())
                .execute();

        return observationCarbs;
    }

    public Observation getMyObservationMeal (){

        Observation observationMeal = MyContextFhir.getClient().read()
                .resource(Observation.class)
                .withId(user.getObservationMealId())
                .execute();

        return observationMeal;
    }

    public MedicationStatement getMyMedicationStatement (){

        MedicationStatement medicationStatement = MyContextFhir.getClient().read()
                .resource(MedicationStatement.class)
                .withId(user.getMedicationStatementId())
                .execute();

        return medicationStatement;
    }
    /*public Patient getPatientWithDNI (String dni){

           Patient patient = MyContextFhir.getClient().read()
                .resource(Patient.class)
                .withUrl(Constants.serverBase + "Patient?identifier=" + dni)
                .execute();

        return patient;
    }

    public Bundle getMyBundle (String dni){
        Bundle patient = MyContextFhir.getClient().search()
                .forResource(Patient.class)
                .where(Patient.IDENTIFIER.exactly().code(dni))
                .returnBundle(Bundle.class)
                .execute();
        return patient;
    }*/

}
