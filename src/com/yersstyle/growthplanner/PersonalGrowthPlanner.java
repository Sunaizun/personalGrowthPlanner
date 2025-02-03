package com.yersstyle.growthplanner;

import java.sql.*;
import java.util.*;


abstract class Goal {
    protected String name;
    protected String description;
    protected int progress;
    protected int target;

    public Goal(String name, String description, int target) {
        this.name = name;
        this.description = description;
        this.target = target;
        this.progress = 0;
    }

    public void logProgress(int amount) {
        progress += amount;
        if (progress > target) progress = target;
        System.out.println("Progress logged for " + name + ": " + progress + "/" + target);
    }

    public abstract void displayGoalDetails();
}


class FinanceGoal extends Goal {
    public FinanceGoal(String description, int target) {
        super("Finance", description, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Finance Goal] " + description + " Progress: " + progress + "/" + target);
    }
}

class StudyGoal extends Goal {
    public StudyGoal(String description, int target) {
        super("Study", description, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Study Goal] " + description + " Progress: " + progress + "/" + target);
    }
}

class HealthyFoodGoal extends Goal {
    public HealthyFoodGoal(String description, int target) {
        super("Healthy Food", description, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Healthy Food Goal] " + description + " Progress: " + progress + "/" + target);
    }
}

class WorkoutGoal extends Goal {
    public WorkoutGoal(String description, int target) {
        super("Workout", description, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Workout Goal] " + description + " Progress: " + progress + "/" + target);
    }
}


class GoalFactory {
    public static Goal createGoal(String type, String description, int target) {
        switch (type.toLowerCase()) {
            case "finance": return new FinanceGoal(description, target);
            case "study": return new StudyGoal(description, target);
            case "healthy food": return new HealthyFoodGoal(description, target);
            case "workout": return new WorkoutGoal(description, target);
            default: throw new IllegalArgumentException("Invalid goal type");
        }
    }
}

class DatabaseHelper {
    private static final String URL = "jdbc:postgresql://localhost:5432/personal_growth";
    private static final String USER = "postgres";
    private static final String PASSWORD = "yersultan2006";
    private Connection connection;

    public DatabaseHelper() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGoalToDatabase(String type, String description, int target) {
        String sql = "INSERT INTO goals (type, description, target, progress) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            stmt.setString(2, description);
            stmt.setInt(3, target);
            stmt.setInt(4, 0);
            stmt.executeUpdate();
            System.out.println("Goal added to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


public class PersonalGrowthPlanner {
    private static final Scanner scanner = new Scanner(System.in);
    private static DatabaseHelper dbHelper = new DatabaseHelper();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Personal Growth Planner ===");
            System.out.println("1. Add Goal");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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

    private static void addGoal() {
        System.out.println("\nChoose goal type:");
        System.out.println("1. Finance");
        System.out.println("2. Study");
        System.out.println("3. Healthy Food");
        System.out.println("4. Workout");
        System.out.print("Enter choice: ");
        int type = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter goal description: ");
        String description = scanner.nextLine();

        System.out.print("Enter target amount: ");
        int target = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String goalType;
        switch (type) {
            case 1: goalType = "Finance"; break;
            case 2: goalType = "Study"; break;
            case 3: goalType = "Healthy Food"; break;
            case 4: goalType = "Workout"; break;
            default:
                System.out.println("Invalid type. Goal not added.");
                return;
        }

        dbHelper.addGoalToDatabase(goalType, description, target);
    }
}