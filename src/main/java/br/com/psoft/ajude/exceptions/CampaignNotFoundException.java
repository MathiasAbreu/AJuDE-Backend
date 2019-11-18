package br.com.psoft.ajude.exceptions;

public class CampaignNotFoundException extends CampaignException {

    public CampaignNotFoundException() {
        super("A campanha não existe!");
    }

    public CampaignNotFoundException(String identificadorURL) {
        super("A campanha não existe: " + identificadorURL);
    }
}
