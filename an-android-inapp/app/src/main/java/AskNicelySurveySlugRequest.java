import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AskNicelySurveySlugRequest {
    //The following are the standard variables that come through the survey setup request
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
    @SerializedName("force")
    private final boolean force;

    //The following can be any custom property that you care to set
    @SerializedName("a_custom_property")
    private final String aCustomProperty;

    public AskNicelySurveySlugRequest(
            String domainKey,
            String templateName,
            String name,
            String email,
            String emailHash,
            String created,
            String aCustomProperty
    ) {
        this.domainKey = domainKey;
        this.templateName = templateName;
        this.name = name;
        this.email = email;
        this.emailHash = emailHash;
        this.created = created;
        this.aCustomProperty = aCustomProperty;
        this.force = true;
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

    public String getCustomProperty() {
        return this.aCustomProperty;
    }

    public boolean getForce() {
        return this.force;
    }

    @NonNull
    public String toString() {
        return "AskNicelySurveySetup: " + this.domainKey;
    }
}
