package com.mojang.minecraftpe.hardwareinfo;

import java.util.BitSet;
import java.util.Set;

/* loaded from: classes.dex */
public class CPUCluster implements Comparable<CPUCluster> {
    private BitSet bitmask = new BitSet();
    private Set<SystemCPU> clusterCPUs;
    int[] cpuIds;
    private long maxFreq;
    private long minFreq;
    String siblingsString;

    CPUCluster(String siblingCPUs, Set<SystemCPU> cpus) {
        this.minFreq = 2147483647L;
        this.maxFreq = -2147483648L;
        this.clusterCPUs = cpus;
        this.siblingsString = siblingCPUs;
        this.cpuIds = new int[cpus.size()];
        int i = 0;
        for (SystemCPU systemCPU : cpus) {
            this.cpuIds[i] = systemCPU.getCPUId();
            this.bitmask.or(systemCPU.getCPUMask());
            this.minFreq = Math.min(systemCPU.getMinFrequencyHz(), this.minFreq);
            this.maxFreq = Math.max(systemCPU.getMaxFrequencyHz(), this.maxFreq);
            i++;
        }
    }

    public String getSiblingsString() {
        return this.siblingsString;
    }

    public boolean contains(int cpuId) {
        return this.clusterCPUs.contains(Integer.valueOf(cpuId));
    }

    public int[] getCPUIds() {
        return (int[]) this.cpuIds.clone();
    }

    public int getClusterCoreCount() {
        return this.clusterCPUs.size();
    }

    public SystemCPU[] getCPUArray() {
        Set<SystemCPU> set = this.clusterCPUs;
        return (SystemCPU[]) set.toArray(new SystemCPU[set.size()]);
    }

    public long getMinFreq() {
        return this.minFreq;
    }

    public long getMaxFreq() {
        return this.maxFreq;
    }

    public int hashCode() {
        return this.bitmask.hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(CPUCluster other) {
        BitSet bitSet = (BitSet) this.bitmask.clone();
        bitSet.xor(other.bitmask);
        if (bitSet.isEmpty()) {
            return 0;
        }
        return bitSet.length() == other.bitmask.length() ? -1 : 1;
    }
}