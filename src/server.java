import java.io.*;
import java.net.*;
import java.util.Date;

public class server {
    public static void main(String[] args) {

        displayServerBanner();

        try (ServerSocket serverSocket = new ServerSocket(20100)) {
            System.out.println("| O servidor foi iniciado! Aguardando conexoes na porta 20100");
            System.out.println();

            while (true) {
                Socket s1 = serverSocket.accept();
                String ip1 = s1.getInetAddress().toString().replace("/", "");
                System.out.println("| Conexao estabelecida com o client -> IP " + ip1);
                DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());

                String os = detectOS();
                updateServerTime(os);

                Date dt = new Date();
                System.out.println("| Data e hora enviada -> " + dt);
                dos1.writeLong(dt.getTime());

                System.out.println("| Conexao encerrada com o client -> IP " + ip1);
                System.out.println();
                System.out.println("| Aguardando conexoes na porta 20100");
                System.out.println();

                s1.close();
                dos1.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateServerTime(String os) throws Exception {
        if (os.equals("win")) {
            String[] win_command = {"cmd.exe", "/c", "net time /set /yes && w32tm /resync /force"};
            Runtime.getRuntime().exec(win_command);
        } else if (os.equals("linux")) {
            String[] linux_command = {"/bin/bash", "-c", "sudo ntpdate -u pool.ntp.org"};
            Runtime.getRuntime().exec(linux_command);
        } else throw new Exception("Sistema operacional nao suportado :(");
    }

    public static void displayServerBanner() {
        System.out.println();
        System.out.println("*******  TP2 - Sistemas Distribuidos  *******");
        System.out.println("*                                           *");
        System.out.println("*  Alunos: Arthur, Sweney e Victor          *");
        System.out.println("*  Professor: Theo Silva Lins               *");
        System.out.println("*                                           *");
        System.out.println("*********************************************");
        System.out.println();
    }

    public static String detectOS() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) return "win";
        else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) return "linux";
        else return "notsupported";
    }
}