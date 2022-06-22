import com.google.gson.annotations.SerializedName;

public class AskNicelySurveySlugResponse {
    @SerializedName("id")
    private final int id;

    @SerializedName("wait")
    private final int wait;

    @SerializedName("slug")
    private final String slug;

    @SerializedName("show")
    private final boolean show;

    @SerializedName("info")
    private final String info;

    public AskNicelySurveySlugResponse(int id, int wait, String slug, boolean show, String info) {
        this.id = id;
        this.wait = wait;
        this.slug = slug;
        this.show = show;
        this.info = info;
    }

    public int getId() {
        return this.id;
    }

    public int getWait() {
        return this.wait;
    }

    public String getSlug() {
        return this.slug;
    }

    public boolean getShow() {
        return this.show;
    }

    public String getInfo() {
        return this.info;
    }
}
