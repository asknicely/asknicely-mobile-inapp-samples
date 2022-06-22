import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenericApiClient {

    private static GenericApiClient instance = null;
    private final GenericApi gApi;

    private GenericApiClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GenericApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gApi = retrofit.create(GenericApi.class);
    }

    public static synchronized GenericApiClient getInstance() {
        if (instance == null) {
            instance = new GenericApiClient();
        }
        return instance;
    }

    public GenericApi getApi() {
        return gApi;
    }
}
