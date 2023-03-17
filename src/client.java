import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class client {
    public static void main(String[] args) throws IOException {

        displayClientBanner();

        String server_ip = "xxx.xxx.x.x"; // Your server ip
        Socket socket = new Socket(server_ip, 20100);
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        System.out.println("| A aplicacao foi iniciada!");
        System.out.println();
        System.out.println("| Conexao estabelecida com o servidor -> IP " + server_ip);

        long synchronized_client_time = getSynchronizedTimeAndShowRttStatistics(dis);
        Date synchronized_client_date = new Date(synchronized_client_time);
        changeSystemDateTime(synchronized_client_date);

        dis.close();
        socket.close();
    }

    public static long getSynchronizedTimeAndShowRttStatistics(DataInputStream dis) throws IOException {
        long before_req_time = new Date().getTime();
        long server_time = dis.readLong();
        long after_req_time = new Date().getTime();
        long process_delay_latency = after_req_time - before_req_time;
        long synchronized_client_time = server_time + process_delay_latency / 2;
        long synchronization_error = after_req_time - synchronized_client_time;
        Date after_req_date = new Date(after_req_time);
        Date server_date = new Date(server_time);

        System.out.println("| Latencia/Delay do processo -> " + (double) process_delay_latency / 1000.0 + " segundos");
        System.out.println("| Diferenca do tempo apos requisicao e hora sincronizada -> " + (double) synchronization_error / 1000.0 + " segundos");
        System.out.println("| Data e hora apos requisicao do client -> " + after_req_date);
        System.out.println("| Data e hora recebida do servidor -> " + server_date);

        return synchronized_client_time;
    }

    public static void changeSystemDateTime(Date synchronized_client_date) throws IOException {
        String os = detectOS();
        SimpleDateFormat date_pattern = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat time_pattern = new SimpleDateFormat("HH:mm:ss");
        String str_time = time_pattern.format(synchronized_client_date);
        String str_date = date_pattern.format(synchronized_client_date);

        if (os.equals("win")) {
            String[] win_command = {"cmd.exe", "/c", "time", str_time, "&", "date", str_date};

            Runtime.getRuntime().exec(win_command);
            System.out.println("| Data e hora do client sincronizadas -> " + synchronized_client_date);

        } else if (os.equals("linux")) {
            String[] linux_command = {"/bin/bash", "-c", "date -s '" + str_date + " " + str_time + "'"};

            Runtime.getRuntime().exec(linux_command);
            System.out.println("| Data e hora do client sincronizadas -> " + synchronized_client_date);

        } else {
            System.out.println("| A aplicacao ainda nao suporta o sistema operacional " + os + " :(");
        }

        System.out.println();
    }

    public static String detectOS() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) return "win";
        else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) return "linux";
        else return "notsupported";
    }

    public static void displayClientBanner() {
        System.out.println();
        System.out.println("*******  TP2 - Sistemas Distribuidos  *******");
        System.out.println("*                                           *");
        System.out.println("*  Alunos: Arthur, Sweney e Victor          *");
        System.out.println("*  Professor: Theo Silva Lins               *");
        System.out.println("*                                           *");
        System.out.println("*********************************************");
        System.out.println();
    }

}