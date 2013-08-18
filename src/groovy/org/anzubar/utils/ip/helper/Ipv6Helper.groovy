package org.anzubar.utils.ip.helper

import java.nio.ByteBuffer

import org.anzubar.utils.ip.IpAddress

import com.googlecode.ipv6.IPv6Address


class Ipv6Helper implements IpHelper {
	
	static final int MAXPLEN = 128

	IpAddress i

	Ipv6Helper(IpAddress i) {
		assert i.isV6
		this.i = i
	}
	
	int getMaxPlen() {
		return MAXPLEN
	}
	
	boolean checkPlen(int plen) {
		return (0 <= plen && plen <= MAXPLEN)
	}
	
	boolean getIsPrefix() {
		if (i.plen == 128) return true
		def inum = new BigInteger(i.inetAddress.address)
		return (inum & ((new BigInteger(1).shiftLeft(128 - i.plen)) -1)) == 0
	}
	
	boolean getIsHost() {
		return (i.plen == 128)
	}

	IpAddress getPrefix() {
		
	}
	
	IpAddress getOffset() {
		
	}

	IpAddress minus(IpAddress o) {
		def inum = new BigInteger(i.inetAddress.address)
		def onum = new BigInteger(o.inetAddress.address)
		ByteBuffer buf = ByteBuffer.allocate(16)
		def diff = inum - onum
		def diffBuff = diff.toByteArray()
		(0..(16 - diffBuff.length - 1)).each {
			buf.put((diff < BigInteger.ZERO ? 0xff : 0) as byte)
		}
		buf.put(diffBuff)
		def addr = InetAddress.getByAddress(buf.array())
		return new IpAddress(addr, o.plen)
	}

	boolean contains(IpAddress o) {
		assert i.isPrefix
		if (i.plen > o.plen) return false
		def inum = new BigInteger(i.inetAddress.address)
		def onum = new BigInteger(o.inetAddress.address)
		if (inum > onum) return false
		return (onum - inum < new BigInteger(1).shiftLeft(128 - i.plen))

	}
	
	String toString() {
		def s = IPv6Address.fromInetAddress(i.inetAddress).toString()
		if (!this.isHost) {
			s <<= "/" << i.plen
		}
		return s
	}
	
	String toFullString() {
		return IPv6Address.fromInetAddress(i.inetAddress).toString() << "/" << i.plen
	}
	
	String toLongString() {
		def s = IPv6Address.fromInetAddress(i.inetAddress).toLongString()
		if (!this.isHost) {
			s << "/" << i.plen
		}
		return s
	}
	
	String toHostOnlyString() {
		return IPv6Address.fromInetAddress(i.inetAddress).toString()
	}
}
