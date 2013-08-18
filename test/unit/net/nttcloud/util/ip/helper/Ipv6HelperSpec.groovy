package net.nttcloud.util.ip.helper

import grails.test.mixin.*
import net.nttcloud.util.ip.IpAddress
import spock.lang.Specification

class Ipv6HelperSpec extends Specification {
	
	void "test isPrefix"() {
		expect:
		new IpAddress(x).isPrefix == y
		
		where:
		x | y
		"::/0" | true
		"::1/0" | false
		"ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/0" | false
		"2001:db8:cafe:beaf::/64" | true
		"2001:db8:cafe:beaf::1/64" | false
		"2001:db8:cafe:beaf:ffff:ffff:ffff:ffff/64" | false
		"2001:db8:cafe:beb0::/64" | true
		"::/128" | true
		"2001:db8:cafe:beaf::/128" | true
		"ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/128" | true
		"2001:db8::/32" | true
		"2001:db8:cafe::/48" | true
		"2001:db8:cafe:beaf::/65" | true
		"2001:db8:cafe:beaf::/66" | true
		"2001:db8:cafe:beaf::/80" | true
		"2001:db8:cafe:beaf::/112" | true
		"2001:db8:cafe:beaf::/126" | true
		"2001:db8:cafe:beaf::/127" | true
	}
	
	void "test minus"() {
		expect:
		new IpAddress(x) - new IpAddress(y) == new IpAddress(z)
		
		where:
		x | y | z
		"2001:db8:cafe:beaf::1/64" | "2001:db8:cafe:beaf::/64" | "::1/64"
		"2001:db8:cafe:beaf::1/64" | "2001:db8:cafe:beaf::2/64" | "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/64"
		"2001:db8:cafe:beaf::1/64" | "2001:db8:cafe:beaf::1/64" | "::/64"
		
		"2001:db8:cafe:beaf::1/64" | "2001:db8::/32" | "0:0:cafe:beaf::1/32"
		"2001:db8:cafe:beaf::1/64" | "2001:db8:cafe::/48" | "0:0:0:beaf::1/48"
	}
	
	void "test contains"() {
		expect:
		new IpAddress(x).contains(new IpAddress(y)) == z
		
		where:
		x | y | z
		
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:bead:ffff:ffff:ffff:ffff/64" | false
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf::/64" | true
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf::1/64" | true
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf:ffff:ffff:ffff:ffff/64" | true
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beb0::/64" | false
		
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:bead:ffff:ffff:ffff:ffff/65" | false
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf::/65" | true
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf::1/65" | true
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf:ffff:ffff:ffff:ffff/65" | true
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beb0::/65" | false
		
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:bead:ffff:ffff:ffff:ffff/63" | false
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf::/63" | false
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf::1/63" | false
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beaf:ffff:ffff:ffff:ffff/63" | false
		"2001:db8:cafe:beaf::/64" | "2001:db8:cafe:beb0::/63" | false
	}
	
	void "test contains - fail case"() {
		when:
		new IpAddress(x).contains(y)
		
		then:
		thrown(e)
		
		where:
		x | y | e
		"2001:db8::1/64" | new IpAddress("2001:db8::1/64") | AssertionError
	}
	
	void "test toString()"() {
		expect:
		new IpAddress(x).toString() == y
		
		where:
		x								| y
		"2001:db8:0:0:0:0:0:0"			| "2001:db8::"
		"2001:0:0:0:cafe:0:0:0"			| "2001::cafe:0:0:0"
		"2001:db8:0:0:0:0:0:1"			| "2001:db8::1"
		"2001:db8:0:0:cafe:0:0:beaf/64"	| "2001:db8::cafe:0:0:beaf/64"
	}
	
	void "test toFullString()"() {
		expect:
		new IpAddress(x).toFullString() == y
		
		where:
		x								| y
		"2001:db8:0:0:0:0:0:0"			| "2001:db8::/128"
		"2001:0:0:0:cafe:0:0:0"			| "2001::cafe:0:0:0/128"
		"2001:db8:0:0:0:0:0:1"			| "2001:db8::1/128"
		"2001:db8:0:0:cafe:0:0:beaf/64"	| "2001:db8::cafe:0:0:beaf/64"
	}
}
