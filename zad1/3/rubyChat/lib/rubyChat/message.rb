require 'digest'

class Message

  attr_accessor :nick, :text, :time, :checksum, :checksumValid

  def initialize(nick="",message="")
    @nick = nick
    @text = message[0..20].gsub(/\n/, "") # limiting length, no newline
    @time = "#{Time.now.hour}:#{Time.now.min}".to_s()
    @checksum = calculateChecksum
    @checksumValid = true
  end

  def calculateChecksum
    str = "#{@nick}#{@text}#{@time}"
    Digest::MD5.hexdigest(str)
  end

  def testChecksum
    if calculateChecksum == @checksum
      @checksumValid = true
    else
      @checksumValid = false
    end
  end

  def toString
    "#{@nick}\n#{@text}\n#{@time}\n#{@checksum}"
  end

  def fromString(text)
    values = String(text).split("\n")
    @nick = values[0]
    @text = values[1]
    @time = values[2]
    @checksum = values[3]
    testChecksum
  end
end