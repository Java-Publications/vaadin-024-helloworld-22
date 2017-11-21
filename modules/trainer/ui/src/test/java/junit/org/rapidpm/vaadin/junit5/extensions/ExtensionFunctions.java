package junit.org.rapidpm.vaadin.junit5.extensions;

import static org.rapidpm.frp.StringFunctions.notEmpty;
import static org.rapidpm.frp.StringFunctions.notStartsWith;
import static org.rapidpm.frp.Transformations.not;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.frp.Transformations;
import org.rapidpm.frp.functions.CheckedSupplier;

/**
 *
 */
public class ExtensionFunctions {

  public static Function<ExtensionContext, ExtensionContext.Namespace> namespaceFor() {
    return (ctx) -> {
      String name = ctx.getTestClass().get().getName();
      String methodName = ctx.getTestMethod().get().getName();
      ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(ExtensionFunctions.class,
                                                                               name,
                                                                               methodName);
      return namespace;
    };
  }

  public static Function<ExtensionContext, ExtensionContext.Store> store() {
    return (context) -> context.getStore(namespaceFor().apply(context));
  }

  public static Function<ExtensionContext, BiConsumer<String, Object>> storeObjectIn() {
    return (context) -> (key, value) -> store().apply(context).put(key, value);
  }

  public static Function<ExtensionContext, Consumer<String>> removeObjectIn() {
    return (context) -> (key) -> store().apply(context).remove(key);
  }


  public static Supplier<String> ipSupplierLocalIP = () -> {
    final CheckedSupplier<Enumeration<NetworkInterface>> checkedSupplier = NetworkInterface::getNetworkInterfaces;

    return Transformations.<NetworkInterface>enumToStream()
        .apply(checkedSupplier.getOrElse(Collections::emptyEnumeration))
        .map(NetworkInterface::getInetAddresses)
        .flatMap(iaEnum -> Transformations.<InetAddress>enumToStream().apply(iaEnum))
        .filter(inetAddress -> inetAddress instanceof Inet4Address)
        .filter(not(InetAddress::isMulticastAddress))
        .map(InetAddress::getHostAddress)
        .filter(notEmpty())
        .filter(adr -> notStartsWith().apply(adr, "127"))
        .filter(adr -> notStartsWith().apply(adr, "169.254"))
        .filter(adr -> notStartsWith().apply(adr, "255.255.255.255"))
        .filter(adr -> notStartsWith().apply(adr, "255.255.255.255"))
        .filter(adr -> notStartsWith().apply(adr, "0.0.0.0"))
        //            .filter(adr -> range(224, 240).noneMatch(nr -> adr.startsWith(valueOf(nr))))
        .findFirst().orElse("localhost");
  };


}
