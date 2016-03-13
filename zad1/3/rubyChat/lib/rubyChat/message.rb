require 'digest'

class Message

  attr_accessor :nick, :text, :time, :checksum

  def initialize(nick=nil,message=nil)
    @nick = nick
    @text = message
    @time = Time.now
    @checksum = calculateChecksum
  end

  def calculateChecksum
    hash = {nick: @nick, text: @text, time: @time}
    Digest::MD5.hexdigest(Marshal::dump(hash))
  end

  def testChecksum
    #change
    if (calculateChecksum.equal? @checksum)
      puts "Error in checksum"
    end
  end

  def toString
    "#{@nick}\n#{@text}\n#{@time}\n#{@checksum}"
  end

  def fromString(text)
    values = String(text).split("\n")
    # puts values
    @nick = values[0]
    @text = values[1]
    @time = values[2]
    @checksum = values[3]
    testChecksum
  end
end