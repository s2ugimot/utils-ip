package org.anzubar.utils.ip.helper

import java.nio.ByteBuffer

import org.anzubar.utils.ip.IpAddress


class Ipv4Helper implements IpHelper {

	static final int MAXPLEN = 32
	
	IpAddress i

	Ipv4Helper(IpAddress i) {
		assert i.isV4
		this.i = i
	}
	
	int getMaxPlen() {
		return MAXPLEN
	}
	
	boolean checkPlen(int plen) {
		return (0 <= plen && plen <= MAXPLEN)
	}
	
	boolean getIsPrefix() {
		if (i.plen == 32) return true
		def inum = ByteBuffer.allocate(8).putInt(0).put(i.inetAddress.address).getLong(0)
		return (inum & ((1L << (32 - i.plen)) -1)) == 0
	}
	
	boolean getIsHost() {
		return (i.plen == 32)
	}
	
	IpAddress getPrefix() {
		
	}
	
	IpAddress getOffset() {
		
	}

	IpAddress minus(IpAddress o) {
		def inum = ByteBuffer.allocate(8).putInt(0).put(i.inetAddress.address).getLong(0)
		def onum = ByteBuffer.allocate(8).putInt(0).put(o.inetAddress.address).getLong(0)
		def diff = ByteBuffer.allocate(8).putLong(inum - onum)
		def addr = InetAddress.getByAddress(diff.array()[4..7] as byte[])
		return new IpAddress(addr, o.plen)
	}

	boolean contains(IpAddress o) {
		assert i.isPrefix
		if (i.plen > o.plen) return false
		def inum = ByteBuffer.allocate(8).putInt(0).put(i.inetAddress.address).getLong(0)
		def onum = ByteBuffer.allocate(8).putInt(0).put(o.inetAddress.address).getLong(0)
		if (inum > onum) return false
		return (onum - inum < 1L << (32 - i.plen))
	}
	
	String toString() {
		def s = i.inetAddress.getHostAddress()
		if (!this.isHost) {
			s <<= "/" << i.plen
		}
		return s
	}
	
	String toFullString() {
		return i.inetAddress.getHostAddress() << "/" << i.plen
	}
	
	String toLongString() {
		return toString()
	}
	
	String toHostOnlyString() {
		return i.inetAddress.getHostAddress()
	}
}
