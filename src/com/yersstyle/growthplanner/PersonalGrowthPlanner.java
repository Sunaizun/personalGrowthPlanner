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
    protected String name;
    protected String description;
    protected String plan;
    protected int progress;
    protected int target;


    public Goal(String name, String description, String plan, int progress, int target) {
        this.name = name;
        this.description = description;
        this.plan = plan;
        this.target = target;
        this.progress = progress;
    }

    @Override
    public void logProgress(int amount) {
        progress += amount;
        if (progress > target) progress = target;
        System.out.println("Progress logged for " + name + ": " + progress + "/" + target);
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getPlan();

    public abstract int getTarget();

    public abstract int getProgress();



}


class FinanceGoal extends Goal {
    public FinanceGoal(String description, String plan, int target, int progress) {
        super("Finance", description, plan, progress, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Finance Goal] " + description + plan + " Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPlan() {
        return plan;
    }

    @Override
    public int getTarget() {
        return target;
    }

    @Override
    public int getProgress() {
        return progress;
    }
}

class StudyGoal extends Goal {
    public StudyGoal(String description, String plan, int target, int progress) {
        super("Study", description, plan, progress, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Study Goal] " + description + plan + " Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPlan() {
        return plan;
    }

    @Override
    public int getTarget() {
        return target;
    }

    @Override
    public int getProgress() {
        return progress;
    }
}

class HealthyFoodGoal extends Goal {
    public HealthyFoodGoal(String description, String plan,  int target, int progress) {
        super("Healthy Food", description, plan,  progress, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Healthy Food Goal] " + description + plan + " Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPlan() {
        return plan;
    }

    @Override
    public int getTarget() {
        return target;
    }

    @Override
    public int getProgress() {
        return progress;
    }
}

class WorkoutGoal extends Goal {
    public WorkoutGoal(String description, String plan, int target, int progress) {
        super("Workout", description, plan,  progress, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Workout Goal] " + description + plan +" Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPlan() {
        return plan;
    }

    @Override
    public int getTarget() {
        return target;
    }

    @Override
    public int getProgress() {
        return progress;
    }
}

class AvoidHarmfulHabitsGoal extends Goal {
    public AvoidHarmfulHabitsGoal(String description, String plan, int target, int progress) {
        super("Avoid Harmful Habits", description, plan,  progress, target);
    }

    @Override
    public void displayGoalDetails() {
        System.out.println("[Avoid Harmful Habits] " + description + plan +" Progress: " + progress + "/" + target);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPlan() {
        return plan;
    }

    @Override
    public int getTarget() {
        return target;
    }

    @Override
    public int getProgress() {
        return progress;
    }
}



class GoalFactory {
    public static Goal createGoal(String name, String description, String plan, int target, int progress) {
        System.out.println("[Goal Factory] " + name + " Progress: " + progress + "/" + target);
        return switch (name.toLowerCase()) {
            case "finance" -> new FinanceGoal(description, plan, target, progress);
            case "study" -> new StudyGoal(description, plan, target, progress);
            case "healthy food" -> new HealthyFoodGoal(description, plan,  target, progress);
            case "workout" -> new WorkoutGoal(description, plan, target, progress);
            case "avoid harmful habits" -> new AvoidHarmfulHabitsGoal(description, plan, target, progress);
            default -> throw new IllegalArgumentException("Invalid goal type");
        }
   ; }
}


interface GoalRepository {
    void addGoalToDatabase(Goal goal) throws SQLException;
    void updateGoalInDatabase(int id, String description, String plan, int target, int progress) throws SQLException;
}

class DatabaseHelper implements GoalRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/personal_growth";
    private static final String USER = "postgres";
    private static final String PASSWORD = "yersultan2006";


    private static final String INSERT_GOALS_SQL =  "INSERT INTO goals (name, description,plan,  target, progress) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_GOALS_SQL = "UPDATE goals SET description = ?, plan = ?, target = ?, progress = ? WHERE name = ?";
    private GoalRepository repo;


    @Override
    public void addGoalToDatabase(Goal goal) throws SQLException {
        System.out.println("[Database Add Goal] " + goal.getName() + goal.getDescription() + goal.getTarget() + goal.getProgress());
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(INSERT_GOALS_SQL)) {
            System.out.println( goal.getName() + goal.getDescription() + goal.getPlan() + goal.getTarget() + goal.getProgress() );
            stmt.setString(1, goal.getName());
            stmt.setString(2, goal.getDescription());
            stmt.setString(3, goal.getPlan());
            stmt.setInt(3, goal.getTarget());
            stmt.setInt(4, goal.getProgress());

            int rows = stmt.executeUpdate();
            System.out.println(rows + " rows inserted");
        }
    }

    @Override
    public void updateGoalInDatabase(int id, String description, String plan, int target, int progress) throws SQLException {

    }

    public void updateGoalInDatabase(String name, String description, String plan, int target, int progress) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(UPDATE_GOALS_SQL)) {
            stmt.setString(1, description);
            stmt.setString(2, plan);
            stmt.setInt(3, target);
            stmt.setInt(4, progress);
            stmt.setString(5, name);
            int rows = stmt.executeUpdate();
            System.out.println(rows + " rows updated");
        }
    }

    public void getAllGoal() throws SQLException {
        String query = "SELECT * FROM goals";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String plan = rs.getString("plan");
                int target = rs.getInt("target");
                int progress = rs.getInt("progress");
                System.out.println(id + " " + name + " " + description + " "+ plan + " " + target + " " + progress);
            }
        }
    }


    public static class PersonalGrowthPlanner {
        private static final Scanner scanner = new Scanner(System.in);
        private static final GoalRepository goalRepository = new DatabaseHelper();
        private static String[] args;

        public static void main(String[] args) throws SQLException {
            PersonalGrowthPlanner.args = args;
            while (true) {
                System.out.println("\n=== Personal Growth Planner ===");
                System.out.println("1. Add Goal");
                System.out.println("2. Change Goal");
                System.out.println("2. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addGoal();
                        break;

                    case 2:
                        changeGoal();
                        break;
                        case 3:
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
            System.out.println("5. AvoidHarmful Habits");
            System.out.print("Enter choice: ");
            int type = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter goal description: ");
            String description = scanner.nextLine();

            System.out.println("Enter plan: ");
            String plan = scanner.nextLine();

            System.out.print("Enter target amount: ");
            int target = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter progress amount: ");
            int progress = scanner.nextInt();
            scanner.nextLine();

            String name;
            switch (type) {
                case 1:
                    name = "Finance";
                    break;
                case 2:
                    name = "Study";
                    break;
                case 3:
                    name = "Healthy Food";
                    break;
                case 4:
                    name = "Workout";
                    break;
                case 5:
                    name = "AvoidHarmful Habits";
                default:
                    System.out.println("Invalid name. Goal not added.");
                    return;
            }



            Goal goal = GoalFactory.createGoal(name, description, plan, target, progress);
            System.out.println("\nGoal added: " + goal.getName() + goal.getDescription() + goal.getPlan() + goal.getTarget() + goal.getProgress() + goal.getProgress());
            goalRepository.addGoalToDatabase(goal);
        }
        private static void changeGoal() throws SQLException {


            System.out.println("\nChoose new goal type:");
            System.out.println("1. Finance");
            System.out.println("2. Study");
            System.out.println("3. Healthy Food");
            System.out.println("4. Workout");
            System.out.println("5. Avoid Harmful Habits");
            System.out.print("Enter choice: ");
            int type = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new goal description: ");
            String description = scanner.nextLine();

            System.out.println("Enter new plan: ");
            String plan = scanner.nextLine();

            System.out.print("Enter new target amount: ");
            int target = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new progress amount: ");
            int progress = scanner.nextInt();
            scanner.nextLine();

            String name;
            switch (type) {
                case 1:
                    name = "Finance";
                    break;
                case 2:
                    name = "Study";
                    break;
                case 3:
                    name = "Healthy Food";
                    break;
                case 4:
                    name = "Workout";
                    break;
                case 5:
                    name = "Avoid Harmful Habits";
                    break;
                default:
                    System.out.println("Invalid choice. Goal not updated.");
                    return;
            }
            ((DatabaseHelper) goalRepository).updateGoalInDatabase(name, description, plan, target, progress);
        }
    }
}

