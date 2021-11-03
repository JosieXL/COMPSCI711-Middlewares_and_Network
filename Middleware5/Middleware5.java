import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Middleware5 {
    private static JFrame f;
    private static JPanel panelSend, panelReceived, panelReady, panel, panelBottom, panelButton;
    private static JLabel lSend, lReceived, lReady;
	private static JTextArea textAreaSendList, textAreaReceivedList, textAreaReadyList;
	private static JButton jbSend;
    private ArrayList<String> holdingQueue = new ArrayList<>();
    private int receivedMessageNumber = 0;
    private int sendMessageNumber = 1;
    private static boolean isReceivedMessage = false;

	public static void main(String[] args) {
		new Middleware5();
	}

	Middleware5() {
		init();
	}

	public void init() {
        f = new JFrame("Middleware 5");
        System.out.println("Creating Middleware 5 frame!");
        f.setBounds(500, 500, 1500, 1000);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font = new Font("Arial", Font.BOLD, 25);
        Font fontTextArea = new Font("Arial", Font.PLAIN, 25);

        // Define three JLabel, one JButton and three JTextArea
        lSend = new JLabel("Send");
        lReceived = new JLabel("Received");
        lReady = new JLabel("Ready");
        jbSend = new JButton("Send");
        textAreaSendList = new JTextArea(15, 15);
        textAreaReceivedList = new JTextArea(15, 15);
        textAreaReadyList = new JTextArea(15, 15);    
        
        jbSend.setFont(font);
        lSend.setFont(font);
        lReceived.setFont(font);
        lReady.setFont(font);
        textAreaSendList.setFont(fontTextArea);
        textAreaReceivedList.setFont(font);
        textAreaReadyList.setFont(font);

        // Set content features in textArea
        textAreaSendList.setLineWrap(true);
        textAreaSendList.setWrapStyleWord(true);
        textAreaSendList.setEditable(false);
        textAreaReceivedList.setLineWrap(true);
        textAreaReceivedList.setWrapStyleWord(true);
        textAreaReceivedList.setEditable(false);
        textAreaReadyList.setLineWrap(true);
        textAreaReadyList.setWrapStyleWord(true);
        textAreaReadyList.setEditable(false);


         
        // Define the panels
        panelSend = new JPanel();
        panelReceived = new JPanel();
        panelReady = new JPanel();
        panel = new JPanel();
        panelBottom = new JPanel();
        panelButton = new JPanel();
         
        // Set up the BoxLayout
        BoxLayout layout1 = new BoxLayout(panelSend, BoxLayout.Y_AXIS);
        BoxLayout layout2 = new BoxLayout(panelReceived, BoxLayout.Y_AXIS);
        BoxLayout layout3 = new BoxLayout(panelReady, BoxLayout.Y_AXIS);
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        BoxLayout layoutX = new BoxLayout(panelBottom, BoxLayout.X_AXIS);
        BoxLayout layoutXButton = new BoxLayout(panelButton, BoxLayout.X_AXIS);
        panelSend.setLayout(layout1);
        panelReceived.setLayout(layout2);
        panelReady.setLayout(layout3);
        panel.setLayout(layout);
        panelBottom.setLayout(layoutX);
        panelButton.setLayout(layoutXButton);
         
        // Add the label and textArea with gap into the panel
        lSend.setAlignmentX(Component.CENTER_ALIGNMENT);
        textAreaSendList.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSend.add(lSend);
        panelSend.add(Box.createRigidArea(new Dimension(0, 40)));
        panelSend.add(textAreaSendList);
         
        lReceived.setAlignmentX(Component.CENTER_ALIGNMENT);
        textAreaReceivedList.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelReceived.add(lReceived);
        panelReceived.add(Box.createRigidArea(new Dimension(0, 40)));
        panelReceived.add(textAreaReceivedList);
         
        lReady.setAlignmentX(Component.CENTER_ALIGNMENT);
        textAreaReadyList.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelReady.add(lReady);
        panelReady.add(Box.createRigidArea(new Dimension(0, 40)));
        panelReady.add(textAreaReadyList);

        // Add button into panel with gap area on the right
        jbSend.setAlignmentX(Component.LEFT_ALIGNMENT);
        jbSend.setMargin(new Insets(5,30,5,30));
        panelButton.add(jbSend);
        panelButton.add(Box.createRigidArea(new Dimension(900, 0)));
         
        // Add the panels into the frame
        panelBottom.add(Box.createRigidArea(new Dimension(80, 0)));
        panelBottom.add(panelSend);
        panelBottom.add(Box.createRigidArea(new Dimension(80, 0)));
        panelBottom.add(panelReceived);
        panelBottom.add(Box.createRigidArea(new Dimension(80, 0)));
        panelBottom.add(panelReady);
        panelBottom.add(Box.createRigidArea(new Dimension(80, 0)));
        panel.add(Box.createRigidArea(new Dimension(0, 80)));
        panel.add(panelButton);
        panel.add(Box.createRigidArea(new Dimension(0, 80)));
        panel.add(panelBottom);
        panel.add(Box.createRigidArea(new Dimension(0, 80)));
        f.add(panel);

        f.pack();
        f.setResizable(false);
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        jbSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageSend = "Msg #" + sendMessageNumber + " from Middleware 5 <EOM>";
                sendMessageToNetwork(messageSend);
                textAreaSendList.append(messageSend);
                textAreaSendList.append("\n");
                sendMessageNumber += 1;
                System.out.println("click on button");
            }
        });

        while (true) {
            isReceivedMessage = false;
            System.out.println("displaying info...");
            String messageFromNetwork = getMessage();
            if (messageFromNetwork.contains("Msg")) {
                isReceivedMessage = true;
                textAreaReceivedList.append(messageFromNetwork);
                textAreaReceivedList.append("\n");

                //original: Msg #1 from Middleware 1 <EOM>
                //now: Msg #1 from Middleware 1 timestamp x <EOM>
                String addTimestamp1 = "timestamp " + Integer.toString(receivedMessageNumber) + " ";
                String EOM = messageFromNetwork.substring(messageFromNetwork.length()-5);
                int signIndex = messageFromNetwork.indexOf("<");
                String firstPart = messageFromNetwork.substring(0, signIndex);
                String result1 = firstPart + addTimestamp1 + EOM;
                System.out.println(result1);
                holdingQueue.add(result1);
                System.out.println("holdingQueue1: "+ holdingQueue); 
                receivedMessageNumber += 1;
                System.out.println("success display the contents");

                if (isReceivedMessage == true) {
                    if (messageFromNetwork.contains("Middleware 1")) {
                        //当message from sender为“please send timestamp”
                        String requestMessage = "Please send the timestamp";
                        String messageMatch = getMessage();
                        System.out.println(messageMatch);
                        if (messageMatch.equals( requestMessage)) {
                            System.out.println("request message matched.");
                            sendTimestampToM1();
                            String updatedMessage = getMessage();
                            System.out.println("updatedMessage: " + updatedMessage);
                            updateHoldingQueue(updatedMessage);
                        }
                        System.out.println("holdingQueue: "+ holdingQueue); 
                    }
                    else if (messageFromNetwork.contains("Middleware 2")) {
                        String requestMessage = "Please send the timestamp";
                        String messageMatch = getMessage();
                        System.out.println("messageMatch: " + messageMatch);
                        if (messageMatch.equals( requestMessage)) {
                            System.out.println("request message matched.");
                            sendTimestampToM2();
                            String updatedMessage = getMessage();
                            System.out.println("updatedMessage: " + updatedMessage);
                            updateHoldingQueue(updatedMessage);
                        }
                        System.out.println("holdingQueue: "+ holdingQueue); 
                    }
                    else if (messageFromNetwork.contains("Middleware 3")) {
                        String requestMessage = "Please send the timestamp";
                        String messageMatch = getMessage();
                        System.out.println("messageMatch: " + messageMatch);
                        if (messageMatch.equals( requestMessage)) {
                            System.out.println("request message matched.");
                            sendTimestampToM3();
                            String updatedMessage = getMessage();
                            System.out.println("updatedMessage: " + updatedMessage);
                            updateHoldingQueue(updatedMessage);
                        }
                        System.out.println("holdingQueue: "+ holdingQueue); 
                    }
                    else if (messageFromNetwork.contains("Middleware 4")) {
                        String requestMessage = "Please send the timestamp";
                        String messageMatch = getMessage();
                        System.out.println("messageMatch: " + messageMatch);
                        if (messageMatch.equals( requestMessage)) {
                            System.out.println("request message matched.");
                            sendTimestampToM4();
                            String updatedMessage = getMessage();
                            System.out.println("updatedMessage: " + updatedMessage);
                            updateHoldingQueue(updatedMessage);
                        }
                        System.out.println("holdingQueue: "+ holdingQueue); 
                    }
                    else if (messageFromNetwork.contains("Middleware 5")) {
                        int count = 0;
                        ArrayList<Integer> timestampList = new ArrayList<>();
                        timestampList.add(receivedMessageNumber);
                        // timestampList includes format each middleware's currentReceivedNumber
                        
                        try {
                            System.out.println("Sleeping...");
                            Thread.sleep(20000);
                            System.out.println("Sleeping end...");
                        }
                        catch(Exception ex) {
                            Thread.currentThread().interrupt();
                        }
                        
                        sendRequestTimestampToMiddleware();
                        ServerSocket serverSocket;
                        try {
                            serverSocket = new ServerSocket(8086);
                            while (count < 4) {
                                int[] TSC = getTimestampFromOtherMiddleware(count, serverSocket);
                                int timeStamp = TSC[0];
                                count = TSC[1];
                                timestampList.add(timeStamp);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        Integer max = Collections.max(timestampList);
                        String addTimestamp = "timestamp " + Integer.toString(max) + " ";
                        //original: Msg #1 from Middleware 1 timestamp x <EOM>
                        //updated: Msg #1 from Middleware 1 timestamp 3 <EOM>*
                        String updatedMessage = firstPart + addTimestamp + EOM + "*";
                        System.out.println(updatedMessage);

                        sendAgreedTimeStampToOtherMiddleware(updatedMessage);
                        updateHoldingQueue(updatedMessage);
                        System.out.println("holdingQueue: "+ holdingQueue);  
                    }
                }
            
                if (holdingQueue.size() != 0) {
                    resortHoldingQueue();
                    ifReadyMessage();
                }
            }
        }

    }

    private void sendMessageToNetwork(String messageSend) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8081);
            System.out.println("sendRequestInfoToServer socket 8081 Connected!");
            System.out.println("Sending string to the Network.");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending string to the Network.");
            dataOutputStreamToServer.writeUTF(messageSend);
            dataOutputStreamToServer.flush();
            dataOutputStreamToServer.close();
            System.out.println("socket 8081 is closing...");
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getMessage() {
        String requestMessage = "";
        try {
            ServerSocket serverSocket = new ServerSocket(8086);
            //serverSocket.setSoTimeout(10000);
            System.out.println("ServerSocket 8086 connections...");
            Socket socket = serverSocket.accept();
            System.out.println("Connection from " + socket + "!");
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStreamFromClient = new DataInputStream(inputStream);
            requestMessage = dataInputStreamFromClient.readUTF();
            System.out.println("The message sent from the network was: " + requestMessage);
            System.out.println("Closing socket 8086.");
            serverSocket.close();
            socket.close();
        }
        catch (Exception e) {
            //return "";
            e.printStackTrace();
        }
        return requestMessage;
    }

    private void sendTimestampToM2() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8083);
            System.out.println("sendTimestampToM2 socket 8083 Connected!");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending int...");
            dataOutputStreamToServer.writeInt(receivedMessageNumber);
            dataOutputStreamToServer.flush();
            System.out.println("finish sending int...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendTimestampToM3() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8084);
            System.out.println("sendTimestampToM3 socket 8084 Connected!");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending int...");
            dataOutputStreamToServer.writeInt(receivedMessageNumber);
            dataOutputStreamToServer.flush();
            System.out.println("finish sending int...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendTimestampToM4() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8085);
            System.out.println("sendTimestampToM4 socket 8085 Connected!");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending int...");
            dataOutputStreamToServer.writeInt(receivedMessageNumber);
            dataOutputStreamToServer.flush();
            System.out.println("finish sending int...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendTimestampToM1() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8082);
            System.out.println("sendTimestampToM1 socket 8082 Connected!");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending int...");
            dataOutputStreamToServer.writeInt(receivedMessageNumber);
            dataOutputStreamToServer.flush();
            System.out.println("finish sending int...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int[] getTimestampFromOtherMiddleware(int count, ServerSocket serverSocket) {
        int t = 0;
        int[] output = new int[2];
        try {
            //serverSocket.setSoTimeout(10000);
            System.out.println("ServerSocket 8086 connections...");
            Socket socket = serverSocket.accept();
            System.out.println("Connection from " + socket + "!");
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStreamFromMiddleware = new DataInputStream(inputStream);
            t = dataInputStreamFromMiddleware.readInt();
            System.out.println("The TS sent from the network was: " + t);
            count++;
            output[0] = t;
            output[1] = count;            
        }
        catch (Exception e) {
            //return "";
            e.printStackTrace();
        }
        return output;
    }

    private void sendAgreedTimeStampToOtherMiddleware(String agreedTimestampString) {
        int[] otherMiddlewarePorts = { 8082, 8083, 8084, 8085 };
        try {
            for (int i=0; i<otherMiddlewarePorts.length; i++) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                Socket socket = new Socket(inetAddress, otherMiddlewarePorts[i]);
                System.out.println("sendRequestInfoToServer socket " + otherMiddlewarePorts[i] + " Connected!");
                System.out.println("Sending string to the middleware " + (i+2));
                OutputStream outputStreamFromSocket = socket.getOutputStream();
                DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
                System.out.println("Sending string to the middleware " + (i+2));
                dataOutputStreamToServer.writeUTF(agreedTimestampString);
                dataOutputStreamToServer.flush();
                dataOutputStreamToServer.close();
                System.out.println("socket " + otherMiddlewarePorts[i] + " is closing...");
                socket.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequestTimestampToMiddleware() {
        int[] otherMiddlewarePorts = { 8082, 8083, 8084, 8085 };
        try {
            for (int i=0; i<otherMiddlewarePorts.length; i++) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                Socket socket = new Socket(inetAddress, otherMiddlewarePorts[i]);
                System.out.println("sendRequestTimestampToMiddleware socket " + otherMiddlewarePorts[i] + " Connected!");
                System.out.println("Sending string to the middleware " + (i+2));
                OutputStream outputStreamFromSocket = socket.getOutputStream();
                DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
                System.out.println("Sending string to the middleware " + (i+2));
                dataOutputStreamToServer.writeUTF("Please send the timestamp");
                dataOutputStreamToServer.flush();
                dataOutputStreamToServer.close();
                System.out.println("socket " + otherMiddlewarePorts[i] + " is closing...");
                socket.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //original: Msg #1 from Middleware 1 timestamp x <EOM>
    //updated: Msg #1 from Middleware 1 timestamp 3 <EOM>*
    private void updateHoldingQueue(String updatedMessage) {
        for (int i=0; i<holdingQueue.size(); i++) {
            String messageNo = holdingQueue.get(i).substring(holdingQueue.get(i).indexOf("#")+1, holdingQueue.get(i).indexOf("from")-1);
            String updateMessageNo = updatedMessage.substring(updatedMessage.indexOf("#")+1, updatedMessage.indexOf("from")-1);
            String middlewareNo = holdingQueue.get(i).substring(holdingQueue.get(i).indexOf("ware")+5, holdingQueue.get(i).indexOf("time")-1);
            String updateMessageMiddlewareNo = updatedMessage.substring(updatedMessage.indexOf("ware")+5, updatedMessage.indexOf("time")-1);
            if ((messageNo.equals(updateMessageNo)) && (middlewareNo.equals(updateMessageMiddlewareNo))) {
                holdingQueue.set(i, updatedMessage);
            }
        }
    }

    // resort holding queue - the smallest timestamp is the first, if the timestamp are equal, the smallest middleware ID is the first one.
    private void resortHoldingQueue() {
        for (int i=0; i<holdingQueue.size(); i++){
            int timestamp = Integer.parseInt(holdingQueue.get(i).substring(holdingQueue.get(i).indexOf("stamp")+6, holdingQueue.get(i).indexOf("<")-1));
            int middlewareId = Integer.parseInt(holdingQueue.get(i).substring(holdingQueue.get(i).indexOf("ware")+5, holdingQueue.get(i).indexOf("time")-1));

            for (int j=0; j<holdingQueue.size(); j++) {
                int timestamp1 = Integer.parseInt(holdingQueue.get(j).substring(holdingQueue.get(j).indexOf("stamp")+6, holdingQueue.get(j).indexOf("<")-1));
                int middlewareId1 = Integer.parseInt(holdingQueue.get(j).substring(holdingQueue.get(j).indexOf("ware")+5, holdingQueue.get(j).indexOf("time")-1));
                if (timestamp < timestamp1){
                    Collections.swap(holdingQueue, i, j);
                }
                else if (timestamp == timestamp1) {
                    if (middlewareId < middlewareId1){
                        Collections.swap(holdingQueue, i, j);
                    }
                }
                
            }

        }
    }

    private void ifReadyMessage() {
        String finalCharForFirstElementOfList = holdingQueue.get(0).substring(holdingQueue.get(0).length() - 1);
        if (finalCharForFirstElementOfList.equals("*")){
            textAreaReadyList.append(holdingQueue.get(0));
            textAreaReadyList.append("\n");
            holdingQueue.remove(0);
        }
    }
}



    
    
