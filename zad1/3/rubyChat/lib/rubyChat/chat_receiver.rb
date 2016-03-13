require 'socket'                # Get sockets from stdlib

require 'rubyChat/message.rb'

class ChatReceiver

  def initialize(ip, port)
    @ip=ip
    @port=port
  end

  def start
    @serverActive = true
    addr = [@ip, @port]

    @socket = UDPSocket.new
    @socket.bind(addr[0], addr[1])

    puts "\nDEBUG Receiver: Open socket to #{@ip}:#{@port}"


    while(@serverActive) do
      # if this number is too low it will drop the larger packets and never give them to you
      data, addr = @socket.recvfrom(1024)
      msg = Message.new.fromString(data)
      puts "#{msg.nick} #{msg.time}: #{msg.text}"
    end


    @socket.close

  end
end