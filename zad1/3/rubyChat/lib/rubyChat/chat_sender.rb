require 'socket'

class ChatSender

  def initialize(ip, port)
    @ip=ip
    @port=port
  end


  def send(message)
    addr = [@ip, @port]

    @socket = UDPSocket.new
    @socket.setsockopt(Socket::SOL_SOCKET, Socket::SO_BROADCAST, true)

    puts "Client: Open socket to #{@ip}:#{@port}"

    data = message
    @socket.send(data, 0, addr[0], addr[1])

    print "Client: Data has been sent\n"

    @socket.close
  end
end