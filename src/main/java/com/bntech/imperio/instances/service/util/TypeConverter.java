package com.bntech.imperio.instances.service.util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TypeConverter {

    public static Mono<Long> monoStringToLong(Mono<String> string) {
        return string.map(Long::parseLong);
    }

    public static Mono<Long> fluxStringToLong(Flux<String> string) {
        return string.map(Long::parseLong).collectList().transform(listMono -> listMono.map(longs -> longs.get(0)));
    }

    public static List<String> inetListToStringList(List<InetAddress> inets) {
        return inets.stream().map(InetAddress::getHostAddress).toList();
    }

    public static List<InetAddress> stringListToInetList(List<String> strings) {
        log.info("stringListToInetList: {} ", strings);
        List<InetAddress> inetAddresses = new ArrayList<>();
        strings.forEach(ip -> {
            try {
                String[] parts = ip.split("/");
                InetAddress inetAddress = InetAddress.getByName(parts[0]);
                if (parts.length > 1) {
                    int prefixLength = Integer.parseInt(parts[1]);
                    if (inetAddress instanceof Inet4Address) {
                        byte[] addressBytes = inetAddress.getAddress();
                        for (int i = prefixLength; i < addressBytes.length * 8; i++) {
                            addressBytes[i / 8] &= ~(1 << (7 - (i % 8)));
                        }
                        inetAddress = Inet4Address.getByAddress(addressBytes);
                        inetAddresses.add(inetAddress);
                    } else if (inetAddress instanceof Inet6Address) {
                        byte[] addressBytes = inetAddress.getAddress();
                        for (int i = prefixLength; i < addressBytes.length * 8; i++) {
                            addressBytes[i / 8] &= ~(1 << (7 - (i % 8)));
                        }
                        inetAddress = Inet6Address.getByAddress(addressBytes);
                        inetAddresses.add(inetAddress);
                    }
                }
            } catch (UnknownHostException | NumberFormatException e) {
                System.err.println("Error: " + e.getMessage());
            }
        });
        return inetAddresses;
    }
}
