package cat.saramtzalabart.tfg.myclientandroid.Service;

import org.hl7.fhir.dstu3.model.IdType;

public class UserProvider {
    //MY IDENTIFIER
    //private String dni;
    //TODO pensar si cal per a tots els recursos o si no cal però igualment prefereixo tenir un per a cada thread
    private boolean resourceCreated;

    //MY PATIENT
    private IdType patientId;


    public UserProvider() {
        //this.dni = "";
        this.patientId = null;
        this.resourceCreated=false;
    }

    public IdType getPatientId() {
        return patientId;
    }

    public boolean getResourceCreated(){return resourceCreated;}

    public void setResourceCreated(boolean created){
        this.resourceCreated=created;
    }

    public void setPatientId(IdType patientId) {
        this.patientId = patientId;
    }
}
