package com.nix.integrationtests;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;

import java.io.File;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

public class KarafBaseConfig {

    @Configuration
    public static Option[] getConfiguration() throws Exception {
        return new Option[]{
                karafDistributionConfiguration().frameworkUrl(
                        maven().groupId("org.apache.karaf")
                                .artifactId("apache-karaf")
                                .version("4.0.8")
                                .type("tar.gz"))
                        .karafVersion("4.0.8")
                        .name("Apache Karaf")
                        .unpackDirectory(new File("target/integration-tests/karaf/"))
                        .useDeployFolder(false),

//                keepRuntimeFolder(),

                editConfigurationFilePut("etc/config.properties ","org.apache.aries.blueprint.synchronous","true"),

                cleanCaches(true),

                configureConsole().ignoreRemoteShell(),

                logLevel(LogLevelOption.LogLevel.WARN),

                junitBundles(),

                features("mvn:org.apache.karaf.features/standard/4.0.8/xml/features", "(wrap)"),
                features("mvn:com.nix/user-app-features/0.0.1/xml", "user-app-install-all"),
        };
    }
}
