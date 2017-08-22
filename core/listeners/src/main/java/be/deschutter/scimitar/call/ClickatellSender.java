package be.deschutter.scimitar.call;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClickatellSender {

    @Value("${clickatell.rest.apikey}")
    private String clickatellApiKey;
    private ClickatellRest clickatellRest;

    @PostConstruct
    public void initClickatell() {
        clickatellRest = new ClickatellRest(clickatellApiKey);
    }

    private ClickatellRest getClickatellRest() {
        if (clickatellRest == null)
            initClickatell();
        return clickatellRest;
    }

    void sendMessage(String phonenumber, String messageToSend)
        throws Exception {
        getClickatellRest().sendMessage(phonenumber, messageToSend);
    }

    public static class ClickatellRest {
        /**
         * @var The URL to use for the base of the REST API.
         */
        private static final String CLICKATELL_REST_BASE_URL = "https://api.clickatell.com/rest/";

        private static final String POST = "POST", GET = "GET", DELETE = "DELETE";

        /**
         * @var The three private variables to use for authentication.
         */
        private String apiKey;

        /**
         * Create a REST object, and set the auth, but not test the auth.
         */
        ClickatellRest(String apiKey) {
            this.apiKey = apiKey;
        }

        /**
         * This will attempt to get your current balance.
         * @return Your balance.
         * @throws Exception
         *     This will be thrown if your auth details were incorrect.
         */
        public double getBalance() throws Exception {
            // Send Request:
            String response = this.excute("account/balance", GET, null);
            JSONObject obj = (JSONObject) JSONValue.parse(response);

            CheckForError(obj);

            JSONObject objData = (JSONObject) obj.get("data");
            String balance = (String) objData.get("balance");
            return Double.parseDouble(balance);
        }

        /**
         * This sends a single message.
         * @param number
         *     The number that you wish to send to. This should be in
         *     international format.
         * @param message
         *     The message you want to send,
         * @return A Message object which will contain the information from the
         * request.
         * @throws Exception
         *     This gets thrown on an auth failure.
         */
        Message sendMessage(String number, String message) throws Exception {
            // Send Request:
            String response = this.excute("message", POST,
                "{\"to\":[\"" + number + "\"],\"text\":\"" + message + "\"}");
            JSONObject obj = (JSONObject) JSONValue.parse(response);

            CheckForError(obj);
            JSONObject objData = (JSONObject) obj.get("data");
            JSONArray msgArray = (JSONArray) objData.get("message");
            JSONObject firstMsg = (JSONObject) msgArray.get(0);

            return createMessage(firstMsg);
        }

        private Message createMessage(final JSONObject firstMsg) {
            Message msg = new Message();
            msg.number = (String) firstMsg.get("to");
            if (!((boolean) firstMsg.get("accepted"))) {
                try {
                    CheckForError(firstMsg);
                } catch (Exception e) {
                    msg.error = e.getMessage();
                }
            } else {
                msg.message_id = (String) firstMsg.get("apiMessageId");
            }
            return msg;
        }

        /**
         * This is to send the same message to multiple people.
         * @param numbers
         *     The array of numbers that are to be sent to.
         * @param message
         *     The message that you would like to send.
         * @return An Array of Message objects which will contain the
         * information from the request.
         * @throws Exception
         *     This gets thrown on auth errors.
         */
        public Message[] sendMessage(String[] numbers, String message)
            throws Exception {
            String number = numbers[0];
            for (int x = 1; x < numbers.length; x++) {
                number += "\",\"" + numbers[x];
            }

            // Send Request:
            String response = this.excute("message", POST,
                "{\"to\":[\"" + number + "\"],\"text\":\"" + message + "\"}");
            JSONObject obj = (JSONObject) JSONValue.parse(response);

            CheckForError(obj);
            JSONObject objData = (JSONObject) obj.get("data");
            JSONArray msgArray = (JSONArray) objData.get("message");
            List<Message> messages = createMessages(msgArray);
            return messages.toArray(new Message[0]);
        }

        private ArrayList<Message> createMessages(final JSONArray msgArray) {
            ArrayList<Message> messages = new ArrayList<>();
            for (Object aMsgArray : msgArray) {
                JSONObject firstMsg = (JSONObject) aMsgArray;

                messages.add(createMessage(firstMsg));
            }
            return messages;
        }

        /**
         * This will get the status and charge of the message given by the
         * messageId.
         * @param messageId
         *     The message ID that should be searched for.
         * @return A Message object which will contain the information from the
         * request.
         * @throws Exception
         *     If there was an error with the request.
         */
        public Message getMessageStatus(String messageId) throws Exception {
            String response = this.excute("message/" + messageId, GET, null);
            JSONObject obj = (JSONObject) JSONValue.parse(response);

            CheckForError(obj);
            Message msg = new Message();
            JSONObject objData = (JSONObject) obj.get("data");
            msg.message_id = (String) objData.get("apiMessageId");
            msg.charge = String.valueOf(objData.get("charge"));
            msg.status = (String) objData.get("messageStatus");
            msg.statusString = (String) objData.get("description");

            return msg;
        }

        /**
         * This will try to stop a message that has been sent. Note that only
         * messages that are going to be sent in the future can be stopped. Or
         * if by some luck you message has not been sent to the operator yet.
         * @param messageId
         *     The message ID that is to be stopped.
         * @return A Message object which will contain the information from the
         * request.
         * @throws Exception
         *     If there was something wrong with the request.
         */
        public Message stopMessage(String messageId) throws Exception {
            // Send Request:
            String response = this.excute("message/" + messageId, DELETE, null);
            JSONObject obj = (JSONObject) JSONValue.parse(response);

            CheckForError(obj);
            Message msg = new Message();
            JSONObject objData = (JSONObject) obj.get("data");
            msg.message_id = (String) objData.get("apiMessageId");
            msg.status = (String) objData.get("messageStatus");
            msg.statusString = (String) objData.get("description");

            return msg;
        }

        /**
         * This will allow you to use any feature of the API. Note that you can
         * do more powerful things with this function. And as such should only
         * be used once you have read the documentation, as the parameters are
         * passed directly to the API.
         * @param numbers
         *     The list of numbers that must be sent to.
         * @param message
         *     The message that is to be sent.
         * @param features
         *     The extra features that should be included.
         * @return An Array of Message objects which will contain the
         * information from the request.
         * @throws Exception
         *     If there is anything wrong with the submission this will get
         *     thrown.
         */
        public Message[] sendAdvancedMessage(String[] numbers, String message,
            HashMap<String, String> features) throws Exception {

            String dataPacket = "{\"to\":[\"" + numbers[0];
            for (int x = 1; x < numbers.length; x++) {
                dataPacket += "\",\"" + numbers[x];
            }
            dataPacket += "\"],\"text\":\"" + message + "\"";
            for (Map.Entry<String, String> entry : features.entrySet()) {
                dataPacket +=
                    ",\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"";
            }
            dataPacket += "}";

            // Send Request:
            String response = this.excute("message", POST, dataPacket);
            JSONObject obj = (JSONObject) JSONValue.parse(response);

            CheckForError(obj);
            JSONObject objData = (JSONObject) obj.get("data");
            JSONArray msgArray = (JSONArray) objData.get("message");
            List<Message> messages = createMessages(msgArray);
            return messages.toArray(new Message[0]);
        }

        /**
         * This attempts to get coverage data for the given number. A -1 means
         * no coverage, all else is the minimum cost the message could charge.
         * @param number
         *     The number the lookup should be done on.
         * @return The minimum possible cost, or a -1 on error.
         * @throws Exception
         *     If there was something wrong with the request.
         */
        public double getCoverage(String number) throws Exception {
            String response = this.excute("coverage/" + number, GET, null);
            JSONObject obj = (JSONObject) JSONValue.parse(response);

            CheckForError(obj);
            JSONObject objData = (JSONObject) obj.get("data");

            if (!((boolean) objData.get("routable"))) {
                return -1;
            }
            return Double
                .parseDouble(String.valueOf(objData.get("minimumCharge")));
        }

        /**
         * This executes a POST query with the given parameters.
         * @param resource
         *     The URL that should get hit.
         * @param method
         *     The data you want to send via the POST.
         * @return The content of the request.
         * @throws UnknownHostException
         */
        private String excute(String resource, String method, String dataPacket)
            throws UnknownHostException {
            URL url;
            HttpURLConnection connection = null;
            try {
                // Create connection
                url = new URL(CLICKATELL_REST_BASE_URL + resource);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(method);
                connection
                    .setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("X-Version", "1");
                connection
                    .setRequestProperty("Authorization", "Bearer " + apiKey);
                String l = "0";
                if (dataPacket != null) {
                    l = Integer.toString(dataPacket.getBytes().length);
                }
                connection.setRequestProperty("Content-Length", "" + l);
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(dataPacket != null);

                // Send request
                if (dataPacket != null) {
                    DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                    wr.writeBytes(dataPacket);
                    wr.flush();
                    wr.close();
                }

                // Get Response
                connection.getResponseCode();
                InputStream stream = connection.getErrorStream();
                if (stream == null) {
                    stream = connection.getInputStream();
                }
                BufferedReader rd = new BufferedReader(
                    new InputStreamReader(stream));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }
                rd.close();
                return response.toString().trim();
            } catch (UnknownHostException e) {
                throw e;
            } catch (Exception e) {
                return "";
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        /**
         * This is an internal function used to shorten other functions. Checks
         * for an error object, and throws it.
         * @param obj
         *     The object that needs to be checked.
         * @throws Exception
         *     The exception that was found.
         */
        private void CheckForError(JSONObject obj) throws Exception {
            JSONObject objError = (JSONObject) obj.get("error");
            if (objError != null) {
                throw new Exception((String) objError.get("description"));
            }
        }

        /**
         * This is the Message class that gets used as return values for some of
         * the functions.
         * @author Dominic Schaff <dominic.schaff@gmail.com>
         */
        public class Message {
            String number = null;
            String message_id = null;
            String charge = null;
            String status = null;
            String error = null;
            String statusString;

            Message() {
            }

            public String toString() {
                if (message_id != null) {
                    return number + ": " + message_id;
                }
                return number + ": " + error;
            }
        }
    }
}
