package Receiver;
//127.0.0.1
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class receiver {
    public DatagramSocket dSocket;
    public static byte[] buffer = new byte[256];

    public static void main (String[] args) throws IOException {
        InetAddress senderIP = InetAddress.getByName(args[0]);
        int rPort = Integer.parseInt(args[1]);
        int sPort = Integer.parseInt(args[2]);
        String fName = args[3];
        System.out.println("senderIP: " + senderIP + " rPort: " + rPort + " sPort: " + sPort);
        System.out.println("start");
        DatagramSocket dSocket = new DatagramSocket(rPort);
        //byte[] seenSequenceNumbers = new byte[4];
        int seq = 1;
        String buildTxt = "";
        //most print statement sin here used for debugging
        while(true){
            try {

                DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length);
                System.out.println("Ready for Packet");
                dSocket.receive(dPacket);
                System.out.println("Packet Received");
                String rData = new String(dPacket.getData(), 0, dPacket.getLength());
                if(!rData.equals("ping") && !Character.isDigit(rData.charAt(0))){
                    System.out.println(rData);
                    System.out.println("EXITING THROUGH EOT");
                    break;
                }
                if(rData.equals("ping")){
                    dPacket = new DatagramPacket(buffer, buffer.length, senderIP, sPort);
                    dSocket.send(dPacket);
                    continue;
                }
                Character toStr = rData.charAt(0);
                String toParse = toStr.toString();
                if(Integer.parseInt(toParse) != seq){
                    seq = Math.abs(seq-1);
                    buildTxt = buildTxt + rData.substring(1);
                }
                //if(seenSequenceNumbers.add)
                String senderMsg = new String(dPacket.getData(), 0, dPacket.getLength());
                //System.out.println("Sequence no. :" + seqNum);
                System.out.println("test msg: " + senderMsg);
                //response packet
                dPacket = new DatagramPacket(buffer, buffer.length, senderIP, sPort);
                dSocket.send(dPacket);
                System.out.println("Sent ACK to " + sPort);
            }catch(Exception e){
                System.out.println(e);
            }

        }
        System.out.println("\nFINAL MESSAGE: \n" + buildTxt);
        try {
            File myObj = new File(fName);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(fName);
            myWriter.write(buildTxt);
            myWriter.close();

        } catch (IOException er) {
            System.out.println("An error occurred.");
            //er.printStackTrace();
        }
    }
}

