package com.mojang.minecraftpe.hardwareinfo;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class CPUTopologyInfo {
    private static final Pattern CPU_LIST_VALUE_FORMAT = Pattern.compile("(\\d{1,4})(?:-(\\d{1,4}))?");
    private static final CPUTopologyInfo SINGLETON_INSTANCE = new CPUTopologyInfo();
    private Set<CPUCluster> CLUSTERS;
    private List<SystemCPU> CPUS;
    private BitSet CPUS_BITSET;
    private int CPU_PROCESSOR_COUNT;

    public interface BitCollector {
        void setBit(int bitIndex);
    }

    public static CPUTopologyInfo getInstance() {
        return SINGLETON_INSTANCE;
    }

    private CPUTopologyInfo() {
        try {
            initializeCPUInformation();
            this.CLUSTERS = getClustersFromSiblingCPUs();
        } catch (Exception unused) {
            Log.w("MCPE", "Failed to initialize CPU topology information");
            this.CPU_PROCESSOR_COUNT = 0;
            this.CPUS_BITSET = new BitSet();
            this.CPUS = new ArrayList();
            this.CLUSTERS = new TreeSet();
        }
    }

    private void initializeCPUInformation() throws Exception {
        File file = new File("/sys/devices/system/cpu/present");
        int i = 0;
        if (!file.exists() || !file.canRead()) {
            this.CPU_PROCESSOR_COUNT = 0;
            this.CPUS_BITSET = new BitSet();
            this.CPUS = new ArrayList();
        }
        String line = new BufferedReader(new FileReader(file)).readLine();
        BitSetCollector bitSetCollector = new BitSetCollector();
        parseCPUListString(line, bitSetCollector);
        this.CPU_PROCESSOR_COUNT = bitSetCollector.getBitCount();
        this.CPUS = new ArrayList(this.CPU_PROCESSOR_COUNT);
        this.CPUS_BITSET = bitSetCollector.getBitSet();
        while (true) {
            int iNextSetBit = this.CPUS_BITSET.nextSetBit(i);
            if (iNextSetBit < 0) {
                return;
            }
            this.CPUS.add(new SystemCPU(iNextSetBit));
            i = iNextSetBit + 1;
        }
    }

    private Set<CPUCluster> getClustersFromSiblingCPUs() throws NumberFormatException {
        TreeSet treeSet = new TreeSet();
        List<SystemCPU> cpus = getCPUS();
        ArrayDeque arrayDeque = new ArrayDeque(cpus.size());
        arrayDeque.addAll(cpus);
        while (!arrayDeque.isEmpty()) {
            SystemCPU systemCPU = (SystemCPU) arrayDeque.poll();
            if (systemCPU != null && systemCPU.exists()) {
                systemCPU.updateCPUFreq();
                String siblingString = systemCPU.getSiblingString();
                Set<SystemCPU> setCpuSetFromCPUListString = cpuSetFromCPUListString(siblingString);
                if (!setCpuSetFromCPUListString.isEmpty()) {
                    for (SystemCPU systemCPU2 : setCpuSetFromCPUListString) {
                        if (systemCPU2 != systemCPU) {
                            systemCPU2.updateCPUFreq();
                        }
                        arrayDeque.remove(systemCPU2);
                    }
                    treeSet.add(new CPUCluster(siblingString, setCpuSetFromCPUListString));
                }
            }
        }
        return treeSet;
    }

    List<SystemCPU> getCPUS() {
        return this.CPUS;
    }

    public int getCPUCount() {
        return this.CPU_PROCESSOR_COUNT;
    }

    public Set<SystemCPU> cpuSetFromCPUListString(String siblingInfo) throws NumberFormatException {
        final TreeSet treeSet = new TreeSet();
        if (siblingInfo.isEmpty()) {
            return treeSet;
        }
        parseCPUListString(siblingInfo, new BitCollector() { // from class: com.mojang.minecraftpe.hardwareinfo.-$$Lambda$CPUTopologyInfo$qVW8OLAaw1rUR7scObWz2-2eKd4
            @Override // com.mojang.minecraftpe.hardwareinfo.CPUTopologyInfo.BitCollector
            public final void setBit(int i) {
                lambda$cpuSetFromCPUListString$0$CPUTopologyInfo(treeSet, i);
            }
        });
        return treeSet;
    }

    public /* synthetic */ void lambda$cpuSetFromCPUListString$0$CPUTopologyInfo(final Set siblingCPUs, int bitIndex) {
        siblingCPUs.add(this.CPUS.get(bitIndex));
    }

    public static class BitSetCollector implements BitCollector {
        private int bitsCounted = 0;
        private BitSet bits = new BitSet();

        int getBitCount() {
            return this.bitsCounted;
        }

        BitSet getBitSet() {
            return this.bits;
        }

        @Override // com.mojang.minecraftpe.hardwareinfo.CPUTopologyInfo.BitCollector
        public void setBit(int bitIndex) {
            this.bits.set(bitIndex);
            this.bitsCounted++;
        }
    }

    public static <T extends BitCollector> T parseCPUListString(String listString, T collector) throws NumberFormatException {
        if (listString.isEmpty()) {
            return collector;
        }
        for (String str : listString.split(",")) {
            Matcher matcher = CPU_LIST_VALUE_FORMAT.matcher(str);
            if (matcher.find()) {
                int i = Integer.parseInt(matcher.group(1));
                String strGroup = matcher.group(2);
                if (strGroup != null && !strGroup.isEmpty()) {
                    int i2 = Integer.parseInt(strGroup);
                    while (i <= i2) {
                        collector.setBit(i);
                        i++;
                    }
                } else {
                    collector.setBit(i);
                }
            } else {
                Log.w("MCPE", "Unknown CPU List format: " + str);
            }
        }
        return collector;
    }

    public Set<CPUCluster> getClusterSet() {
        return new TreeSet(this.CLUSTERS);
    }

    public CPUCluster[] getClusterArray() {
        return (CPUCluster[]) this.CLUSTERS.toArray(new CPUCluster[0]);
    }

    public boolean isMultiClusterSystem() {
        return this.CLUSTERS.size() > 1;
    }

    public int getCPUClusterCount() {
        return this.CLUSTERS.size();
    }
}