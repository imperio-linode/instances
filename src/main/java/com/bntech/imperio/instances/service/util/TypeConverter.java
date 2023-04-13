package com.bntech.imperio.instances.service.util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
//        strings.forEach(ip -> {
//            try {
//                if (!ip.contains("/")) {
//                    ip = ip + "/32";
//                }
//
//                String[] parts = ip.split("/");
//
//                InetAddress inetAddress = InetAddress.getByName(parts[0]);
//                int prefixLength = Integer.parseInt(parts[1]);
//                byte[] addressBytes = inetAddress.getAddress();
//
//                for (int i = prefixLength; i < addressBytes.length * 8; i++) {
//                    addressBytes[i / 8] &= ~(1 << (7 - (i % 8)));
//                }
//
//                inetAddress = InetAddress.getByAddress(addressBytes);
//
//                inetAddresses.add(inetAddress);
//
//            } catch (UnknownHostException | NumberFormatException e) {
//                System.err.println("Error: " + e.getMessage());
//            }
//        });
        return strings.stream().map(string -> {
            try {
                return InetAddress.getByName(string.split("/")[0]);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return null;
        }).toList();

    }
}
