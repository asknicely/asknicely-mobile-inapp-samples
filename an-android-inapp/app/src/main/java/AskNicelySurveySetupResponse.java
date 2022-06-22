import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AskNicelySurveySetupResponse {
    @SerializedName("domain_key")
    private final String domainKey;
    @SerializedName("template_name")
    private final String templateName;
    @SerializedName("name")
    private final String name;
    @SerializedName("email")
    private final String email;
    @SerializedName("email_hashed")
    private final String emailHashed;
    @SerializedName("joined")
    private final String joined;

    public AskNicelySurveySetupResponse(String domainKey, String templateName, String name, String email, String emailHashed, String joined) {
        this.domainKey = domainKey;
        this.templateName = templateName;
        this.name = name;
        this.email = email;
        this.emailHashed = emailHashed;
        this.joined = joined;
    }

    public String getDomainKey() {
        return this.domainKey;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getEmailHashed() {
        return this.emailHashed;
    }

    public String getJoined() {
        return this.joined;
    }

    @NonNull
    public String toString() {
        return "AskNicelySurveySetup: " + this.domainKey;
    }
}
