package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryService {

    private List<List<Object>> executeQuery(String sqlFilePath, ResultSetProcessor processor) {
        List<List<Object>> results = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(new String(Files.readAllBytes(Paths.get(sqlFilePath))))) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                results.add(processor.process(rs));
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<MaxProjectCountClient> findMaxProjectsClient() {
        return (List<MaxProjectCountClient>) (List<?>) executeQuery("sql/find_max_projects_client.sql", rs -> {
            List<Object> row = new ArrayList<>();
            row.add(rs.getString("NAME"));
            row.add(rs.getInt("PROJECT_COUNT"));
            return row;
        });
    }

    public List<ProjectPrice> getProjectPrices() {
        return (List<ProjectPrice>) (List<?>) executeQuery("sql/print_project_prices.sql", rs -> {
            List<Object> row = new ArrayList<>();
            row.add(rs.getInt("NAME"));
            row.add(rs.getInt("DURATION"));
            row.add(rs.getInt("PRICE"));
            return row;
        });
    }

    public List<Worker> findYoungestEldestWorkers() {
        return (List<Worker>) (List<?>) executeQuery("sql/find_youngest_eldest_workers.sql", rs -> {
            List<Object> row = new ArrayList<>();
            row.add(rs.getString("TYPE"));
            row.add(rs.getString("NAME"));
            row.add(rs.getDate("BIRTHDAY"));
            return row;
        });
    }

    public List<MaxSalaryWorker> findMaxSalaryWorker() {
        return (List<MaxSalaryWorker>) (List<?>) executeQuery("sql/find_max_salary_worker.sql", rs -> {
            List<Object> row = new ArrayList<>();
            row.add(rs.getString("NAME"));
            row.add(rs.getInt("SALARY"));
            return row;
        });
    }

    public List<LongestProject> findLongestProject() {
        return (List<LongestProject>) (List<?>) executeQuery("sql/find_longest_project.sql", rs -> {
            List<Object> row = new ArrayList<>();
            row.add(rs.getInt("NAME"));
            row.add(rs.getInt("DURATION"));
            return row;
        });
    }

    @FunctionalInterface
    private interface ResultSetProcessor {
        List<Object> process(ResultSet rs) throws SQLException;
    }
}

class MaxProjectCountClient {
    private String name;
    private int projectCount;

    public MaxProjectCountClient(String name, int projectCount) {
        this.name = name;
        this.projectCount = projectCount;
    }

    public String getName() {
        return name;
    }

    public int getProjectCount() {
        return projectCount;
    }
}

class ProjectPrice {
    private int id;
    private int duration;
    private int price;

    public ProjectPrice(int id, int duration, int price) {
        this.id = id;
        this.duration = duration;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public int getPrice() {
        return price;
    }
}

class Worker {
    private String type;
    private String name;
    private java.sql.Date birthday;

    public Worker(String type, String name, java.sql.Date birthday) {
        this.type = type;
        this.name = name;
        this.birthday = birthday;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public java.sql.Date getBirthday() {
        return birthday;
    }
}

class MaxSalaryWorker {
    private String name;
    private int salary;

    public MaxSalaryWorker(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }
}

class LongestProject {
    private int id;
    private int duration;

    public LongestProject(int id, int duration) {
        this.id = id;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }
}
