# CP372_transfer
Reliable UDP file transfer protocol implemented on top of DatagramSocket API in Java, Stop-and-Wait 3.0.

Created by Chen Jin and Anthony Siprak. 

##Installation
Sender and receiver can be compiled using ```javac *.java```

##Features and User Commands
Sender has a GUI implemented in Java Swing featuring receiver IP input, sender UDP port input, receiver UDP port input. GUI also features reliable and simulated unreliable toggle.

Receiver is a command line application accepting sender IP, receiver UDP port, sender UDP, port, and transfer destination as arguments. 
Example: ```java Receiver 129.122.0.10 4455 3321 received.txt```


## Implementation Details
The text is initially taken from a text file and converted into a string. The string is then divided into chunks of size 31 bytes and placed into an ArrayList. Packets are filled by iterating this arraylist and converting each chunk of string into byte arrays. Our transfer protocol makes use of 32 byte MDS, containing 1 byte of overhead to store an integer (1 or 0) at the start representing the sequence number. The other 31 bytes store the text being transferred. Timing for transfers listed below:

219 Byte file with 3500ms timeout: 
- Average Time (reliable): 5ms 
- Unreliable transmission not applicable because file can be sent in under 10 packets with 32 byte MDS

1.43 Kilobyte file with 3500ms timeout: 
- Average Time (reliable): 17ms 
- Average Time (unreliable): 14019ms 

3.16 Megabyte file with 500ms timeout: 
- Average Time (reliable): 147574ms 
- Average Time (unreliable): 182154ms
