package com.cmeza.sdgenerator.util;

import com.cmeza.sdgenerator.annotation.SDGenerator;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.asciithemes.a7.A7_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by carlos on 23/04/17.
 */
public class SDLogger {
    private static final Log commonsLogger = LogFactory.getLog(SDGenerator.class);
    private static org.apache.maven.plugin.logging.Log mavenLogger;
    private static int generated = 0;
    private static List<String> errors = new ArrayList<>();
    private static List<String> warns = new ArrayList<>();
    private static List<String> additionalExtend = new ArrayList<>();
    private static AsciiTable generatedTable;

    private SDLogger() {
    }

    public static void configure(org.apache.maven.plugin.logging.Log log) {
        mavenLogger = log;
    }

    public static void info(String message) {
        if (mavenLogger == null) {
            commonsLogger.info(message);
            return;
        }
        mavenLogger.info(message);
    }

    public static void debug(String message) {
        if (mavenLogger == null) {
            commonsLogger.debug(message);
            return;
        }
        mavenLogger.debug(message);
    }

    public static void error(String message) {
        if (mavenLogger == null) {
            commonsLogger.error(message);
            return;
        }
        mavenLogger.error(message);
    }

    public static Integer addError(String message) {
        errors.add(message);
        return errors.size();
    }

    public static Integer addWarn(String warn) {
        warns.add(warn);
        return warns.size();
    }

    public static Integer addAdditionalExtend(String ext) {
        additionalExtend.add(ext);
        return additionalExtend.size();
    }

    private static void printErrors() {
        printGenericTable("Errors", errors);
    }

    private static void printWarns() {
        printGenericTable("Warnings", warns);
    }

    private static void printAdditionalExtends() {
        printGenericTable("Additional Extends", additionalExtend);
    }

    private static void br() {
        br(1);
    }

    private static void br(int iterate) {
        IntStream.iterate(0, i -> i++).limit(iterate).forEach(j ->{
            if (mavenLogger == null) {
                commonsLogger.info(StringUtils.EMPTY);
                return;
            }
            mavenLogger.info(StringUtils.EMPTY);
        });
    }

    private static void printGenericTable(String title, List<String> messages) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(null, title + ": " + messages.size()).getCells().get(1).getContext().setTextAlignment(TextAlignment.CENTER);
        table.addRule();

        int count = 1;
        for (String mess : messages) {
            table.addRow("#" + count, mess).getCells().get(0).getContext().setTextAlignment(TextAlignment.CENTER);
            table.addRule();
            count ++;
        }

        table.getContext().setGrid(A7_Grids.minusBarPlusEquals());
        table.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{5, 101}));
        table.renderAsCollection().forEach(SDLogger::info);
    }

    public static void addRowGeneratedTable(Object... columns) {
        if (generatedTable == null) {
            initializeTable();
        }
        generatedTable.addRule();
        AT_Row row = generatedTable.addRow(columns);

        if (columns[0] != null) {
            row.getCells().get(0).getContext().setTextAlignment(TextAlignment.CENTER);
        }


        if (columns[2] != null) {
            row.getCells().get(2).getContext().setTextAlignment(TextAlignment.CENTER);
        }
    }

    public static void printGeneratedTables(boolean debug) {
        if (debug && generatedTable == null) {
            initializeTable();
        }

        if (generatedTable != null) {
            br();
            printBanner();
            br();

            if (!additionalExtend.isEmpty()) {
                br();
                printAdditionalExtends();
                br(2);
            }

            if (generated > 0) {
                addRowGeneratedTable( null, null, generated + " files generated");
            } else {
                addRowGeneratedTable( null, null, "No files generated");
            }
            generatedTable.addRule();
            generatedTable.getContext().setGrid(A7_Grids.minusBarPlusEquals());
            generatedTable.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{20, 68, 17}));
            generatedTable.renderAsCollection().forEach(SDLogger::info);

            br();

            if (!warns.isEmpty()) {
                printWarns();
                br(2);
            }

        }else {
            info(String.format("\u001B[1m\u001B[43m  %s:\u001B[0m\u001B[43m %s files generated  \u001B[0m", Constants.PROJECT_NAME, generated));
        }

        if (!errors.isEmpty()) {
            br();
            printErrors();
            br(2);
        }
    }

    private static void initializeTable() {
        generatedTable = new AsciiTable();
        generatedTable.addRule();
        AT_Row header  = generatedTable.addRow(Constants.TABLE_POSTFIX_COLUMN, Constants.TABLE_FILE_COLUMN, Constants.TABLE_RESULT_COLUMN);
        header.getCells().forEach(c -> c.getContext().setTextAlignment(TextAlignment.CENTER));
    }

    public static void plusGenerated(int plus) {
        generated += plus;
    }

    private static void printBanner() {
        List<String> banner = new ArrayList<>();
        banner.add("   _____            _                ____        __           ______                           __            ");
        banner.add("  / ___/____  _____(_)___  ____ _   / __ \\____ _/ /_____ _   / ____/__  ____  ___  _________ _/ /_____  _____");
        banner.add("  \\__ \\/ __ \\/ ___/ / __ \\/ __ `/  / / / / __ `/ __/ __ `/  / / __/ _ \\/ __ \\/ _ \\/ ___/ __ `/ __/ __ \\/ ___/");
        banner.add(" ___/ / /_/ / /  / / / / / /_/ /  / /_/ / /_/ / /_/ /_/ /  / /_/ /  __/ / / /  __/ /  / /_/ / /_/ /_/ / /    ");
        banner.add("/____/ .___/_/  /_/_/ /_/\\__, /  /_____/\\__,_/\\__/\\__,_/   \\____/\\___/_/ /_/\\___/_/   \\__,_/\\__/\\____/_/     ");
        banner.add("====/_/=================/____/======================================================================" + Constants.VERSION);
        banner.forEach(SDLogger::info);
    }

}
