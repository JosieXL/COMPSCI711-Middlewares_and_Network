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

public class Middleware3 {
    private static JFrame f;
    private static JPanel panelSend, panelReceived, panelReady, panel, panelBottom, panelButton;
    private static JLabel lSend, lReceived, lReady;
	private static JTextArea textAreaSendList, textAreaReceivedList, textAreaReadyList;
	private static JButton jbSend;    private ArrayList<String> holdingQueue = new ArrayList<>();
    private int receivedMessageNumber = 0;
    private int sendMessageNumber = 0;


	public static void main(String[] args) {
		new Middleware3();
	}

	Middleware3() {
		init();
	}

	public void init() {
        f = new JFrame("Middleware 3");
        System.out.println("Creating Middleware 3 frame!");
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

            //original: Msg #1 from Middleware 1 <EOM>
            //now: Msg #1 from Middleware 1 timestamp x <EOM>
            String addTimestamp1 = "timestamp " + Integer.toString(receivedMessageNumber) + " ";
            String EOM = messageFromNetwork.substring(messageFromNetwork.length()-5);
            int signIndex = messageFromNetwork.indexOf("<");
            String firstPart = messageFromNetwork.substring(0, signIndex);
            System.out.println(EOM);
            System.out.println(firstPart);
            String result1 = firstPart + addTimestamp1 + EOM;
            System.out.println(result1);
            holdingQueue.add(result1);
            System.out.println("holdingQueue1: "+ holdingQueue); 
            receivedMessageNumber += 1;
            System.out.println("success display the contents");
            /*
            if (messageFromNetwork.contains("Middleware 3")) {
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
                System.out.println(EOM);
                System.out.println(firstPart);
                String result = firstPart + addTimestamp + EOM + "*";
                System.out.println(result);
                sendAgreedTimeStampToOtherMiddleware(result);
                //original: Msg #1 from Middleware 1 timestamp x <EOM>
                //updated: Msg #1 from Middleware 1 timestamp 3 <EOM>*

                for (int i=0; i<holdingQueue.size(); i++) {
                    if (holdingQueue.get(i).equals(messageFromNetwork)) {
                        holdingQueue.set(i, result);
                    }
                }
                System.out.println("holdingQueue: "+ holdingQueue);  
            }
            else if (messageFromNetwork.contains("Middleware 2")) {
                sendTimestampToM2();
                String updatedMessage = getTimestampFromOtherMiddleware();
                System.out.println(updatedMessage);
                for (int i=0; i<holdingQueue.size(); i++) {
                    if (holdingQueue.get(i).equals(messageFromNetwork)) {
                        holdingQueue.set(i, updatedMessage);
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
            */
        }

    }

    public void sendMessageToNetwork() {
        String messageM3 = "Msg #" + sendMessageNumber + " from Middleware 3 <EOM>";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            Socket socket = new Socket(inetAddress, 8081);
            System.out.println("sendRequestInfoToServer socket 8081 Connected!");
            System.out.println("Sending string to the Network.");
            OutputStream outputStreamFromSocket = socket.getOutputStream();
            DataOutputStream dataOutputStreamToServer = new DataOutputStream(outputStreamFromSocket);
            System.out.println("Sending string to the Network.");
            dataOutputStreamToServer.writeUTF(messageM3);
            textAreaSendList.append(messageM3);
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
            ServerSocket serverSocket = new ServerSocket(8084);
            //serverSocket.setSoTimeout(10000);
            System.out.println("ServerSocket 8084 connections...");
            Socket socket = serverSocket.accept();
            System.out.println("Connection from " + socket + "!");
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStreamFromClient = new DataInputStream(inputStream);
            requestMessage = dataInputStreamFromClient.readUTF();
            System.out.println("The message sent from the network was: " + requestMessage);
            System.out.println("Closing socket 8084.");
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
        textAreaReceivedList.append("...");
        textAreaReceivedList.append("\n");
    }


}



    
    
