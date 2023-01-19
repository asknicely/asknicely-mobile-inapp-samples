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
    @SerializedName("email_hash")
    private final String emailHash;
    @SerializedName("created")
    private final String created;

    public AskNicelySurveySetupResponse(String domainKey, String templateName, String name, String email, String emailHash, String created) {
        this.domainKey = domainKey;
        this.templateName = templateName;
        this.name = name;
        this.email = email;
        this.emailHash = emailHash;
        this.created = created;
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

    public String getEmailHash() {
        return this.emailHash;
    }

    public String getCreated() {
        return this.created;
    }

    @NonNull
    public String toString() {
        return "AskNicelySurveySetup: " + this.domainKey;
    }
}
