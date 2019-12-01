package com.climpy.modules.discord;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class CommandParser {
  private Member member;
  private TextChannel channel;
  private Message message;
  private String label;
  private String[] args;
  
  public CommandParser(Member member, TextChannel channel, Message message, String label, String[] args) {
    this.member = member;
    this.channel = channel;
    this.message = message;
    this.label = label;
    this.args = args;
  }

  
  public Member getMember() { return this.member; }


  
  public TextChannel getChannel() { return this.channel; }


  
  public Message getMessage() { return this.message; }


  
  public String getLabel() { return this.label; }


  
  public String[] getArgs() { return this.args; }
}
