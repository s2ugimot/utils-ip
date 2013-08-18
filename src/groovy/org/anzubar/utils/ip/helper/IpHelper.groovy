package org.anzubar.utils.ip.helper

import org.anzubar.utils.ip.IpAddress

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
