package net.nttcloud.util.ip.helper

import net.nttcloud.util.ip.IpAddress;

interface IpHelper {

	int getMaxPlen()
	boolean getIsPrefix()
	boolean getIsHost()
	IpAddress getPrefix()
	IpAddress getOffset()
	IpAddress minus(IpAddress o)
	boolean contains(IpAddress o)
	String toString()
	String toFullString()
	String toLongString()
	String toHostOnlyString()
	
}
