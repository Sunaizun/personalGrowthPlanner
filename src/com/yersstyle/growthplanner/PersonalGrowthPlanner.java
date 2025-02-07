package com.yersstyle.growthplanner;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


interface ProgressLoggable {
    void logProgress(int amount);
}


interface Displayable {
    void displayGoalDetails();
}


abstract class Goal implements ProgressLoggable, Displayable {
    public String getDescription;
    protected String name;
    protected String description;
    protected int progress;
    protected int target;

    public Goal(String name, String description, int progress, int tsrget) {
        this.name = name;
        this.description = description;
        this.target = tsrget;
        this.progress = 0;
    }

    @Override
    public void logProgress(int amount) {
        progress += amount;
        if (progress > target) progress = target;
        System.out.println("Progress logged for " + name + ": " + progress + "/" + target);
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract int getTarget();

    public abstract int getProgress();
}


class FinanceGoal extends Goal {
    public FinanceGoal(String description, int target, int progress) {
        super("Finance", description, target, progress);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Finance Goal] " + description + " Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getTarget() {
        return 0;
    }

    @Override
    public int getProgress() {
        return 0;
    }
}

class StudyGoal extends Goal {
    public StudyGoal(String description, int target, int progress) {
        super("Study", description, target, progress);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Study Goal] " + description + " Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getTarget() {
        return 0;
    }

    @Override
    public int getProgress() {
        return 0;
    }
}

class HealthyFoodGoal extends Goal {
    public HealthyFoodGoal(String description, int target, int progress) {
        super("Healthy Food", description, target, progress);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Healthy Food Goal] " + description + " Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getTarget() {
        return 0;
    }

    @Override
    public int getProgress() {
        return 0;
    }
}

class WorkoutGoal extends Goal {
    public WorkoutGoal(String description, int target, int progress) {
        super("Workout", description, target, progress);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Workout Goal] " + description + " Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getTarget() {
        return 0;
    }

    @Override
    public int getProgress() {
        return 0;
    }
}


class GoalFactory {
    public static Goal createGoal(String type, String description, int target, int progress) {
        return switch (type.toLowerCase()) {
            case "finance" -> new FinanceGoal(description, target, progress);
            case "study" -> new StudyGoal(description, target, progress);
            case "healthy food" -> new HealthyFoodGoal(description, target, progress);
            case "workout" -> new WorkoutGoal(description, target, progress);
            default -> throw new IllegalArgumentException("Invalid goal type");
        };
    }
}


interface GoalRepository {
    void addGoalToDatabase(Goal goal) throws SQLException;
}



class DatabaseHelper implements GoalRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/personal_growth";
    private static final String USER = "postgres";
    private static final String PASSWORD = "yersultan2006";


    private static final String INSERT_GOALS_SQL = "INSERT INTO goals" + "(name, description, target, progress) VALUES"
            + "(?, ?, ?, ?);";

    @Override
    public void addGoalToDatabase(@org.jetbrains.annotations.NotNull Goal goal) throws SQLException {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(INSERT_GOALS_SQL)) {
            stmt.setString(1, goal.getName());
            stmt.setString(2, goal.getDescription());
            stmt.setInt(3, goal.getTarget());
            stmt.setInt(4, goal.getProgress());

            int rows = stmt.executeUpdate();
            System.out.println(rows + " rows inserted");
        }
    }

    public void getAllGoals(GoalRepository repo) throws SQLException {
        String sql = "SELECT * FROM goals";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int target = rs.getInt("target");
                int progress = rs.getInt("progress");
                System.out.println(id + " " + name + " " + description + " " + target + " " + progress);
            }
        }
    }



    public static class PersonalGrowthPlanner {
        private static final Scanner scanner = new Scanner(System.in);
        private static final GoalRepository goalRepository = new DatabaseHelper();

        public static void main(String[] args) throws SQLException {
            while (true) {
                System.out.println("\n=== Personal Growth Planner ===");
                System.out.println("1. Add Goal");
                System.out.println("2. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addGoal();
                        break;
                    case 2:
                        System.out.println("Exiting... Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }


        private static void addGoal() throws SQLException {
            System.out.println("\nChoose goal type:");
            System.out.println("1. Finance");
            System.out.println("2. Study");
            System.out.println("3. Healthy Food");
            System.out.println("4. Workout");
            System.out.print("Enter choice: ");
            int type = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter goal description: ");
            String description = scanner.nextLine();

            System.out.print("Enter target amount: ");
            int target = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter progress amount: ");
            int progress = scanner.nextInt();
            scanner.nextLine();

            String goalType;
            switch (type) {
                case 1:
                    goalType = "Finance";
                    break;
                case 2:
                    goalType = "Study";
                    break;
                case 3:
                    goalType = "Healthy Food";
                    break;
                case 4:
                    goalType = "Workout";
                    break;
                default:
                    System.out.println("Invalid type. Goal not added.");
                    return;
            }

            Goal goal = GoalFactory.createGoal(goalType, description, progress, progress);
            goalRepository.addGoalToDatabase(goal);
        }
    }
}
