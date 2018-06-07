//package client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

public class SMTPClient {

    private static String message = "";

    public static void main(String [] args) throws IOException {

     //   message =" \"X-=-=-=-text boundary\" \nTest test 123 ü. \n\"X-=-=-=-text boundary\" ";
        message = "Test test 123 ü.";
        byte[] encodedMessage = message.getBytes();
        String encodedMime = Base64.getMimeEncoder().encodeToString(encodedMessage);


        // Read in file path from console
//        String path = args[0];
//        File f = new File(path);
//        BufferedImage image = ImageIO.read(f);

        sendViaSMTP(encodedMime);

    }

    private static void sendViaSMTP(String encodedMime) {

        //SMTPServer domain is debby.vs.uni-due.de
        //SMTPServer port designated 25
        try (Socket serverCon = new Socket("debby.vs.uni-due.de", 25);
             PrintWriter socketoutput = new PrintWriter(serverCon.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader (new InputStreamReader(System.in));
             BufferedReader socketinput = new BufferedReader(new InputStreamReader(serverCon.getInputStream()))) {

            int counter =0;

            while(true){
                //Display SMTP Server response
                String serverFullResponse = socketinput.readLine();
                String serverResponse = serverFullResponse.substring(0, 5);
                System.out.println("S:" + serverFullResponse);
                String reply="";
                switch(serverResponse){
                    case "220 d":
                        reply = "HELO sending";
                        System.out.println("C:" + reply);
                        socketoutput.println(reply);
                        break;
                    case "250 d":
                        reply = "MAIL FROM: <mail@mail.com>";
                        System.out.println("C:" + reply);
                        socketoutput.println(reply);
                        break;
                    case "250 O":
                        if(serverFullResponse.equals("250 OK")) {
                            reply = "RCPT TO: <spam@debby.vs.uni-due.de>";
                            System.out.println("C: " + reply);
                            socketoutput.println(reply);
                        } else {
                            //exit connection with server and while loop if response from server not anticipated
                            reply = "QUIT";
                            System.out.println("C: " + reply);
                            socketoutput.println(reply);
                        }
                        break;
                    case "250 A":
                        reply = "DATA";
                        System.out.println("C: " + reply);
                        socketoutput.println(reply);
                        break;
                    case "354 E":
                        System.out.println("C: " + message);
                        socketoutput.println("Subject: test 123 \nMime-Version: 1.0  \nContent-Transfer-Encoding: base64\n" +
                                " \nContent-Disposition: multipart/mixed;  boundary=\"17pEHd4RhPHOinZp\" \nContent-Type: text/plain\n charset=utf-8 \n "    );
//                       socketoutput.println(message);
                        socketoutput.println(encodedMime);
                        socketoutput.println(".");
                        break;

                    default :
                        //exit connection with server and while loop if response from server not anticipated
                        //System.out.println("S:" + serverResponse);
                        reply = "QUIT";
                        System.out.println("C: " + reply);
                        socketoutput.println(reply);
                        break;
                }

                if(reply.equals("QUIT")) break;

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
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
