package com.example.servier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpService {
    public  HttpService() throws IOException {
        ServerSocket serverSocket = new ServerSocket(10086);
        ExecutorService pool= Executors.newFixedThreadPool(50);
        while (true) {
            Socket socket = serverSocket.accept();
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();
            System.out.println("服务器已经连接");
            byte[] requestbuff=new byte[is.available()];
            is.read(requestbuff);
            String s = new String(requestbuff);
//            System.out.println(s);
            String reqstr=s.split("\r\n")[0];
            String requstuil=reqstr.split(" ")[1];
            String resourse=requstuil.substring(1);
            System.out.println(resourse);
            String jieguo;
            if(resourse==null)
            {
                jieguo=null;
            }else {
                jieguo=doquest(resourse);
            }
            System.out.println(jieguo);
            if (socket != null && !socket.isClosed()) {
                          acceptclient(socket,jieguo);
            }
         socket.close();
        }
    }
    private void acceptclient(Socket socket, String show) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write("HTTP/1.1 200 OK\r\n".getBytes());
        os.write("Content-Type:text/html;charset=utf-8\r\n".getBytes());
//                os.write("Content-Length:38\r\n".getBytes());
        os.write("Server:gybs\r\n".getBytes());
        os.write(("Date:" + new Date() + "\r\n").getBytes());
        os.write("\r\n".getBytes());
        os.write(show.getBytes());
        os.flush();
        os.close();
    }
    public String doquest(String resourse){
        String substring = resourse.substring(resourse.indexOf("?") + 1);
        String substring1 = resourse.substring(0,resourse.indexOf("?"));
        Map<String,Object> map=getUrlparam(substring);
        String getValuea= String.valueOf(map.get("a"));
        String getValueb= String.valueOf(map.get("b"));
        Integer a1=Integer.parseInt(getValuea);
        Integer b1=Integer.parseInt(getValueb);
        String odd;
        if(substring1.equals("add")){
            odd= String.valueOf(a1+b1);
        }else {
            odd= String.valueOf(a1*b1);

        }
        return odd;
    }
    public Map<String,Object>getUrlparam(String params){
        Map<String,Object> map=new HashMap<>();
        if (params==null||params.equals("")){
            return map;
        }
        String[] parms=params.split("&");
        for (String parm : parms) {
            String s = parm.trim();
            String[] p=s.split("=");
            if(p.length==2){
                map.put(p[0],p[1]);
            }
        }
        return map;
    }
    public static void main(String[] args) throws IOException {
        new HttpService();
    }
}
