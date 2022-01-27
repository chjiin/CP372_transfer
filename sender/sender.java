package Sender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class sender {

    public static Boolean RSetting = true; //whether reliable or not

    public static void main(String[] args) {

        System.out.println("Running GUI");
        JFrame frame = new JFrame("Transfer File");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);


        addComponentsToFrame(frame);
        //frame.pack();
        frame.setVisible(true);

    }
    public void parseFile(String path){

    }
    public static void addComponentsToFrame(JFrame frame) {
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();//this is for the frame
        //GridBagConstraints p = new GridBagConstraints();//this is for in panels if needed.
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.weightx = 1.0;
        c.weighty = 2.0;
        c.gridx = 0;

        //port panel
        JPanel portPanel = new JPanel();


        JTextField IPFeild = new JTextField("IP Address:", 10);
        JTextField portSFeild = new JTextField("Sender Port #:", 10);
        JTextField portRFeild = new JTextField("Receiver Port #:", 10);



        portPanel.add(IPFeild);
        portPanel.add(portSFeild);
        portPanel.add(portRFeild);


        //file panel
        JPanel filePanel = new JPanel();

        JTextField filefield = new JTextField("File name", 40);

        filePanel.add(filefield);

        //send panel
        JPanel sendPanel = new JPanel();

        JButton aliveButton = new JButton("Is Alive");
        JButton sendButton = new JButton("Send");
        JButton rulesButton = new JButton("Rules");

        sendPanel.add(aliveButton);
        sendPanel.add(sendButton);
        sendPanel.add(rulesButton);

        //time panel
        JPanel timePanel = new JPanel();

        JTextField timefeild = new JTextField("Timeout (in microseconds)", 20);
        JTextArea packetText = new JTextArea("Packets:");
        JTextArea packetDisplay = new JTextArea("0");

        timePanel.add(timefeild);
        timePanel.add(packetText);
        timePanel.add(packetDisplay);

        //reliable panel
        JPanel reliablePanel = new JPanel();

        JButton reliableButton = new JButton("Reliable");
        JTextArea leftArrow = new JTextArea("<<<<");
        JTextArea rightArrow = new JTextArea(">>>>");
        JButton unreliableButton = new JButton("Unreliable");

        reliablePanel.add(reliableButton);
        reliablePanel.add(leftArrow);
        reliablePanel.add(unreliableButton);

//adding to frame section---------------------------------------
        c.fill = GridBagConstraints.HORIZONTAL;


        c.gridy = 0;
        frame.add(portPanel, c);
        c.gridy = 1;
        frame.add(filePanel, c);
        c.gridy = 2;
        frame.add(sendPanel, c);
        c.gridy = 3;
        frame.add(timePanel, c);
        c.gridy = 4;
        frame.add(reliablePanel, c);

        aliveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    InetAddress IP;
                    if(IPFeild.getText().equals("IP Address:")){
                        IP = Inet4Address.getByName("127.0.0.1");
                    } else {
                        IP = Inet4Address.getByName(IPFeild.getText());
                    }
                    int timeValue;
                    if(timefeild.getText().equals("Timeout (in microseconds)")){
                        timeValue = 3500;
                    } else {
                        timeValue = Integer.parseInt(timefeild.getText());
                    }
                    int rport;
                    if(portRFeild.getText().equals("Receiver Port #:")){
                        rport = 4455;
                    } else {
                        rport = Integer.parseInt(portRFeild.getText());
                    }
                    int sport;
                    if(portSFeild.getText().equals("Sender Port #:")){
                        sport = 3321;
                    } else {
                        sport = Integer.parseInt(portSFeild.getText());
                    }
                    //variables are set

                    DatagramSocket dSocket = new DatagramSocket(sport);
                    byte[] send = "ping".getBytes(StandardCharsets.UTF_8);
                    byte[] receive = new byte[128];
                    DatagramPacket res = new DatagramPacket(receive, receive.length);
                    DatagramPacket ping = new DatagramPacket(send, send.length, IP, rport);
                    dSocket.send(ping);
                    dSocket.setSoTimeout(timeValue);
                    try{
                        dSocket.receive(res);
                        JOptionPane.showMessageDialog(null, "Received response, receiver is alive.");
                    }catch(SocketTimeoutException er){
                        JOptionPane.showMessageDialog(null, "Response timed out, receiver may be offline.");
                    }
                    dSocket.close();
                }catch (SocketException | UnknownHostException socketException) {
                    socketException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    InetAddress IP;
                    if(IPFeild.getText().equals("IP Address:")){
                        IP = Inet4Address.getByName("127.0.0.1");
                    } else {
                        IP = Inet4Address.getByName(IPFeild.getText());
                    }
                    int timeValue;
                    if(timefeild.getText().equals("Timeout (in microseconds)")){
                        timeValue = 3500;
                    } else {
                        timeValue = Integer.parseInt(timefeild.getText());
                    }
                    int rport;
                    if(portRFeild.getText().equals("Receiver Port #:")){
                        rport = 4455;
                    } else {
                        rport = Integer.parseInt(portRFeild.getText());
                    }
                    int sport;
                    if(portSFeild.getText().equals("Sender Port #:")){
                        sport = 3321;
                    } else {
                        sport = Integer.parseInt(portSFeild.getText());
                    }
                    //variables are set
                    //setup socket and get network info
                    int seq = 0;
                    DatagramSocket dSocket = new DatagramSocket(sport);

                    //get file, turn to string--------------------------
                    String sData = "";
                    System.out.println(filefield.getText());
                    InputStream input = sender.class.getResourceAsStream(filefield.getText());
                    InputStreamReader inputReader = new InputStreamReader(input);

                    BufferedReader br = new BufferedReader(inputReader);
                    StringBuilder sbData = new StringBuilder();
                    String line = br.readLine();
                    while(line!=null){
                        sbData.append(line);
                        sbData.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    sData = sbData.toString();
                    br.close();

                    System.out.println("sData : " + sData);
                    ArrayList<String> strlist = new ArrayList<>();

                    for(int i =0;i<=Math.ceil(sData.length()/31);i++){
                        strlist.add(sData.substring(i*31, Math.min((i+1)*31, sData.length())));
                    }
                    System.out.println(strlist.toString());
                    long startTime = System.currentTimeMillis();
                    int i = 1;
                    boolean timeout = false;

                    for(String s: strlist){
                        if(!timeout){
                            s = Integer.toString(seq) + s;
                        }else{
                            timeout= false;
                            s = Integer.toString(seq) + s;
                        }
                        byte[] data = s.getBytes(StandardCharsets.UTF_8);
                        DatagramPacket dPack = new DatagramPacket(data, data.length, IP, rport);
                        if(RSetting || i%10!=0){
                            dSocket.send(dPack);
                            System.out.println("sent");
                        }
                        packetDisplay.setText(String.valueOf(Integer.parseInt(packetDisplay.getText()) + 1));
                        System.out.println("i: " + i);
                        dSocket.setSoTimeout(timeValue);
                        try {
                            dSocket.receive(dPack);
                        }catch(SocketTimeoutException ex){
                            System.out.println("TIMED OUT");
                            timeout = true;
                            i++;
                            continue;
                        }
                        String senderMsg = new String(dPack.getData(), 0, dPack.getLength());
                        System.out.println("packet: " + senderMsg);
                        //System.out.println("remaning: " + sData + "\n");
                        System.out.println("Sent to: " + IP + " " + rport + "");
                        System.out.println("Receiver response: " + dPack + "\n");
                        if(seq == 0){
                            seq = 1;
                        } else {
                            seq = 0;
                        }
                        i++;
                    }
                    long endTime = System.currentTimeMillis();
                    long time = endTime - startTime;
                    String EOTMsg = "\nTOTAL TIME: " + String.valueOf(time) + "ms";
                    byte[] bEOT = EOTMsg.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket EOT = new DatagramPacket(bEOT, bEOT.length, IP, rport);
                    System.out.println("SENT EOT");
                    dSocket.send(EOT);

                    dSocket.close();

                    //display end message
                    String endMsg = "Transmission completed with a total time of " + String.valueOf(time) + "ms";
                    JOptionPane.showMessageDialog(null, endMsg);
                } catch (SocketException socketException) {
                    socketException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        reliableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String setR = "Reliability now set to Reliable";

                if (RSetting == false){
                    JOptionPane.showMessageDialog(null, setR);

                    reliablePanel.remove(rightArrow);
                    reliablePanel.remove(unreliableButton);
                    reliablePanel.add(leftArrow);
                    reliablePanel.add(unreliableButton);
                    reliablePanel.revalidate();
                    reliablePanel.repaint();
                    RSetting = true;
                }

            }
        });

        unreliableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String setU = "Reliability now set to Unreliable";

                if (RSetting == true){
                    JOptionPane.showMessageDialog(null, setU);

                    reliablePanel.remove(leftArrow);
                    reliablePanel.remove(unreliableButton);
                    reliablePanel.add(rightArrow);
                    reliablePanel.add(unreliableButton);
                    reliablePanel.revalidate();
                    reliablePanel.repaint();
                    RSetting = false;
                }
            }
        });

        rulesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String rules = "Rules:\nDo not include \"IP Address:\", \"Sender Port #:\", etc. in your input\nInclude .txt in your file name\nTimeout and port number inputs must be integers\n" +
                        "By default: timeout = 3,500 microseconds, IP Address = 127.0.0.1\nSender Port # = 3321, Receiver Port # = 4455\nAnd transmission type is set to reliable";
                JOptionPane.showMessageDialog(null, rules);


            }
        });
    }




}

