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

public class Middleware2 {
    private static JFrame f;
    private static JPanel panelSend, panelReceived, panelReady, panel, panelBottom, panelButton;
    private static JLabel lSend, lReceived, lReady;
	private static JTextArea textAreaSendList, textAreaReceivedList, textAreaReadyList;
	private static JButton jbSend;
    private ArrayList<String> holdingQueue = new ArrayList<>();
    private int receivedMessageNumber = 0;
    private int sendMessageNumber = 0;

	public static void main(String[] args) {
		new Middleware2();
	}

	Middleware2() {
		init();
	}

	public void init() {
        f = new JFrame("Middleware 2");
        System.out.println("Creating Middleware 2 frame!");
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
                sendMessageToNetwork();
                sendMessageNumber += 1;
                System.out.println("success send the info message request to server");
                System.out.println("click on get info button");
                //displayMessage();
                
            }
        });

        while (true) {
            System.out.println("displaying info...");
            String messageFromNetwork = getMessageFromNetwork();
            textAreaReceivedList.append(messageFromNetwork);
            textAreaReceivedList.append("\n");
            holdingQueue.add(messageFromNetwork);
            System.out.println("holdingQueue1: "+ holdingQueue); 
            receivedMessageNumber += 1;
            System.out.println("success display the contents");
            
            if (messageFromNetwork.contains("Middleware 2")) {
                int count = 0;
                ArrayList<Integer> timestampList = new ArrayList<>();
                timestampList.add(receivedMessageNumber);
                // timestampList includes format each middleware's currentReceivedNumber

                while (count < 4) {
                    String timeStamp = getTimestampFromOtherMiddleware();
                    timestampList.add(Integer.parseInt(timeStamp));
                    count++;
                }
                Integer max = Collections.max(timestampList);
                System.out.println(max);
                String addTimestamp = "timestamp " + Integer.toString(max) + " ";
                String EOM = messageFromNetwork.substring(messageFromNetwork.length()-5);
                int signIndex = messageFromNetwork.indexOf("<");
                String firstPart = messageFromNetwork.substring(0, signIndex);
                System.out.println(EOM);
                System.out.println(firstPart);
                String result = firstPart + addTimestamp + EOM + "*";
                System.out.println(result);
                sendAgreedTimeStampToOtherMiddleware(result);
                //original: Msg #1 from Middleware 1 <EOM>
                //updated: Msg #1 from Middleware 1 timestamp 3 <EOM>*

                for (int i=0; i<holdingQueue.size(); i++) {
                    if (holdingQueue.get(i).equals(messageFromNetwork)) {
                        holdingQueue.set(i, result);
                    }
                }
                System.out.println("holdingQueue: "+ holdingQueue);  
            }
            else if (messageFromNetwork.contains("Middleware 1")) {
                sendTimestampToM1();
                String updatedMessage = getTimestampFromOtherMiddleware();
                System.out.println(updatedMessage);
                for (int i=0; i<holdingQueue.size(); i++) {
                    if (holdingQueue.get(i).equals(messageFromNetwork)) {
                        holdingQueue.set(i, updatedMessage);
                    }
                }
                System.out.println("holdingQueue: "+ holdingQueue); 
            } 
            else if (messageFromNetwork.contains("Middleware 3")) {
                sendTimestampToM3();
                String updatedMessage = getTimestampFromOtherMiddleware();
                System.out.println(updatedMessage);
                for (int i=0; i<holdingQueue.size(); i++) {
                    if (holdingQueue.get(i).equals(messageFromNetwork)) {
                        holdingQueue.set(i, updatedMessage);
                    }
                }
                System.out.println("holdingQueue: "+ holdingQueue); 
            } 
            else if (messageFromNetwork.contains("Middleware 4")) {
                sendTimestampToM4();
                String updatedMessage = getTimestampFromOtherMiddleware();
                System.out.println(updatedMessage);
                for (int i=0; i<holdingQueue.size(); i++) {
                    if (holdingQueue.get(i).equals(messageFromNetwork)) {
                        holdingQueue.set(i, updatedMessage);
                    }
                }
                System.out.println("holdingQueue: "+ holdingQueue); 
            }
            else if (messageFromNetwork.contains("Middleware 5")) {
                sendTimestampToM5();
                String updatedMessage = getTimestampFromOtherMiddleware();
                System.out.println(updatedMessage);
                for (int i=0; i<holdingQueue.size(); i++) {
                    if (holdingQueue.get(i).equals(messageFromNetwork)) {
                        holdingQueue.set(i, updatedMessage);
                    }
                }
                System.out.println("holdingQueue: "+ holdingQueue); 
            }
            resortHoldingQueue();
            ifReadyMessage();
            
        }

    }

    public void sendMessageToNetwork() {
        String messageM2 = "Msg #" + sendMessageNumber + " from Middleware 2 <EOM>";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8081);
            System.out.println("sendRequestInfoToServer socket 8081 Connected!");
            System.out.println("Sending string to the Network.");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending string to the Network.");
            dataOutputStreamToServer.writeUTF(messageM2);
            textAreaSendList.append(messageM2);
            textAreaSendList.append("\n");
            dataOutputStreamToServer.flush();
            dataOutputStreamToServer.close();
            System.out.println("socket 8081 is closing...");
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getMessageFromNetwork() {
        String requestMessage = "";
        try {
            ServerSocket serverSocket = new ServerSocket(8083);
            //serverSocket.setSoTimeout(10000);
            System.out.println("ServerSocket 8083 connections...");
            Socket socket = serverSocket.accept();
            System.out.println("Connection from " + socket + "!");
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStreamFromClient = new DataInputStream(inputStream);
            requestMessage = dataInputStreamFromClient.readUTF();
            System.out.println("The message sent from the network was: " + requestMessage);
            System.out.println("Closing socket 8083.");
            serverSocket.close();
            socket.close();
        }
        catch (Exception e) {
            //return "";
            e.printStackTrace();
        }
        return requestMessage;
    }

    public void displayMessage() {
        System.out.println("displaying info...");
        String messageFromNetwork = getMessageFromNetwork();
        textAreaReceivedList.append(messageFromNetwork);
        textAreaReceivedList.append("\n");
    }

    public void sendTimestampToM1() {
        String timestampM2 = Integer.toString(receivedMessageNumber);
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8082);
            System.out.println("sendRequestInfoToServer socket 8082 Connected!");
            System.out.println("Sending string to the Network.");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending string to the Network.");
            dataOutputStreamToServer.writeUTF(timestampM2);
            dataOutputStreamToServer.flush();
            dataOutputStreamToServer.close();
            System.out.println("socket 8082 is closing...");
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTimestampToM3() {
        String timestampM1 = Integer.toString(receivedMessageNumber);
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8084);
            System.out.println("sendRequestInfoToServer socket 8084 Connected!");
            System.out.println("Sending string to the Network.");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending string to the Network.");
            dataOutputStreamToServer.writeUTF(timestampM1);
            dataOutputStreamToServer.flush();
            dataOutputStreamToServer.close();
            System.out.println("socket 8084 is closing...");
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTimestampToM4() {
        String timestampM1 = Integer.toString(receivedMessageNumber);
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8085);
            System.out.println("sendRequestInfoToServer socket 8085 Connected!");
            System.out.println("Sending string to the Network.");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending string to the Network.");
            dataOutputStreamToServer.writeUTF(timestampM1);
            dataOutputStreamToServer.flush();
            dataOutputStreamToServer.close();
            System.out.println("socket 8085 is closing...");
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTimestampToM5() {
        String timestampM1 = Integer.toString(receivedMessageNumber);
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8086);
            System.out.println("sendRequestInfoToServer socket 8086 Connected!");
            System.out.println("Sending string to the Network.");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending string to the Network.");
            dataOutputStreamToServer.writeUTF(timestampM1);
            dataOutputStreamToServer.flush();
            dataOutputStreamToServer.close();
            System.out.println("socket 8086 is closing...");
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTimestampFromOtherMiddleware() {
        String requestMessage = "";
        try {
            ServerSocket serverSocket = new ServerSocket(8083);
            //serverSocket.setSoTimeout(10000);
            System.out.println("ServerSocket 8083 connections...");
            Socket socket = serverSocket.accept();
            System.out.println("Connection from " + socket + "!");
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStreamFromClient = new DataInputStream(inputStream);
            requestMessage = dataInputStreamFromClient.readUTF();
            System.out.println("The message sent from the network was: " + requestMessage);
            System.out.println("Closing socket 8083.");
            serverSocket.close();
            socket.close();
        }
        catch (Exception e) {
            //return "";
            e.printStackTrace();
        }
        return requestMessage;
    }

    public void sendAgreedTimeStampToOtherMiddleware(String agreedTimestampString) {
        int[] otherMiddlewarePorts = { 8082, 8084, 8085, 8086 };
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            for (int i=0; i<otherMiddlewarePorts.length; i++) {
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

    // resort holding queue - the smallest timestamp is the first, if the timestamp are equal, the smallest middleware ID is the first one.
    public void resortHoldingQueue() {
        for (int i=0; i<holdingQueue.size(); i++){
            int timestamp = Integer.parseInt(holdingQueue.get(i).substring(holdingQueue.get(i).indexOf("stamp")+6, holdingQueue.get(i).indexOf("<")-1));
            int middlewareId = Integer.parseInt(holdingQueue.get(i).substring(holdingQueue.get(i).indexOf("ware")+5, holdingQueue.get(i).indexOf("time")-1));
            System.out.println(timestamp);
            System.out.println(middlewareId);

            
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

    public void ifReadyMessage() {
        //message被updated过[在message后面加个*]，message在holdingQueue的第一位=>从holdingQueue中删除，readyList中append
        String finalCharForFirstElementOfList = holdingQueue.get(0).substring(holdingQueue.get(0).length() - 1);
        System.out.println(finalCharForFirstElementOfList);
        if (finalCharForFirstElementOfList.equals("*")){
            System.out.println("!");
            textAreaReadyList.append(holdingQueue.get(0));
            textAreaReadyList.append("\n");
            holdingQueue.remove(0);
        }
    }

}



    
    