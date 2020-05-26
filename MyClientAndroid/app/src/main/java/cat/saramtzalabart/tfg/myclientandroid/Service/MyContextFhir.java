package cat.saramtzalabart.tfg.myclientandroid.Service;

import android.app.Application;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class MyContextFhir extends Application {

    private static FhirContext ctx;
    private static IGenericClient client;

    @Override
    public void onCreate() {
        ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(Constants.serverBase);
        super.onCreate();
    }

    public static FhirContext getCtx() {
        return ctx;
    }

    public static IGenericClient getClient() {
        return client;
    }
}
