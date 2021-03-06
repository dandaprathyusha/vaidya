package iiit.speech.dialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import iiit.speech.domain.DomainDesc;
import iiit.speech.itra.VaidyaActivity;
import iiit.speech.nlu.NLU;

/**
 * Created by brij on 4/9/15.
 */
public class GreetState extends DialogState {


    public static Map<String, String> ack_message;

    private VaidyaActivity app;
    private NLU nlu;
    public boolean expect_binary = false;

    public GreetState(VaidyaActivity a, NLU nlu1) {
        entered = false;
        app = a;
        nlu = nlu1;
        this.setName("greet");
        ack_message = new HashMap<>();
        ack_message.put("health", "Do you want me to diagnose your symptoms?");
        ack_message.put("nodomain", "Sorry. I did not get that. Can you please repeat?");
    }

    @Override
    public void onEntry() {
        System.out.println("+++++++++++++++++ Greet state entered +++++++++++++++++++++");
        app.speakOut("Hi. Do you want me to diagnose your disease or assist you with first aid or help you with disease enquiry");

        // Set appropriate grammar
        //current_grammar =  app.GENERIC_SEARCH;
        entered = true;
        //next_state = "greet";
        //expect_binary = false;
        current_grammar = app.DISEASE_QUERY_RESPONSE;
     //   conclude = true;
     //   next_state = "ask_symptoms";
        try {
            domain = nlu.getDomain("health");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRecognize(String hypothesis) {

     //   if (expect_binary) {
            // Normalize binary response to either positive or negative
        String greetResponse = nlu.resolveGreetStateResponse(hypothesis);
            if (greetResponse.equalsIgnoreCase("first aid")) {
                conclude = true;
                next_state = "first_aid";
            }
            else if(greetResponse.equalsIgnoreCase("ask symptoms")) {
                conclude = true;
                next_state = "ask_symptoms";
            }
            else if(greetResponse.equalsIgnoreCase("disease enquiry")){
                conclude = true;
                next_state = "disease_enquiry";
            }

    /*    }
        else
        {
            try {
                domain = nlu.getDomain(hypothesis);
            } catch (IOException e) {

            }
            app.speakOut(ack_message.get(domain.getName()));
            switch (domain.getName()) {
                case "health":
                    expect_binary = true;
                    current_grammar = app.BINARY_RESPONSE;
                    break;
                case "nodomain":
                    expect_binary = false;
                    current_grammar = app.GENERIC_SEARCH;
                    break;
            }
        }*/
    }

    @Override
    public void onExit() {

    }
}
