package be.deschutter.scimitar.call;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.net.URISyntaxException;

class CallRunner implements Runnable {
    private final String phoneNumber;
    private String twilioAcountSid;
    private String twilioAuthToken;
    private String twilioPhoneNumber;

    public CallRunner(final String phoneNumber, final String twilioAcountSid,
        final String twilioAuthToken, final String twilioPhoneNumber) {
        this.phoneNumber = phoneNumber;
        this.twilioAcountSid = twilioAcountSid;
        this.twilioAuthToken = twilioAuthToken;
        this.twilioPhoneNumber = twilioPhoneNumber;
    }

    @Override
    public void run() {

        Twilio.init(this.twilioAcountSid, this.twilioAuthToken);

        try {

            com.twilio.rest.api.v2010.account.Call call = com.twilio.rest.api.v2010.account.Call
                .creator(new PhoneNumber(this.phoneNumber),
                // To number
                new PhoneNumber(this.twilioPhoneNumber),  // From number

                // Read TwiML at this URL when a call connects (hold music)
                new URI(
                    "http://twimlets.com/holdmusic?Bucket=com.twilio.music.ambient"))
                .create();

            com.twilio.rest.api.v2010.account.Call.Status status = call.getStatus();
            while (status == com.twilio.rest.api.v2010.account.Call.Status.QUEUED
                || status == com.twilio.rest.api.v2010.account.Call.Status.RINGING) {
                Thread.sleep(500);
                status = com.twilio.rest.api.v2010.account.Call.fetcher(call.getSid()).fetch()
                    .getStatus();
            }

            com.twilio.rest.api.v2010.account.Call.updater(call.getSid())
                .setStatus(com.twilio.rest.api.v2010.account.Call.UpdateStatus.COMPLETED).update();

        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
