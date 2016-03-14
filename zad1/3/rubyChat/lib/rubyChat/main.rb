#!/usr/bin/env ruby

require 'rubyChat/chat_receiver.rb'
require 'rubyChat/chat_sender.rb'
require 'rubyChat/message.rb'

nick="Krokodyl"

MULTICAST_ADDR = "224.0.0.1"
PORT = 5000

sender = ChatSender.new('MULTICAST_ADDR', PORT)
receiver = ChatReceiver.new('MULTICAST_ADDR', PORT, nick)

receiver.start

continue = true

puts("Welcome to the channel dear #{nick}")

while(continue) do
    print(">")
    message = gets
    msg = Message.new(nick,message)
    sender.send(msg.toString)
end

