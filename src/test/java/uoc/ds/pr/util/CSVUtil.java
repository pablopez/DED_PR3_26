package uoc.ds.pr.util;

import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import org.apache.commons.csv.*;
import uoc.ds.pr.Role;
import uoc.ds.pr.pr2.SystemIssues;
import uoc.ds.pr.exceptions.DSException;
import uoc.ds.pr.pr3.SystemIssuesPR3;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class CSVUtil {

    @FunctionalInterface
    public interface DSBiConsumer<T, U> {
        void accept(T t, U u) throws DSException;
    }

    public static CSVParser getCVSParser(String filePath) {

        CSVParser csvParser = null;
        try {
//            System.out.println(filePath);
            InputStream inputStream = CSVUtil.class.getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }

            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvParser;
    }

    public static void processRows(String csvFile, String[] fields,
                                    SystemIssues systemIssues,
                                   DSBiConsumer<SystemIssues, CSVRecord > addFunction) throws DSException {
        CSVParser csvParser = getCVSParser(csvFile);
        for (CSVRecord record : csvParser) {
            addFunction.accept(systemIssues, record);
        }
    }

    public static void processRowsPR3(String csvFile, String[] fields,
                                   SystemIssuesPR3 systemIssues,
                                   DSBiConsumer<SystemIssuesPR3, CSVRecord > addFunction) throws DSException {
        CSVParser csvParser = getCVSParser(csvFile);
        for (CSVRecord record : csvParser) {
            addFunction.accept(systemIssues, record);
        }
    }

    public static void addWorkers(SystemIssues systemIssues) throws DSException {
        addDataSource("/workers.csv",
                new String[]{"workerId", "name", "address"},
                systemIssues,
                (si, v) -> si.addWorker(v[0], v[1], v[2])
        );
    }

    public static void addSystems(SystemIssues systemIssues) throws DSException {
        addDataSource("/systems.csv",
                new String[]{"systemId", "description", "location", "userId"},
                systemIssues,
                (si, v) -> si.addSystem(v[0], v[1], v[2], v[3])
        );
    }

    public static void addComponents(SystemIssues systemIssues) throws DSException {
        addDataSource("/components.csv",
                new String[]{"componentId", "trademark", "model", "serial"},
                systemIssues,
                (si, v) -> si.addComponent(v[0], v[1], v[2], v[3])
        );
    }

    public static void installComponent2Systems(SystemIssuesPR3 systemIssues) throws DSException {
        addDataSource("/installComponents2System.csv",
                new String[]{"componentId","systemId"},
                systemIssues,
                (si, v) -> {
                    si.installComponentToSystem(v[0], v[1]);
                }
        );
    }

    public static void addIssues(SystemIssues systemIssues) throws DSException {
        addDataSource("/issues.csv",
                new String[]{"issueId","componentId","issueTypeId","description","dateTime"},
                systemIssues,
                (si, v) -> {
                    si.createIssue(v[0], v[1], v[2], v[3], DateUtils.createDateTime(v[4]));
                }
        );
    }

    public static void addIssueAsignments(SystemIssues systemIssues) throws DSException {
        addDataSource("/issue_assignments.csv",
                new String[]{"issueId","workerId"},
                systemIssues,
                (si, v) -> {
                    si.assignIssue(v[0], v[1]);
                }
        );
    }

    public static void addRooms(SystemIssuesPR3 systemIssues) throws DSException {
        addDataSourcePR3("/rooms.csv",
                new String[]{"id","name"},
                systemIssues,
                (si, v) -> {
                    si.addRoom(v[0], v[1]);
                }
        );
    }

    public static void addUsers(SystemIssuesPR3 systemIssues) throws DSException {
        addDataSourcePR3("/users.csv",
                new String[]{"id","name", "role", "phone"},
                systemIssues,
                (si, v) -> {
                    si.addUser(v[0], v[1], Role.fromString(v[2]), v[3]);
                }
        );
    }

    public static void addIssueTypes(SystemIssuesPR3 systemIssues) throws DSException {
        addDataSourcePR3("/issue_types.csv",
                new String[]{"id","name"},
                systemIssues,
                (si, v) -> {
                    si.addIssueType(v[0], v[1]);
                }
        );
    }

    public static void addAssistances(SystemIssuesPR3 systemIssues) throws DSException {
        addDataSourcePR3("/assistances.csv",
                new String[]{"assistanceId","userId","issueTypeId","roomId","date","description"},
                systemIssues,
                (si, v) -> {
                    si.addAssistance(v[0], v[1], v[2], v[3], DateUtils.createLocalDate(v[4]), v[5]);
                }
        );
    }

    public static void assignWorkerToIssueType(SystemIssuesPR3 systemIssues) throws DSException {
        addDataSourcePR3("/worker_issueType_assignments.csv",
                new String[]{"workerId","issueTypeId"},
                systemIssues,
                (si, v) -> {
                    si.assignWorkerToIssueType(v[0], v[1]);
                }
        );
    }







    /**
     * Generic method to add data from a CSV source.
     * Uses BiConsumer to map an array of strings to the target method.
     */
    public static void addDataSourcePR3(
            String fileName,
            String[] headers,
            SystemIssuesPR3 systemIssues,
            DSBiConsumer<SystemIssuesPR3, String[]> methodMapping
    ) throws DSException {
        processRowsPR3(fileName, headers, systemIssues, (si, record) -> {
            String[] values = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                values[i] = record.get(headers[i]);
            }
            methodMapping.accept(si, values);
        });
    }

    public static void addDataSource(
            String fileName,
            String[] headers,
            SystemIssues systemIssues,
            DSBiConsumer<SystemIssues, String[]> methodMapping
    ) throws DSException {
        processRows(fileName, headers, systemIssues, (si, record) -> {
            String[] values = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                values[i] = record.get(headers[i]);
            }
            methodMapping.accept(si, values);
        });
    }

}

