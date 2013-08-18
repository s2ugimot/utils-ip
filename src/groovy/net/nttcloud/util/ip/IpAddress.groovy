package net.nttcloud.util.ip

import net.nttcloud.util.ip.helper.IpHelper;
import net.nttcloud.util.ip.helper.Ipv4Helper;
import net.nttcloud.util.ip.helper.Ipv6Helper;
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = ["inetAddress", "plen"])
class IpAddress {

	InetAddress inetAddress
	int plen
	
	IpHelper helper
	
	IpAddress(InetAddress inetAddress, int plen) {
		this.inetAddress = inetAddress
		initHelper(this)
		assert helper.checkPlen(plen), "strange prefix length: ${plen}"
		this.plen = plen
	}

	IpAddress(String addr, int plen) {
		this.inetAddress = InetAddress.getByName(addr)
		initHelper(this)
		assert helper.checkPlen(plen), "strange prefix length: ${plen}"
		this.plen = plen
	}

	IpAddress(String addr) {
		def a = addr.contains("/") ? addr.split("/")[0] : addr
		this.inetAddress = InetAddress.getByName(a)
		initHelper(this)
		def plen = addr.contains("/") ? addr.split("/")[1] as int : helper.maxPlen
		assert helper.checkPlen(plen), "strange prefix length: ${plen}"
		this.plen = plen
	}
	
	private initHelper(IpAddress i) {
		if (this.isV4) {
			this.helper = new Ipv4Helper(i)
		} else if (this.isV6) {
			this.helper = new Ipv6Helper(i)
		} else {
			assert null, "something is wrong..."
		}
	}

	boolean getIsV4() {
		return inetAddress instanceof Inet4Address
	}

	boolean getIsV6() {
		return inetAddress instanceof Inet6Address
	}
	
	boolean getIsPrefix() {
		return helper.isPrefix
	}

	boolean getIsHost() {
		return helper.isHost
	}

	IpAddress getPrefix() {
		return helper.prefix()
	}

	IpAddress getOffset() {
		return helper.offset()
	}

	IpAddress minus(IpAddress o) {
		assert (this.isV4 & o.isV4) | (this.isV6 & o.isV6), "Address family unmatch."
		return helper.minus(o)
	}
	
	boolean contains(IpAddress o) {
		assert (this.isV4 & o.isV4) | (this.isV6 & o.isV6), "Address family unmatch."
		return helper.contains(o)
	}
	
	String toString() {
		return helper.toString()
	}
	
	String toFullString() {
		return helper.toFullString()
	}
	
	String toHostOnlyString() {
		return helper.toHostOnlyString()
	}
}
