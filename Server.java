import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Server extends JFrame {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    public Server(){
        try {
            server = new ServerSocket(7777);
            System.out.println("server is ready to accept the connection");
            System.out.println("waiting..");
            socket=server.accept();
            br =new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out =new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                String contentToSend=messageInput.getText();
                messageArea.append("Me: "+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                messageInput.setText("");
                messageInput.requestFocus();
                }
            }
        });
    }

    private void createGUI() {
        this.setTitle("Server Messanager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

    //    heading.setIcon(new ImageIcon("ClientLogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        //frame layout set
        this.setLayout(new BorderLayout());

        //adding the component to frame
        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
    }

    private void startWriting() {
        //thread provides by reading
        Runnable r1=()->{
            System.out.println("writer started");
            try{
            while(!socket.isClosed())
            {
                    BufferedReader br1=new BufferedReader(new
                            InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        br1.readLine();
                    }
            }
            System.out.println("Connection is closed");
            }catch(Exception e)
            {
                // e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();
    }

    public void startReading() {
        Runnable r2=()->{
            System.out.println("reader started..");
            try{
                while(true)
                {
                    String msg= br.readLine();
                    if(msg.equals("exit")){
                        // System.out.println("Client terminated the chat");
                         JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                         messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                //    System.out.println("client:"+msg);
                 messageArea.append("Client :"+msg+"\n");    
                }
            } catch (IOException e) {
                    // e.printStackTrace();
                    System.out.println("Connection is closed");
                }
        };
        new Thread(r2).start();
    }

    public static void  main(String[] args){
        System.out.println("this is server.. going to start server");
        new Server();
    }
}
