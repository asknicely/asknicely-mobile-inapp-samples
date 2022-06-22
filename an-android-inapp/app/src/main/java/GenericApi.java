import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GenericApi {
    String BASE_URL = "http://10.0.2.2:8083";

    @GET("/")
    Call<AskNicelySurveySetupResponse> getSurveySetup();

    @POST
    Call<AskNicelySurveySlugResponse> getSurveySlug(
            @Url String url,
            @Query("id") String id,
            @Query("anVersion") String version,
            @Query("reloadCookie") String reloadCookie,
            @Body AskNicelySurveySlugRequest slugRequest
    );
}
