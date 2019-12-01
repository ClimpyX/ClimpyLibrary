package com.climpy.managers;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.user.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {
    public static ClimpyLibrary plugin;
    public static File file;
    public static File chatFile;
   
    public LogManager(ClimpyLibrary plugin) {
        this.plugin = plugin;
        System.out.println(plugin.getDataFolder() + "");
        file = new File(plugin.getDataFolder() + "/logs.txt");
        chatFile = new File(plugin.getDataFolder() + "/chat.txt");
    }
   
    public void addMessage(String message) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(message);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    public void setup() {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if(!chatFile.exists()) {
            try {
            	chatFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   
    public void formatMessage(String msg, String name) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(name);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
        String date = dateFormat.format(new Date());
        String finalMessage = date + " - [" + user.getRankType().getDisplayName() + "] " + name + ": " + msg + "\n";
        addMessage(finalMessage);
    }
    
    public void formatChatMessage(String msg, String name) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(name);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
        String date = dateFormat.format(new Date());
        String finalMessage = date + " - " + "[" + user.getRankType().getDisplayName() + "] " + name + ": " + msg + "\n";
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(chatFile, true));
            bw.append(finalMessage);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}