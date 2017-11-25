package com.eyeamthewon.editor;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    static final int PORT = 23066;
    private ServerSocket serverSocket = null;

    private String page;
    private final String BASE_PATH = "";

    private void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            while(true) {
                System.out.println("ACCEPTING CLIENTS");
                Socket socket = serverSocket.accept();
                System.out.println("CLIENT CONNECTING");
                NetThread nt = new NetThread(socket);
                System.out.println("CLIENT CREATED");
                nt.start();
                System.out.println("STARTED NEW CLIENT");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }



    class NetThread extends Thread {
        Socket socket;
        ObjectInputStream input;
        ObjectOutputStream output;

        NetThread(Socket socket) {
            this.socket = socket;
            System.out.println("CREATING NEW STREAMS");
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                System.out.println("STREAMS CREATED");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("RUNNING CLIENT");
            while(true) {
                try {
                    String data = input.readObject().toString();
                    System.out.println(data.substring(0,4));
                    if(data.startsWith(Integer.toString(NetCode.REQUEST_PAGE))) {
                        sendPageToEditor(data.replaceFirst(Integer.toString(NetCode.REQUEST_PAGE), ""));
                    } else if(data.startsWith(Integer.toString(NetCode.CREATE_PAGE))) {
                        createPage(data.replaceFirst(Integer.toString(NetCode.CREATE_PAGE), ""));
                    } else if(data.startsWith(Integer.toString(NetCode.UPDATE_PAGE))) {
                        updatePage(data.replaceFirst(Integer.toString(NetCode.UPDATE_PAGE), ""));
                    }
                } catch(Exception ex) {
                    //nothing
                }
            }
        }

        private void sendPageToEditor(String pageName) {
            readPage(pageName);
            System.out.println("Sending page!");
            try {
                output.writeObject(page);
                output.flush();
                System.out.println("Page sent!");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        private void updatePage(String data) {
            System.out.println("Updating a page!");
            String[] pattern = {Pattern.quote("[[[FILE[["), "]]FILE]]]"};
            String pageName = getFromPattern(pattern, data);
            readPage(pageName);
            data = data.replaceAll(pattern[0] + "(.*?)" + pattern[1], "");
            data = data.replaceAll(Pattern.quote("\\Q"),"");
            try {
                File file = new File(pageName);
                FileWriter writer = new FileWriter(file);
                writer.write(data);
                writer.flush();
                System.out.println("Page Updated!");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        private void createPage(String pageName) {
            System.out.println("Creating page!");
            File file = new File(BASE_PATH.concat(pageName));
            if(!file.exists()) {
                try {
                    if(!file.createNewFile()) System.out.println("File failed to be created!");
                    else System.out.println("Page created!");
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void readPage(String pageName) {
            System.out.println("Reading Page!");
            File file = new File(BASE_PATH.concat(pageName));
            try(BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line = buf.readLine();
                StringBuilder sb = new StringBuilder();
                while(line != null) {
                    sb.append(line).append("\n");
                    line = buf.readLine();
                }
                page = sb.toString();
                System.out.println("Page read!");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getFromPattern(String[] patterns, String data) {
        Pattern pattern = Pattern.compile(Pattern.quote(patterns[0]) + "(.*?)" + Pattern.quote(patterns[1]));
        Matcher m = pattern.matcher(data);
        String s = "";
        while(m.find()) {
            s = (m.group(1));
            data = data.replaceAll(patterns[0] + "(.*?)" + patterns[1], "");
        }
        return s;
    }

    public static void main(String[] args) {
        new Server().start();
    }
}