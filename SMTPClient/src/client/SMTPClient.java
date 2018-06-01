package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SMTPClient {

    public static void main(String [] args){

        //SMTPServer domain is debby.vs.uni-due.de
        //SMTPServer port designated 25
        try (Socket serverCon = new Socket("debby.vs.uni-due.de", 25);
             PrintWriter socketoutput = new PrintWriter(serverCon.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader (new InputStreamReader(System.in));
             BufferedReader socketinput = new BufferedReader(new InputStreamReader(serverCon.getInputStream()))) {

            //Display  SMTP response upon connection ....passierte nichts
            //System.out.println("S:" + socketinput.readLine());

            while(true) {
                //get user input
                System.out.println("C:");
                String cmd = userInput.readLine ();
                socketoutput.println(cmd);
                //get response from server
                String reply = socketinput.readLine();
                System.out.println ("S:" + reply);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
