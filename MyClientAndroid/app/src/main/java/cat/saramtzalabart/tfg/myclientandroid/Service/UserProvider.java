package cat.saramtzalabart.tfg.myclientandroid.Service;

import org.hl7.fhir.dstu3.model.IdType;

public class UserProvider {
    //MY IDENTIFIER
    //private String dni;

    //MY PATIENT
    private IdType patientId;
    private IdType diagnosticReportId;
    private IdType questionnaireResponseInsulinId;
    private IdType questionnaireResponsePillsId;
    private IdType conditionHipoId;
    private IdType conditionHyperId;
    private IdType deviceId;

    //MY OBSERVATION
    private IdType observationBloodSugarId;
    private IdType observationCarbsId;
    private IdType observationMealId;
    private IdType medicationStatementId;

    public UserProvider() {
        //this.dni = "";
        this.patientId = null;
        this.diagnosticReportId=null;
        this.questionnaireResponseInsulinId=null;
        this.questionnaireResponsePillsId=null;
        this.conditionHipoId=null;
        this.conditionHyperId=null;
        this.deviceId=null;
        this.observationBloodSugarId=null;
        this.observationCarbsId=null;
        this.observationMealId=null;
        this.medicationStatementId=null;
    }

    /*public String getDni() {
        return dni;
    }
    //TODO call frome Fragment Sign In
    public void setDni(String dni) {
        this.dni = dni;
    }*/

    public IdType getPatientId() {
        return patientId;
    }

    public IdType getObservationBloodSugarId() {
        return observationBloodSugarId;
    }

    public IdType getObservationCarbsId() {
        return observationCarbsId;
    }

    public IdType getObservationMealId() {
        return observationMealId;
    }

    public IdType getMedicationStatementId() {
        return medicationStatementId;
    }

    public IdType getDiagnosticReportId() {
        return diagnosticReportId;
    }

    public IdType getQuestionnaireResponseInsulinId() {
        return questionnaireResponseInsulinId;
    }

    public IdType getQuestionnaireResponsePillsId() {
        return questionnaireResponsePillsId;
    }

    public IdType getConditionHipoId() {
        return conditionHipoId;
    }

    public IdType getConditionHyperId() {
        return conditionHyperId;
    }

    public IdType getDeviceId() {
        return deviceId;
    }

    public void setDiagnosticReportId(IdType diagnosticReportId) {
        this.diagnosticReportId = diagnosticReportId;
    }

    public void setQuestionnaireResponseInsulinId(IdType questionnaireResponseInsulinId) {
        this.questionnaireResponseInsulinId = questionnaireResponseInsulinId;
    }

    public void setQuestionnaireResponsePillsId(IdType questionnaireResponsePillsId) {
        this.questionnaireResponsePillsId = questionnaireResponsePillsId;
    }

    public void setConditionHipoId(IdType conditionHipoId) {
        this.conditionHipoId = conditionHipoId;
    }

    public void setConditionHyperId(IdType conditionHyperId) {
        this.conditionHyperId = conditionHyperId;
    }

    public void setDeviceId(IdType deviceId) {
        this.deviceId = deviceId;
    }

    public void setMedicationStatementId(IdType medicationStatementId) {
        this.medicationStatementId = medicationStatementId;
    }

    public void setObservationCarbsId(IdType observationCarbs) {
        this.observationCarbsId = observationCarbs;
    }

    public void setObservationMealId(IdType observationMeal) {
        this.observationMealId = observationMeal;
    }

    public void setObservationBloodSugarId(IdType observationBloodSugarId) {
        this.observationBloodSugarId = observationBloodSugarId;
    }

    public void setPatientId(IdType patientId) {
        this.patientId = patientId;
    }
}
