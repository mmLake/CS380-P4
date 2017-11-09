

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Ipv6Client {

    private final static int totalPackets = 12;
    private final static int headerSize = 40;
    private final static String ipAddress = "18.221.102.182";
    private final static int port = 38004;

    public static void main(String[] args){
        Ipv6Client client = new Ipv6Client();
        client.main();
    }

    public void main(){
        try (Socket socket = new Socket(ipAddress, port)) {

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            byte[] packet;
            byte[] destAddress = socket.getInetAddress().getAddress();
            byte[] srcAddress = {127,0,0,1};

            for (int i=0; i < totalPackets; i++) {
                int dataSize = (int)java.lang.Math.pow(2,(i+1));
                int packetSize = headerSize + dataSize;
                packet = new byte[packetSize];

                packet[0] = 0x60; //version and traffic class[0]
                packet[1] = 0; //traffic class[1] and flow label
                packet[2] = 0; 
                packet[3] = 0;
                packet[4] = (byte)((dataSize >>> 8) & 0xFF); //payload length
                packet[5] = (byte)(dataSize & 0xFF);
                packet[6] = 0x11; //next header
                packet[7] = 0x14; //hop limit
                packet[8] = 0; //source address (128b = 16B)
                packet[9] = 0; 
                packet[10] = 0;
                packet[11] = 0;
                packet[12] = 0; 
                packet[13] = 0;
                packet[14] = 0;
                packet[15] = 0;
                packet[16] = 0; 
                packet[17] = 0;
                packet[18] = (byte)0xFF;
                packet[19] = (byte)0xFF;
                packet[20] = srcAddress[0]; 
                packet[21] = srcAddress[1];
                packet[22] = srcAddress[2];
                packet[23] = srcAddress[3];
                packet[24] = 0; //dest address (128b = 16B)
                packet[25] = 0; 
                packet[26] = 0;
                packet[27] = 0;
                packet[28] = 0; 
                packet[29] = 0;
                packet[30] = 0;
                packet[31] = 0;
                packet[32] = 0; 
                packet[33] = 0;
                packet[34] = (byte)0xFF;
                packet[35] = (byte)0xFF;
                packet[36] = destAddress[0]; 
                packet[37] = destAddress[1];
                packet[38] = destAddress[2];
                packet[39] = destAddress[3];

                //enter the data values as zero's
                for (int k=40; k<packet.length;k++){
                    packet[k] = 0;
                }

                //print packet number
                System.out.println("Packet length " + packet.length);

                //send packet
                os.write(packet);

                //print whether or not guess is correct
                for(int l=0;l<4;l++){
                	byte value = (byte)is.read();
                	System.out.print(String.format("%02X", value));
                }
                System.out.println();
            }

        }catch(IOException e){
            System.out.println(e);
        }catch (Error e){
            System.out.println(e);
        }
    }
}
