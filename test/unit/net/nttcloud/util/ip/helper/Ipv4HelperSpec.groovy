package net.nttcloud.util.ip.helper


import grails.test.mixin.*
import net.nttcloud.util.ip.IpAddress
import spock.lang.Specification

class Ipv4HelperSpec extends Specification {
	
	void "test isPrefix"() {
		expect:
		new IpAddress(x).isPrefix == y
		
		where:
		x | y
		"0.0.0.0/0" | true
		"0.0.0.1/0" | false
		"255.255.255.255/0" | false
		"192.168.2.0/24" | true
		"192.168.2.1/24" | false
		"192.168.2.255/24" | false
		"192.168.3.0/24" | true
		"0.0.0.0/32" | true
		"192.168.0.1/32" | true
		"255.255.255.255/32" | true
		"10.0.0.0/8" | true
		"172.16.0.0/12" | true
		"192.168.0.0/16" | true
		"192.168.2.16/27" | false
		"192.168.2.16/28" | true
		"192.168.2.16/29" | true
		"192.168.2.16/30" | true
		"192.168.2.16/31" | true
	}
	
	void "test minus"() {
		expect:
		new IpAddress(x) - new IpAddress(y) == new IpAddress(z)
		
		where:
		x | y | z
		"192.168.0.1/24" | "192.0.0.0/16" | "0.168.0.1/16"
		"192.168.0.1/24" | "192.168.0.2/32" | "255.255.255.255/32"
		"192.168.0.1/24" | "192.168.0.1/32" | "0.0.0.0/32"
		"192.168.0.1/24" | "192.168.0.0/32" | "0.0.0.1/32"
	}
	
	void "test contains"() {
		expect:
		new IpAddress(x).contains(new IpAddress(y)) == z
		
		where:
		x | y | z
		
		"192.168.2.0/24" | "192.168.1.255/24"   | false
		"192.168.2.0/24" | "192.168.2.0/24"     | true
		"192.168.2.0/24" | "192.168.2.1/24"     | true
		"192.168.2.0/24" | "192.168.2.255/24"   | true
		"192.168.2.0/24" | "192.168.3.0/24"     | false
		
		"192.168.2.0/24" | "192.168.1.255/25"   | false
		"192.168.2.0/24" | "192.168.2.0/25"     | true
		"192.168.2.0/24" | "192.168.2.1/25"     | true
		"192.168.2.0/24" | "192.168.2.255/25"   | true
		"192.168.2.0/24" | "192.168.3.0/25"     | false

		"192.168.2.0/24" | "192.168.1.255/23"   | false
		"192.168.2.0/24" | "192.168.2.0/23"     | false
		"192.168.2.0/24" | "192.168.2.1/23"     | false
		"192.168.2.0/24" | "192.168.2.255/23"   | false
		"192.168.2.0/24" | "192.168.3.0/23"     | false
		
		"192.168.2.0/23" | "192.168.2.1/24"     | true
		"192.168.2.0/24" | "192.168.2.1/24"     | true
		"192.168.2.0/25" | "192.168.2.1/24"     | false
	}
	
	void "test contains - fail case"() {
		when:
		new IpAddress(x).contains(y)
		
		then:
		thrown(e)
		
		where:
		x | y | e
		"192.168.0.1/24" | new IpAddress("192.168.0.1/24") | AssertionError
	}
	
}
