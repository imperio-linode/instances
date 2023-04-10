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

    public static InetAddress[] stringListToInetArr(List<String> strings) {
        log.info("stringListToInetList: {} ", strings);
        List<InetAddress> inetAddresses = new ArrayList<>();
        strings.forEach(ip -> {
            try {
                if (!ip.contains("/")) {
                    ip = ip + "/32";
                }

                String[] parts = ip.split("/");
                log.info("String list to inet parts: {} / {} ", parts[0], parts[1]);

                InetAddress inetAddress = InetAddress.getByName(parts[0]);
                int prefixLength = Integer.parseInt(parts[1]);
                byte[] addressBytes = inetAddress.getAddress();
                log.info("if v4: {}, {}", inetAddress.getAddress(), inetAddress.getHostAddress());

                for (int i = prefixLength; i < addressBytes.length * 8; i++) {
                    addressBytes[i / 8] &= ~(1 << (7 - (i % 8)));
                    log.info("Foreach byte 4 {}", addressBytes[i / 8]);
                }

                inetAddress = InetAddress.getByAddress(addressBytes);

                log.info("v4 changed: {}, {}", inetAddress.getAddress(), inetAddress.getHostAddress());
                inetAddresses.add(inetAddress);

            } catch (UnknownHostException | NumberFormatException e) {
                System.err.println("Error: " + e.getMessage());
            }
        });
        inetAddresses.forEach(inetAddress -> log.info("Inet address: {}, {}", inetAddress.getAddress(), inetAddress.getHostAddress()));
        return inetListToInetArr(inetAddresses);
    }

    public static InetAddress[] inetListToInetArr(List<InetAddress> instanceIpv4) {
        return instanceIpv4.toArray(new InetAddress[0]);
    }
}
