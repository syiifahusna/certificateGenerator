package com.example.certificateGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@SpringBootApplication
public class CertificateGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertificateGeneratorApplication.class, args);

		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

		// Get the heap memory usage
//		MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
//
//		// Print the heap memory information
//		System.out.println("Heap Memory Usage:");
//		System.out.println("   Initial: " + formatBytes(heapMemoryUsage.getInit()));
//		System.out.println("   Used: " + formatBytes(heapMemoryUsage.getUsed()));
//		System.out.println("   Committed: " + formatBytes(heapMemoryUsage.getCommitted()));
//		System.out.println("   Max: " + formatBytes(heapMemoryUsage.getMax()));

	}

//	private static String formatBytes(long bytes) {
//		// Convert bytes to megabytes for easier readability
//		long megabytes = bytes / (1024 * 1024);
//		return megabytes + " MB";
//	}

}
