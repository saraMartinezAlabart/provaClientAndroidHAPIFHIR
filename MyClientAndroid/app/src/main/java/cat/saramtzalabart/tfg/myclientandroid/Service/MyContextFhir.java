package cat.saramtzalabart.tfg.myclientandroid.Service;

import android.app.Application;
import android.util.Log;

import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.FhirClientInappropriateForServerException;

public class MyContextFhir extends Application {

    private static FhirContext ctx;
    private static IGenericClient client;

    @Override
    public void onCreate() {
        try {
            ctx = FhirContext.forDstu3();
            Log.println(Log.DEBUG, "S11", "FHIRContext: " + ctx);
            client = ctx.newRestfulGenericClient(Constants.serverBase);
            Log.println(Log.DEBUG, "S11", "Restful Generic Client: " + client);
        }catch (NullPointerException e){
            //Log.e(FhirApp.TAG, e.getMessage(), e);
            System.out.println("Exception: " + e);
        }catch (FhirClientConnectionException e){
            System.out.println("Exception: " + e);
        }catch (FhirClientInappropriateForServerException e){
            System.out.println("Exception: " + e);
        }catch (FHIRException e){
            System.out.println("Exception: " + e);
        }finally {
            System.out.println("Exception Finally");
        }

        super.onCreate();
    }

    public static IGenericClient getClient() {
        if (ctx == null){
            ctx = FhirContext.forDstu3();
        }
        if (client == null){
            client = ctx.newRestfulGenericClient(Constants.serverBase);
        }
        return client;
    }
}
