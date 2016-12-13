package org.jspare.vertx.builder;

import java.util.ArrayList;
import java.util.List;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class ClasspathScannerUtils {
	
	/** The Constant ALL_SCAN_QUOTE. */
	protected static String ALL_SCAN_QUOTE = ".*";


	/**
	 * Scan and execute.
	 *
	 * @param cPackage
	 *            the package convetion
	 * @param perform
	 *            the perform
	 */
	protected static List<String> listClassesByPackage(String cPackage) {
		String packageForScan = cPackage;
		if (packageForScan.endsWith(".*")) {
			packageForScan = packageForScan.substring(0, packageForScan.length() - 2);
		}

		List<String> matchingClasses = new ArrayList<>();
		new FastClasspathScanner(packageForScan).scan().getNamesOfAllClasses().forEach(matchingClasses::add);
		return matchingClasses;
	}

}
