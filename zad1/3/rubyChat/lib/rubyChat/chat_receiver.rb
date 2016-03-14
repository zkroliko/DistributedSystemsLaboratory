require 'socket'                # Get sockets from stdlib
require 'ipaddr'

require 'rubyChat/message.rb'

class ChatReceiver

  def initialize(ip, port, nick)
    @ip = ip
    @port=port
    @nick=nick
  end

  def start
    Thread.start() do


      @serverActive = true
      addr = [@ip, @port]

      printf "\nDEBUG Receiver: Binding socket"

      ip =  IPAddr.new(MULTICAST_ADDR).hton + IPAddr.new("0.0.0.0").hton
      @socket = UDPSocket.new
      @socket.setsockopt(Socket::IPPROTO_IP, Socket::IP_ADD_MEMBERSHIP, ip)
      @socket.bind(Socket::INADDR_ANY, PORT)

      printf "\nDEBUG Receiver: Binding socket to #{@ip}:#{@port} successful"


      while(@serverActive) do
        # if this number is too low it will drop the larger packets and never give them to you
        data, addr = @socket.recvfrom(4096)

        msg = Message.new
        msg.fromString(data)

        if (msg.checksumValid)
          # To disable self posting
          # if (!msg.nick.equal?nic)
          #   printf "\r#{msg.nick} - #{msg.time}: #{msg.text}\n"
          # end
          printf "\r#{msg.nick} - #{msg.time}: #{msg.text}\n"
        else
          printf "\r#{msg.nick} - Bad checksum\n"
        end

        # For formatting sake
        printf ">"
      end

      @socket.close
    end

  end
end