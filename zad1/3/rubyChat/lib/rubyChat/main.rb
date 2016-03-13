#!/usr/bin/env ruby

require 'rubyChat/chat_receiver.rb'
require 'rubyChat/chat_sender.rb'
require 'rubyChat/message.rb'

sender = ChatSender.new('127.0.0.1', 5000)
receiver = ChatReceiver.new('127.0.0.1', 5000)

nick="Krokodyl"

Thread.start() do
  receiver.start
end

continue = true

puts("Welcome to the channel")

while(continue) do
    print(">")
    message = gets
    msg = Message.new(nick,message)
    sender.send(msg.toString)
end

