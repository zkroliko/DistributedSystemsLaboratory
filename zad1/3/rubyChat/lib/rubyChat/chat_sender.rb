require 'socket'

class ChatSender

  def initialize(ip, port)
    @ip=ip
    @port=port
  end


  def send(message)

    begin
      @socket = UDPSocket.open

      #print "Client: Open socket to #{@ip}:#{@port}"

      @socket.setsockopt(Socket::IPPROTO_IP, Socket::IP_TTL, [1].pack('i'))
      @socket.send(message, 0, MULTICAST_ADDR, PORT)

      #print "Client: Data has been sent\n"
    ensure
      @socket.close
    end
  end
end