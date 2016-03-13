require 'socket'                # Get sockets from stdlib

require 'rubyChat/message.rb'

class ChatReceiver

  def initialize(ip, port)
    @ip=ip
    @port=port
  end

  def start
    Thread.start() do

      @serverActive = true
      addr = [@ip, @port]


      @socket = UDPSocket.new
      @socket.bind(addr[0], addr[1])

      # puts "\nDEBUG Receiver: Open socket to #{@ip}:#{@port}"

      while(@serverActive) do
        # if this number is too low it will drop the larger packets and never give them to you
        data, addr = @socket.recvfrom(4096)
        msg = Message.new
        msg.fromString(data)
        printf "\r#{msg.nick} - #{msg.time}: #{msg.text}\n"
        # For formatting sake
        printf ">"
      end

      @socket.close
    end

  end
end