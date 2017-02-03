package com.nix.integrationtests;

import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.nix.integrationtests.KarafBaseConfig.getConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class UserAppInside_IT {

    private final static String[] APP_BUNDLES_NAMES = {
            "user-app-datasource",
            "user-app-model",
            "user-app-validator-hib",
            "user-app-api-rest",
    };
    private final static String[] APP_FEATURES_NAMES = {
            "user-app-datasource",
            "user-app-model",
            "user-app-validator-hibernate",
            "user-app-api",
            "user-app-install-all",
    };

    @Inject private BundleContext bundleContext;
    @Inject private FeaturesService featuresService;

    @Configuration
    public static Option[] configuration() throws Exception {
        return getConfiguration();
    }

    @Test
    public void allAppBundlesAreInstalledAndStarted() throws Exception {

        assertNotNull("BundleContext should not be null.", bundleContext);
        assertNotNull("FeaturesService should not be null.", featuresService);

        TimeUnit.SECONDS.sleep(10);

        List<String> appBundles = Arrays.asList(APP_BUNDLES_NAMES);

        int attempts = 10;
        boolean isAllBundleCollected = false;
        List<Bundle> bundlesInContainer = null;
        while (attempts-- > 0 && !isAllBundleCollected) {
            bundlesInContainer = Arrays.stream(bundleContext.getBundles())
                    .filter(b -> appBundles.contains(b.getSymbolicName())).peek(b -> System.out.println("-" + b.getSymbolicName()))
                    .filter(b -> b.getState() == Bundle.ACTIVE)
                    .collect(Collectors.toList());
            if (bundlesInContainer.size() == APP_BUNDLES_NAMES.length) {
                isAllBundleCollected = true;
            }
            System.out.println("atm: " + attempts);
            TimeUnit.SECONDS.sleep(1);
        }

        assertEquals("expected quantity of installed and active bundles is " + APP_BUNDLES_NAMES.length,
                APP_BUNDLES_NAMES.length, bundlesInContainer.size());
    }

    @Test
    public void allAppFeaturesAreInstalled() throws Exception {

        List<Feature> installedFeatures = new ArrayList<>();

        for (String appFeatureName : APP_FEATURES_NAMES) {
            Feature feature = featuresService.getFeature(appFeatureName);
            if (feature != null) {
                System.out.println(feature.getName());
            } else {
                System.out.println("is null - " + appFeatureName);
            }

            if (feature != null && featuresService.isInstalled(feature)) {
                installedFeatures.add(feature);
            }
        }

        assertEquals("expected quantity of installed features is " + APP_FEATURES_NAMES.length,
                APP_FEATURES_NAMES.length, installedFeatures.size());
    }

}
