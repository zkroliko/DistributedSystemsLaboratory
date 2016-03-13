#!/usr/bin/env ruby

require 'rubyChat/chat_receiver.rb'
require 'rubyChat/chat_sender.rb'
require 'rubyChat/message.rb'

sender = ChatSender.new('127.0.0.1', 5000)
receiver = ChatReceiver.new('127.0.0.1', 5000)

nick="Krokodyl"

receiver.start

continue = true

puts("Welcome to the channel dear #{nick}")

while(continue) do
    print(">")
    message = gets
    msg = Message.new(nick,message)
    sender.send(msg.toString)
end

