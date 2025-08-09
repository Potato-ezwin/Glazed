package com.chorus.api.system.prot;

import cc.polymorphism.annot.IncludeReference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@IncludeReference
public class MathProt {
    private static final Map<String, Double> consts = new ConcurrentHashMap<>();
    private static volatile boolean initialized = false;
    private static final Object INIT_LOCK = new Object();

    public static void initializeConstants() {
        if (!initialized) {
            synchronized (INIT_LOCK) {
                if (!initialized) {
                    initializeDefaultConstants();
                    initialized = true;
                }
            }
        }
    }

    private static void initializeDefaultConstants() {
        // Initialize with standard mathematical constants
        consts.put("pi", Math.PI);
        consts.put("e", Math.E);
        consts.put("phi", (1.0 + Math.sqrt(5.0)) / 2.0); // Golden ratio
        consts.put("sqrt2", Math.sqrt(2.0));
        consts.put("sqrt3", Math.sqrt(3.0));
        consts.put("ln2", Math.log(2.0));
        consts.put("ln10", Math.log(10.0));
        consts.put("size", 1.0); // Default size value
    }

    public static double getConst(String name) {
        if (!initialized) {
            initializeConstants();
        }
        return consts.getOrDefault(name, 0.0);
    }

    public static double PI() { return getConst("pi"); }
    public static double E() { return getConst("e"); }
    public static double PHI() { return getConst("phi"); }
    public static double SQRT2() { return getConst("sqrt2"); }
    public static double SQRT3() { return getConst("sqrt3"); }
    public static double LN2() { return getConst("ln2"); }
    public static double LN10() { return getConst("ln10"); }
    public static double SIZE() { return getConst("size"); }
}
