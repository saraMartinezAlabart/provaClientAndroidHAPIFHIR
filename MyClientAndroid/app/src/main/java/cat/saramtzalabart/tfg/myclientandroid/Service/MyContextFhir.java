package cat.saramtzalabart.tfg.myclientandroid.Service;

import android.app.Application;

import ca.uhn.fhir.context.FhirContext;

public class MyContextFhir extends Application {

    private static FhirContext ctx;

    @Override
    public void onCreate() {
        ctx = FhirContext.forDstu3();
        super.onCreate();
    }

    public static FhirContext getCtx() {
        return ctx;
    }
}
