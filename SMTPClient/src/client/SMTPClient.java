/**
 * Created by Rebecca Marsh, Matrikelnummer 3006709
 */
package client;




//import org.apache.commons.io.*;
import com.oracle.tools.packager.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class SMTPClient {

    private static String message = "";

    public static void main(String [] args)  {

        message = "Test test 123 ü.";
        byte[] encodedMessage = message.getBytes();
        String encodedMime = Base64.getMimeEncoder().encodeToString(encodedMessage);

        // Read in file path from console
//        String path = args[0];
//        File file1 = new File(path);
//        byte [] imageInJar = getBytes(file1);
//        String encodedImageMime2 = Base64.getMimeEncoder().encodeToString(imageInJar);

        //Read in file locally
        //easy version
        File file2 = new File(System.getProperty("user.dir") + "/images/pacman.png");
        byte [] imageInByte = getBytes(file2);
        String encodedImageMime = Base64.getMimeEncoder().encodeToString(imageInByte);
        //***

        //harder version with file and stream ---still incorrect for loading jars
//        System.out.println("Path: "+ System.getProperty("user.dir") + "/images/pacman.png");
//        File file2 = new File(System.getProperty("user.dir") + "/images/pacman.png");
//        InputStream stream = null;
//        try {
//            stream = new FileInputStream(file2);
//        } catch (FileNotFoundException e) {
//            System.out.println("error on converting stream to file");
//        }
//        //****
//        byte[] targetArray = new byte[(int) 63093];
//        if(stream!=null) {
//            try {
//                targetArray = new byte[stream.available()];
//                stream.read(targetArray);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        String encodedImageMime = Base64.getMimeEncoder().encodeToString(targetArray);

        //send text and images via email
  //      sendJarPicViaSMTP(encodedMime, encodedImageMime2);
  //      sendViaSMTP(encodedMime, encodedImageMime, encodedImageMime2);
        sendViaSMTP(encodedMime, encodedImageMime);

    }

    private static byte[] getBytes(File f) {
        try {
            BufferedImage image = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
                                " \nContent-Disposition: inline \nContent-Type: text/plain;  boundary=\"17pEHd4RhPHOinZp\"\n charset=utf-8 \n "    );

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

    private static void sendViaSMTP(String encodedMime, String encodedFile, String encodedFile2) {

        //SMTPServer domain is debby.vs.uni-due.de
        //SMTPServer port designated 25
        try (Socket serverCon = new Socket("debby.vs.uni-due.de", 25);
             PrintWriter socketoutput = new PrintWriter(serverCon.getOutputStream(), true);
             //BufferedReader userInput = new BufferedReader (new InputStreamReader(System.in));
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
//                        socketoutput.println("Subject: test 123 \nMime-Version: 1.0  \nContent-Transfer-Encoding: base64\n" +
//                                " \nContent-Disposition: inline \nContent-Type: text/plain;  boundary=\"17pEHd4RhPHOinZp\"\n charset=utf-8 \n "    );
                        socketoutput.println("Subject: test 123 \nMime-Version: 1.0  \nContent-Transfer-Encoding: base64\n" +
                                " \nContent-Disposition: inline \nContent-Type: multipart/mixed;  boundary=\"17pEHd4RhPHOinZp\"\n charset=utf-8 \n "    );

                        //basic functionality with hard coded text (Basis, Feature 1, Feature 2)
                        socketoutput.println("--17pEHd4RhPHOinZp\n Content-Type: text/plain; charset=utf-8\n" +
                                "Content-Transfer-Encoding: base64\n"    );
                        socketoutput.println(encodedMime);
                        socketoutput.println("--17pEHd4RhPHOinZp"    );

                        //functionality with attached image (Feature 3)
                        socketoutput.println("Content-Type:image/png; name=pacman.png \n" +
                                "Content-Disposition: attachment; filename = \"pacman.png\" \n" +
                                "Content-Transfer-Encoding: base64\n"  );
                        socketoutput.println(encodedFile);
                        socketoutput.println("--17pEHd4RhPHOinZp "  );

                        //functionality with random image (Feature 4)
                        socketoutput.println("Content-Type:image/jpg; name=untitled.jpg \n" +
                                "Content-Disposition: attachment; filename = \"untitled.jpg\" \n" +
                                "Content-Transfer-Encoding: base64\n"  );
                        socketoutput.println(encodedFile2);
                        socketoutput.println("--17pEHd4RhPHOinZp "  );


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

    private static void sendViaSMTP(String encodedMime, String encodedFile) {

        //SMTPServer domain is debby.vs.uni-due.de
        //SMTPServer port designated 25
        try (Socket serverCon = new Socket("debby.vs.uni-due.de", 25);
             PrintWriter socketoutput = new PrintWriter(serverCon.getOutputStream(), true);
             //BufferedReader userInput = new BufferedReader (new InputStreamReader(System.in));
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
//                        socketoutput.println("Subject: test 123 \nMime-Version: 1.0  \nContent-Transfer-Encoding: base64\n" +
//                                " \nContent-Disposition: inline \nContent-Type: text/plain;  boundary=\"17pEHd4RhPHOinZp\"\n charset=utf-8 \n "    );
                        socketoutput.println("Subject: test 123 \nMime-Version: 1.0  \nContent-Transfer-Encoding: base64\n" +
                                " \nContent-Disposition: inline \nContent-Type: multipart/mixed;  boundary=\"17pEHd4RhPHOinZp\"\n charset=utf-8 \n "    );

                        //basic functionality with hard coded text (Basis, Feature 1, Feature 2)
                        socketoutput.println("--17pEHd4RhPHOinZp\n Content-Type: text/plain; charset=utf-8\n" +
                                "Content-Transfer-Encoding: base64\n"    );
                        socketoutput.println(encodedMime);
                        socketoutput.println("--17pEHd4RhPHOinZp"    );

                        //functionality with attached image (Feature 3)
                        socketoutput.println("Content-Type:image/png; name=pacman.png \n" +
                                "Content-Disposition: attachment; filename = \"pacman.png\" \n" +
                                "Content-Transfer-Encoding: base64\n"  );
                        socketoutput.println(encodedFile);
                        socketoutput.println("--17pEHd4RhPHOinZp "  );



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

    private static void sendJarPicViaSMTP(String encodedMime, String encodedFile) {

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
//                        socketoutput.println("Subject: test 123 \nMime-Version: 1.0  \nContent-Transfer-Encoding: base64\n" +
//                                " \nContent-Disposition: inline \nContent-Type: text/plain;  boundary=\"17pEHd4RhPHOinZp\"\n charset=utf-8 \n "    );
                        socketoutput.println("Subject: test 123 \nMime-Version: 1.0  \nContent-Transfer-Encoding: base64\n" +
                                " \nContent-Disposition: inline \nContent-Type: multipart/mixed;  boundary=\"17pEHd4RhPHOinZp\"\n charset=utf-8 \n "    );

                        //basic functionality with hard coded text (Basis, Feature 1, Feature 2)
                        socketoutput.println("--17pEHd4RhPHOinZp\n Content-Type: text/plain; charset=utf-8\n" +
                                "Content-Transfer-Encoding: base64\n"    );
                        socketoutput.println(encodedMime);
                        socketoutput.println("--17pEHd4RhPHOinZp"    );

                        //functionality with attached image (Feature 3)
                        socketoutput.println("Content-Type:Application/octet-stream; name=untitled.jpg \n" +
                                "Content-Disposition: attachment; filename = \"Untitled.jpg\" \n" +
                                "Content-Transfer-Encoding: base64\n"  );
                        socketoutput.println(encodedFile);
                        socketoutput.println("--17pEHd4RhPHOinZp "  );



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
